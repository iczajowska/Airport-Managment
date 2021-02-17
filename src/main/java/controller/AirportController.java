package controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Airport;
import model.Flight;
import service.AirportService;

public class AirportController {

    private AirportService airportService;
    private AccountAppController appController;

    @FXML
    private TextField name;
    @FXML
    private TextField gmt;
    @FXML
    private TextField city;
    @FXML
    private TextField code;

    @FXML
    private Button Add;

    @FXML
    private Button Delete;

    @FXML
    private TableView<Airport> airportTable;
    @FXML
    private TableColumn<Airport, String> airportNameColumn;
    @FXML
    private TableColumn<Airport,Number> airportGMTColumn;
    @FXML
    private TableColumn<Airport, String> airportCityColumn;
    @FXML
    private TableColumn<Airport, String> airportCodeColumn;

    @FXML
    private void initialize() {
        airportTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);

        airportNameColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getName()));
        airportGMTColumn.setCellValueFactory(dataValue -> new SimpleIntegerProperty(dataValue.getValue()
                .getGmt_offset()));
        airportCityColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getCity()));
        airportCodeColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getAirport_code()));

        Delete.disableProperty().bind(
                Bindings.size(airportTable.getSelectionModel()
                        .getSelectedItems()).isNotEqualTo(1));


    }

    public void setData(AirportService airportService) {
        this.airportService = airportService;
        this.airportTable.setItems(FXCollections.observableArrayList(airportService.findAll()));
    }

    public void setAppController(AccountAppController appController) {
        this.appController = appController;
    }


    private String checkAirportCode(String airportCode) {
        if(airportCode.matches( "^[A-Z]{3}$" )) {
            return airportCode;
        }
        throw new IllegalArgumentException("WRONG AIRPORT CODE|Airport code should consist of three capital letters");
    }

    private String checkAirportCity(String airportCity) {
        if(airportCity.matches( "^[A-Z][a-z]*([ ][A-Z][a-z]*)*$" )) {
            return airportCity;
        }
        throw new IllegalArgumentException("WRONG CITY NAME|All parts of city name should start with uppercase letter followed by lower case (no digits or special signs).");
    }

    private String checkAirportGMT(String airportGMT) {
        if(airportGMT.matches( "^[-][1-9]$|^[-][1][0-2]$|^[0-9]$|^[1][0-2]$")) {
            return airportGMT;
        }
        throw new IllegalArgumentException("WRONG GMT OFFSET|GMT offset is an integer number from interval <-12; 12>");
    }

    private String checkAirportName(String airportName) {
        if(airportName.matches( "^[A-Z][a-z]*([ ][A-Z]*[a-z]*)*$" )) {
            return airportName;
        }
        throw new IllegalArgumentException("WRONG AIRPORT NAME |Airport name should start with capital letter. No signs or digits are allowed");
    }

    private void clearData() {
        name.clear();
        gmt.clear();
        city.clear();
        code.clear();
    }

    @FXML
    public void handleAddAction(ActionEvent actionEvent) {
        String airportName = name.getText();
        String airportGMT = gmt.getText();
        String airportCity = city.getText();
        String airportCode = code.getText();

        try {
            airportName = checkAirportName(airportName);
            airportGMT = checkAirportGMT(airportGMT);
            airportCity = checkAirportCity(airportCity);
            airportCode = checkAirportCode(airportCode);
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            String[] msg = e.getMessage().split("[|]");
            alert.setHeaderText(msg[0]);
            alert.setContentText(msg[1]);
            alert.showAndWait();
            return;
        }

        String finalAirportCode = airportCode;
        if (FXCollections.observableArrayList(airportService.findAll()).stream()
                .noneMatch(airport -> airport.getAirport_code().equals(finalAirportCode))) {
            Airport airport = new Airport(airportName, Integer.parseInt(airportGMT), airportCity, airportCode);
            airportService.persist(airport);
            clearData();
            appController.showAdminAirports();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText("AIRPORT CODE MUST BE UNIQUE!");
            alert.setContentText("Check airport code again");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleReturnAction(ActionEvent actionEvent) {
        appController.showAdminPanel();
    }

    @FXML
    public void handleDeleteAction(ActionEvent actionEvent) {
        Airport airportToRemove = airportTable.getSelectionModel()
                .getSelectedItem();

        if (airportToRemove.getDepartureFlights().isEmpty() && airportToRemove.getArrivalFlights().isEmpty()){
            airportService.delete(airportToRemove);
            appController.showAdminAirports();
        } else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText("FORBIDDEN ACTION!");
            alert.setContentText("You cannot delete airport because of existing flights");
            alert.showAndWait();
        }
    }
}
