package controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Flight;
import service.FlightService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FlightReservationController {

    public Label numOfSeats;
    public Label numOfFlights;
    //    private FlightData data;
    private FlightService flightService;

    private AccountAppController appController;

    @FXML
    private TextField filterFieldFrom;
    @FXML
    private TextField filterFieldTo;
    @FXML
    private TextField filterFieldDepartTime;
    @FXML
    private TextField filterFieldArrTime;
    @FXML
    private TextField filterFieldDurTime;
    @FXML
    private TextField filterFieldPriceFrom;

    @FXML
    private TableView<Flight> flightTable;
    @FXML
    private TableColumn<Flight, String> departureAirportColumn;
    @FXML
    private TableColumn<Flight, String> arrivalAirportColumn;
    @FXML
    private TableColumn<Flight, LocalDateTime> departureTimeColumn;
    @FXML
    private TableColumn<Flight, LocalDateTime> arrivalTimeColumn;
    @FXML
    private TableColumn<Flight, Number> flightDurationColumn;
    @FXML
    private TableColumn<Flight, String> priceColumn;

    @FXML
    private TableView<Flight> flightRecommendTable;
    @FXML
    private TableColumn<Flight, String> departureAirportRecommendColumn;
    @FXML
    private TableColumn<Flight, String> arrivalAirportRecommendColumn;
    @FXML
    private TableColumn<Flight, LocalDateTime> departureTimeRecommendColumn;
    @FXML
    private TableColumn<Flight, LocalDateTime> arrivalTimeRecommendColumn;
    @FXML
    private TableColumn<Flight, Number> flightDurationRecommendColumn;
    @FXML
    private TableColumn<Flight, String> priceRecommendColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button showButton;


    @FXML
    private void initialize() {
        flightTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);

        departureAirportColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getDepartureAirport().getAirport_code()));
        arrivalAirportColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getArrivalAirport().getAirport_code()));
        departureTimeColumn.setCellValueFactory(dataValue -> new SimpleObjectProperty( dataValue.getValue()
                .getDepartureTime()));
        arrivalTimeColumn.setCellValueFactory(dataValue -> new SimpleObjectProperty(dataValue.getValue()
                .getArrivalTime()));
        flightDurationColumn.setCellValueFactory(dataValue ->  new SimpleLongProperty(dataValue.getValue()
                .getFlightDuration()));
        priceColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(
                Double.toString(dataValue.getValue()
                        .getPrice().getEconomyClassPrice())));


        flightRecommendTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);

        departureAirportRecommendColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getDepartureAirport().getAirport_code()));
        arrivalAirportRecommendColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getArrivalAirport().getAirport_code()));
        departureTimeRecommendColumn.setCellValueFactory(dataValue -> new SimpleObjectProperty(dataValue.getValue()
                .getDepartureTime()));
        arrivalTimeRecommendColumn.setCellValueFactory(dataValue -> new SimpleObjectProperty(dataValue.getValue()
                .getArrivalTime()));
        flightDurationRecommendColumn.setCellValueFactory(dataValue ->  new SimpleLongProperty(dataValue.getValue()
                .getFlightDuration()));
        priceRecommendColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(
                Double.toString(dataValue.getValue()
                        .getPrice().getEconomyClassPrice())));

        addButton.disableProperty().bind(

            Bindings.and(Bindings.size(flightRecommendTable.getSelectionModel()
                    .getSelectedItems()).isNotEqualTo(1), Bindings.size(flightTable.getSelectionModel()
                    .getSelectedItems()).isNotEqualTo(1))
        );

        flightTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                flightRecommendTable.getSelectionModel().clearSelection();
            }
        });

        flightRecommendTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                flightTable.getSelectionModel().clearSelection();
            }
        });

//        ArrayList<Flight> flights = new ArrayList();
//        Flight f = flightTable.getSelectionModel().getSelectedItem();
//        if (f != null) {
//            ObservableList<Flight> flight = FXCollections.observableList(flights);
//            int a = flight.stream().mapToInt(fl -> (int) fl.getAllSeats().stream().filter(s -> !s.isBooked()).count()).sum();
//            numOfSeats.setText(String.valueOf(a));
//        } else {
//            f = flightRecommendTable.getSelectionModel()
//                    .getSelectedItem();
//            if (f != null){
//                ObservableList<Flight> flight = FXCollections.observableList(flights);
//                int a = flight.stream().mapToInt(fl -> (int) fl.getAllSeats().stream().filter(s -> !s.isBooked()).count()).sum();
//                numOfSeats.setText(String.valueOf(a));
//            }
//        }
    }


    public void setAppController(AccountAppController appController) {
        this.appController = appController;
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        Flight flight = flightTable.getSelectionModel()
                .getSelectedItem();
        if (flight != null) {
            appController.showAddReservationLayout(flight);
        } else {
            flight = flightRecommendTable.getSelectionModel()
                    .getSelectedItem();
            if (flight != null){
                appController.showAddReservationLayout(flight);
            }
        }
    }

    public void setData(FlightService flightService) {
        this.flightService = flightService;
//        this.data = flightData;
        //flightTable.setItems(FXCollections.observableArrayList(this.flightService.findAll()));
        ObservableList<Flight> data = FXCollections.observableArrayList(
                this.flightService.findAll().stream().filter(f -> f.getDepartureTime().isAfter(LocalDateTime.now())).collect(Collectors.toList())
        );
        FilteredList<Flight> filteredData = new FilteredList<>(data,p->true);
        filterFieldFrom.textProperty().addListener((observable, oldValue, newValue) -> {
            setFilter(filteredData);
        });
        filterFieldTo.textProperty().addListener((observable, oldValue, newValue) -> {
            setFilter(filteredData);
            });
        filterFieldDurTime.textProperty().addListener((observable, oldValue, newValue) -> {
            setFilter(filteredData);
        });
        filterFieldDepartTime.textProperty().addListener((observable, oldValue, newValue) -> {
            setFilter(filteredData);
        });
        filterFieldArrTime.textProperty().addListener((observable, oldValue, newValue) -> {
            setFilter(filteredData);
        });
        filterFieldPriceFrom.textProperty().addListener((observable, oldValue, newValue) -> {
            setFilter(filteredData);
        });
        flightTable.setItems(filteredData);


        ObservableList<Flight> filteredRecommendData = FXCollections.observableArrayList(
                this.flightService.findAll().stream().filter(f -> f.getDepartureTime().isAfter(LocalDateTime.now())).collect(Collectors.toList())
        );
        Comparator<Flight> comparator = Comparator.comparingDouble(Flight::getEconomyClassPrice);
        FXCollections.sort(filteredRecommendData, comparator);
        int numberOfRecommendedFlights = Math.min(3, filteredRecommendData.size());
        flightRecommendTable.setItems(FXCollections.observableArrayList(filteredRecommendData.subList(0, numberOfRecommendedFlights)));

        numOfFlights.setText(String.valueOf(filteredData.size()));
        int a = filteredData.stream().mapToInt(f -> (int) f.getAllSeats().stream().filter(s -> !s.isBooked()).count()).sum();
        numOfSeats.setText(String.valueOf(a));
    }

    private void setFilter (FilteredList<Flight> filteredData)
    {
        String from = filterFieldFrom.getText ();
        String to = filterFieldTo.getText ();
        String arrTime = filterFieldArrTime.getText ();
        String depTime= filterFieldDepartTime.getText ();
        String durTime= filterFieldDurTime.getText ();
        String priceFrom=filterFieldPriceFrom.getText();

        filteredData.setPredicate (flight ->
        {
            boolean p0 = flight.getDepartureAirport ().getAirport_code().contains(from) || from.isEmpty() || from ==null;
            boolean p1 = flight.getArrivalAirport ().getAirport_code().contains(to)|| to.isEmpty() || to ==null;

            String pattern = "yyyy-MM-dd HH:mm";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

            LocalDateTime arrivalTime = null;

            if(checkTime(arrTime))
                arrivalTime = LocalDateTime.parse(arrTime, formatter);

            LocalDateTime departureTime = null;

            if(checkTime(depTime))
                departureTime = LocalDateTime.parse(depTime, formatter);

            Long durationTime = null;

            if(checkIfLong(durTime))
                durationTime = Long.valueOf(durTime);

            Double price = null;

            if(checkPrice(priceFrom))
                price= Double.valueOf(priceFrom);

            boolean p2 = arrivalTime == null || flight.getArrivalTime().isBefore(arrivalTime);
            boolean p3 = departureTime == null || flight.getDepartureTime().isAfter(departureTime);
            boolean p4 = durationTime == null || flight.getFlightDuration() <= durationTime;
            boolean p5 = price == null || flight.getPrice().getEconomyClassPrice() <= price;
            return p0 && p1 && p2 && p3 && p4 && p5;
        });

        numOfFlights.setText(String.valueOf(filteredData.size()));
        int a = filteredData.stream().mapToInt(f -> (int) f.getAllSeats().stream().filter(s -> !s.isBooked()).count()).sum();
        numOfSeats.setText(String.valueOf(a));
    }

    private boolean checkTime(String flightArrivalTime) {
        if (flightArrivalTime.matches("^[0-9]{4}-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])\\s(2[0-3]|[0-1][0-9]):([0-5][0-9])$")) {
            System.out.println("true");
            return true;
        }
        return false;
    }

    private boolean checkIfLong(String longNumber) {
        if (longNumber.matches("(^[1-9][0-9]*$)")) {
            return true;
        }
        return false;
    }

    private boolean checkPrice(String price) {
        if (price.matches("(^[1-9][0-9]*$)|(^[1-9][0-9]*\\.[0-9]{1,2}$)")) {
            return true;
        }
        return false;
    }


    @FXML
    public void handleShowAction(ActionEvent actionEvent) {
        appController.showClientAllReservations();
    }

    @FXML
    public void handleLogOutAction(ActionEvent actionEvent) { appController.initLoginLayout(); }
}
