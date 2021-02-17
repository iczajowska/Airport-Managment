package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import model.*;
import service.ReservationService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddReservationController {
    private ReservationService reservationService;
    private Flight flight;
    private AccountAppController appController;

    @FXML
    private TextArea myText;

    @FXML
    private RadioButton radioButton;
    @FXML
    private RadioButton radioButton2;
    @FXML
    private RadioButton radioButton3;

    @FXML
    private Button cancelButton;
    @FXML
    private Button addButton;

    @FXML
    private void initialize() {
    }

    public void setAppController(AccountAppController appController) {
        this.appController = appController;
    }

    public void setData(ReservationService reservationService, Flight flight) {
        this.reservationService = reservationService;
        this.flight = flight;
        myText.appendText(this.flight.toString() + "\nPrices:\n" + this.flight.getPrice().toString());
        myText.setEditable(false);
    }

    private boolean checkDateReservation(List<Reservation> reservations, LocalDateTime departure, LocalDateTime arrival) {
        return reservations.stream().allMatch(res -> res.getFlight().getDepartureTime().isAfter(arrival) ||
                res.getFlight().getArrivalTime().isBefore(departure));
    }

    private TypeClass getTypeClass() {
        TypeClass td;
        if (radioButton.isSelected()) {
            td = TypeClass.ECONOMY;
        } else if (radioButton2.isSelected()) {
            td = TypeClass.BUSINESS;
        } else if (radioButton3.isSelected()) {
            td = TypeClass.FIRST_CLASS;
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText("NONE CLASS CHOSEN");
            alert.setContentText("You must choose class!");
            alert.showAndWait();
            return null;
        }
        return td;
    }

    @FXML
    public void handleAddAction(ActionEvent actionEvent) {
        // TODO check DAO compatibility of this line
        Account account = this.appController.getCurrentAccount();

        TypeClass td = getTypeClass();
        if (td == null) return;

        LocalDateTime departureTime = this.flight.getDepartureTime();
        LocalDateTime arrivalTime = this.flight.getArrivalTime();

        if (!checkDateReservation(account.getReservations(), departureTime, arrivalTime)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText("Collision Detected");
            alert.setContentText("You already have overlapping reservation!");
            alert.showAndWait();
            this.appController.showAllFlights();
            return;
        }

        Seat seat = this.flight.bookSeat(td);
        Reservation reservation = new Reservation(account, this.flight, seat);

        reservationService.persist(reservation);
        this.appController.showAllFlights();
    }

    @FXML
    public void handleCancelAction(ActionEvent actionEvent) {
        this.appController.showAllFlights();
    }
}
