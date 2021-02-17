package controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.LocalDateStringConverter;
import model.*;
import service.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class FlightController {

    private FlightService flightService;
    private AirportService airportService;
    private PlaneService planeService;
    private CompanyService companyService;
    private PriceService priceService;
    private AccountAppController appController;
    private Company currentCompany;

    @FXML
    private TextField arrivalTime;
    @FXML
    private TextField departureTime;
    @FXML
    private TextField flightNumber;
    @FXML
    private ComboBox<String> arrivalAirport;
    @FXML
    private ComboBox<String> departureAirport;
    @FXML
    private ComboBox<String> company;
    @FXML
    private ComboBox<String> plane;
    @FXML
    private  TextField economyPrice;
    @FXML
    private  TextField businessPrice;
    @FXML
    private  TextField firstClassPrice;

    @FXML
    private Button Add;
    @FXML
    private Button Delete;
    @FXML
    private Button Return;
    @FXML
    private Button Update;

    @FXML
    private TableView<Flight> flightTable;
    @FXML
    private TableColumn<Flight, LocalDateTime> flightArrivalTimeColumn;
    @FXML
    private TableColumn<Flight, LocalDateTime> flightDepartureTimeColumn;
    @FXML
    private TableColumn<Flight, String> flightNumberColumn;
    @FXML
    private TableColumn<Flight, String> flightArrivalAirportColumn;
    @FXML
    private TableColumn<Flight, String> flightDepartureAirportColumn;
    @FXML
    private TableColumn<Flight, String> flightCompanyColumn;
    @FXML
    private TableColumn<Flight, String> flightPlaneColumn;

    @FXML
    private void initialize() {
        flightTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);

        flightArrivalTimeColumn.setCellValueFactory(dataValue -> new SimpleObjectProperty<LocalDateTime>(dataValue.getValue()
                .getArrivalTime()));
        flightDepartureTimeColumn.setCellValueFactory(dataValue -> new SimpleObjectProperty<LocalDateTime>(dataValue.getValue()
                .getDepartureTime()));
        flightNumberColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getFlightNumber()));
        flightArrivalAirportColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getArrivalAirport().getName()));
        flightDepartureAirportColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getDepartureAirport().getName()));
        flightCompanyColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getPlane().getCompany().getName()));
        flightPlaneColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getPlane().getModel()));

        Delete.disableProperty().bind(
                Bindings.size(flightTable.getSelectionModel()
                        .getSelectedItems()).isNotEqualTo(1));
        Update.disableProperty().bind(
                Bindings.size(flightTable.getSelectionModel()
                        .getSelectedItems()).isNotEqualTo(1));

    }

    public void setData(Services services) {
        this.flightService = services.getFlightService();
        this.airportService = services.getAirportService();
        this.planeService = services.getPlaneService();
        this.companyService = services.getCompanyService();
        this.priceService = services.getPriceService();
        List<Airport> airports = this.airportService.findAll();
        this.arrivalAirport.setItems(FXCollections.observableArrayList(airports.stream()
                .map(Airport::getAirport_code).collect(Collectors.toList())));
        this.departureAirport.setItems(FXCollections.observableArrayList(airports.stream()
                .map(Airport::getAirport_code).collect(Collectors.toList())));
        List<Company> companies = this.companyService.findAll();
        this.company.setItems(FXCollections.observableArrayList(companies.stream()
                .map(Company::getName).collect(Collectors.toList())));
        this.flightTable.setItems(FXCollections.observableArrayList(flightService.findAll()));
        this.company.setOnAction((e)->{
            this.currentCompany = companyService.findAll().stream().filter(c-> c.getName()
                    .equals(this.company.getSelectionModel().getSelectedItem())).collect(Collectors.toList()).get(0);
            List <Plane> planes = this.currentCompany.getPlanes();
            this.plane.setItems(FXCollections.observableArrayList(planes.stream()
                    .map(Plane::getModel).collect(Collectors.toList())));
        });
    }

    public void setAppController(AccountAppController appController) {
        this.appController = appController;
    }

    private String checkFlightNumber(String flightFlightNumber) {
        if (flightFlightNumber.matches("^[A-Z]{2,3}[1-9]{3,4}$")) {
            return flightFlightNumber;
        }
        throw new IllegalArgumentException("WRONG FLIGHT NUMBER| Flight number format XX000 or XXX000, where X - capital letter, 0 - digit. ");
    }

    private String checkTime(String flightTime) {
        if (flightTime.matches("^[0-9]{4}-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])\\s(2[0-3]|[0-1][0-9]):([0-5][0-9])$")) {
            return flightTime;
        }
        throw new IllegalArgumentException("WRONG TIME FORMAT| Expected time format yyyy-MM-dd HH:mm");

    }

    private double checkFirstClass(String firstClass, double business) {
        if (firstClass.matches("(^[1-9][0-9]*$)|(^[1-9][0-9]*\\.[0-9]{1,2}$)")) {
            double fc = Double.parseDouble(firstClass);
            if(business<fc){
                return fc;
            }
            throw new IllegalArgumentException("WRONG FIRST CLASS PRICE| First class price must be higher than business price");
        }
        else{
            throw new IllegalArgumentException("WRONG FIRST CLASS PRICE| Price should contain only digits");
        }
    }

    private double checkBusiness(String business,double economy) {
        if (business.matches("(^[1-9][0-9]*$)|(^[1-9][0-9]*\\.[0-9]{1,2}$)")) {
            double b = Double.parseDouble(business);
            if(b>economy)
                return b;
            throw new IllegalArgumentException("WRONG BUSINESS PRICE| Business price must be higher than economy price");
        }
        else{
            throw new IllegalArgumentException("WRONG BUSINESS PRICE| Price should contain only digits");
        }
    }

    private Double checkEconomy(String economy) {
        if (economy.matches("(^[1-9][0-9]*$)|(^[1-9][0-9]*\\.[0-9]{1,2}$)")) {
            return Double.parseDouble(economy);
        }
        throw new IllegalArgumentException("WRONG FLIGHT NUMBER| Price should contain only digits");
    }


    @FXML
    public void handleAddAction(ActionEvent actionEvent) {
        String flightArrivalTime = arrivalTime.getText();
        String flightDepartureTime = departureTime.getText();
        String flightFlightNumber = flightNumber.getText();
        String flightArrivalAirport = arrivalAirport.getValue();
        String flightDepartureAirport = departureAirport.getValue();
        String flightPlane = plane.getValue();
        String economy = economyPrice.getText();
        double ep, fcp, bp;
        String business = businessPrice.getText();
        String firstClass = firstClassPrice.getText();


        try {
            flightArrivalTime = checkTime(flightArrivalTime);
            flightDepartureTime = checkTime(flightDepartureTime);
            flightFlightNumber = checkFlightNumber(flightFlightNumber);
            ep = checkEconomy(economy);
            bp = checkBusiness(business, ep);
            fcp = checkFirstClass(firstClass, bp);
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            String[] msg = e.getMessage().split("[|]");
            alert.setHeaderText(msg[0]);
            alert.setContentText(msg[1]);
            alert.showAndWait();
            return;
        }

        String finalFlightNumber = flightFlightNumber;
        if ( ! flightService.findAll().stream().noneMatch(flight -> flight.getFlightNumber().equals(finalFlightNumber))) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText("FLIGHT CODE MUST BE UNIQUE!");
            alert.setContentText("Check flight code again");
            alert.showAndWait();
            return;
        }

        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime arrivalTime = LocalDateTime.parse(flightArrivalTime, formatter);
        LocalDateTime departureTime = LocalDateTime.parse(flightDepartureTime, formatter);
        Airport airportArrival = airportService.findAll().stream().filter(a-> a.getAirport_code()
                .equals(flightArrivalAirport)).collect(Collectors.toList()).get(0);
        Airport airportDeparture = airportService.findAll().stream().filter(a-> a.getAirport_code()
                .equals(flightDepartureAirport)).collect(Collectors.toList()).get(0);
        Plane plane =  planeService.findAll().stream().filter(p-> p.getModel()
                .equals(flightPlane)).collect(Collectors.toList()).get(0);

        if(!arrivalTime.isAfter(departureTime) || !departureTime.isAfter(LocalDateTime.now())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText("WRONG TIMES!");
            alert.setContentText("Arrival time must be after departure time and must be in the future!");
            alert.showAndWait();
            return;
        }

        if(airportArrival.equals(airportDeparture)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText("AIRPORTS MUST BE DIFFERENT!");
            alert.setContentText("Departure and Arrival airports can not be the same!");
            alert.showAndWait();
            return;
        }


        Price price = new Price(ep, bp, fcp);
        priceService.persist(price);
        Flight flight = new Flight(flightFlightNumber, plane, airportDeparture, airportArrival, departureTime, arrivalTime, price);
        flightService.persist(flight);
        appController.showAdminFlights();
    }


    @FXML
    public void handleReturnAction(ActionEvent actionEvent) {
        appController.showAdminPanel();
    }

    @FXML
    public void handleDeleteAction(ActionEvent actionEvent) {
        Flight flightToRemove = flightTable.getSelectionModel()
                .getSelectedItem();
        flightService.delete(flightToRemove);
        appController.showAdminFlights();
    }

    @FXML
    public void handleUpdateAction(ActionEvent actionEvent) {
        Flight flightToUpdate = flightTable.getSelectionModel()
                .getSelectedItem();

        if(flightToUpdate.getDepartureTime().isBefore(LocalDateTime.now())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText("FORBIDDEN ACTION!");
            alert.setContentText("Forbidden to update past flight!");
            alert.showAndWait();
        }else{
            appController.updateAdminFlights(flightToUpdate);
        }
    }
}
