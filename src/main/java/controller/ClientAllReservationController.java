package controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Account;
import model.Flight;
import model.Reservation;
import model.Seat;
import service.ReservationService;
import service.SeatService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClientAllReservationController {

    public Label sumOfDurations;
    public Label mostCommonDeparture;
    public Label mostCommonArrival;
    public Label mostUsedCompany;
    public Label sumOfPrices;
    private Account account;

    private AccountAppController appController;
    private ReservationService reservationService;

    @FXML
    private TableView<Reservation> reservationTable;

    @FXML
    private TableColumn<Reservation, String> departureAirportColumn;
    @FXML
    private TableColumn<Reservation, String> arrivalAirportColumn;
    @FXML
    private TableColumn<Reservation, LocalDateTime> departureTimeColumn;
    @FXML
    private TableColumn<Reservation, LocalDateTime> arrivalTimeColumn;
    @FXML
    private TableColumn<Reservation, Number> flightDurationColumn;
    @FXML
    private TableColumn<Reservation, String> classTypeColumn;
    @FXML
    private TableColumn<Reservation, String> priceColumn;

    @FXML
    private Button returnButton;

    @FXML
    private Button deleteButton;


    @FXML
    private void initialize() {
        reservationTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);

        departureAirportColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getFlight()
                .getDepartureAirport().getAirport_code()));
        arrivalAirportColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getFlight()
                .getArrivalAirport().getAirport_code()));
        departureTimeColumn.setCellValueFactory(dataValue -> new SimpleObjectProperty(dataValue.getValue().getFlight().getDepartureTime()));
        arrivalTimeColumn.setCellValueFactory(dataValue -> new SimpleObjectProperty(dataValue.getValue().getFlight().getArrivalTime()));
        flightDurationColumn.setCellValueFactory(dataValue -> new SimpleLongProperty(dataValue.getValue().getFlight().getFlightDuration()));
        classTypeColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getSeat().getTypeClass().getClassType()));
        priceColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(
                Double.toString(dataValue.getValue().getFlight()
                        .getPrice().getPriceByType(dataValue.getValue().getSeat().getTypeClass()))));

        deleteButton.disableProperty().bind(
                Bindings.size(
                        reservationTable.getSelectionModel()
                                .getSelectedItems()).isNotEqualTo(1));

    }

    private static <T> T mostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<>();

        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Map.Entry<T, Integer> max = null;

        for (Map.Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return max.getKey();
    }

    public void setAppController(AccountAppController appController) {
        this.appController = appController;
    }

    public void setData(ReservationService reservationService,  Account account) {
        this.reservationService = reservationService;
        this.account = account;
        reservationTable.setItems(FXCollections.observableArrayList(account.getReservations()));
        updateStats();
    }

    @FXML
    public void handleReturnAction(ActionEvent actionEvent) {
        appController.showAllFlights();
    }

    @FXML
    public void handleDeleteAction(ActionEvent actionEvent) {

        Reservation reservationToRemove = reservationTable.getSelectionModel()
                .getSelectedItem();
        if(reservationToRemove.getFlight().getDepartureTime().isBefore(LocalDateTime.now())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText("FORBIDDEN ACTION!");
            alert.setContentText("Forbidden to delete past reservation!");
            alert.showAndWait();
        }else{
            reservationService.delete(reservationToRemove);
            reservationTable.setItems(FXCollections.observableArrayList(account.getReservations()));
            updateStats();
        }
    }

    private void updateStats() {
        if (account.getReservations().size() > 0) {
            sumOfDurations.setText(String.valueOf(
                    account.getReservations().stream().filter(f -> f.getFlight().getDepartureTime().isBefore(LocalDateTime.now()))
                            .mapToLong(r -> (long) r.getFlight().getFlightDuration()).sum()
            ));
            sumOfPrices.setText(String.valueOf(
                    account.getReservations().stream().mapToLong(r -> (long) r.getFlight().getPrice().getPriceByType(r.getSeat().getTypeClass())).sum()
            ));
            mostCommonDeparture.setText(String.valueOf(
                    mostCommon(account.getReservations().stream().map(r -> r.getFlight().getDepartureAirport()).collect(Collectors.toList())).getCity())
            );
            mostCommonArrival.setText(String.valueOf(
                    mostCommon(account.getReservations().stream().map(r -> r.getFlight().getArrivalAirport()).collect(Collectors.toList())).getCity())
            );
            mostUsedCompany.setText(String.valueOf(
                    mostCommon(account.getReservations().stream().map(r -> r.getFlight().getPlane().getCompany()).collect(Collectors.toList())).getName())
            );
        }
        else {
            sumOfDurations.setText(String.valueOf(0));
            sumOfPrices.setText(String.valueOf(0));
            mostCommonDeparture.setText("No data");
            mostCommonArrival.setText("No data");
            mostUsedCompany.setText("No data");
        }
    }
}
