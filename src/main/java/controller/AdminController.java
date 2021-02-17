package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AdminController {

    private AccountAppController appController;

    @FXML
    private Button goToCompaniesButton;
    @FXML
    private Button goToFlightsButton;
    @FXML
    private Button goToAirportsButton;


    @FXML
    private void initialize() {
    }

    public void setAppController(AccountAppController appController) {
        this.appController = appController;
    }

    @FXML
    public void handleGoToCompaniesAction(ActionEvent actionEvent) {
        appController.showAdminCompanies();
    }

    @FXML
    public void handleGoToFlightsAction(ActionEvent actionEvent) {
        appController.showAdminFlights();
    }

    @FXML
    public void handleGoToAirportsAction(ActionEvent actionEvent) {
        appController.showAdminAirports();
    }

    @FXML
    public void handleLogOutAction(ActionEvent actionEvent) { appController.initLoginLayout(); }
}
