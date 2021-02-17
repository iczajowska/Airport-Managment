package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;
import model.*;
import service.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateFlightController {
    private FlightService flightService;
    private AirportService airportService;
    private PlaneService planeService;
    private CompanyService companyService;
    private PriceService priceService;
    private AccountAppController appController;
    private Company currentCompany;
    private Flight currentFlight;

    @FXML
    private TextField arrivalTime;
    @FXML
    private TextField departureTime;
    @FXML
    private Label flightNumber;
    @FXML
    private ComboBox arrivalAirport;
    @FXML
    private ComboBox departureAirport;
    @FXML
    private ComboBox company;
    @FXML
    private ComboBox plane;
    @FXML
    private TextField economyPrice;
    @FXML
    private TextField businessPrice;
    @FXML
    private TextField firstClassPrice;

    @FXML
    private Button Update;

    public void setData(Flight currentFlight, Services services) {
        this.flightService = services.getFlightService();
        this.airportService = services.getAirportService();
        this.planeService = services.getPlaneService();
        this.companyService = services.getCompanyService();
        this.priceService = services.getPriceService();
        this.currentFlight = currentFlight;

        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTimeStringConverter converter = new LocalDateTimeStringConverter(formatter, formatter);

        this.flightNumber.setText(currentFlight.getFlightNumber());
        this.arrivalTime.setText(converter.toString(this.currentFlight.getArrivalTime()));
        this.departureTime.setText(converter.toString(this.currentFlight.getDepartureTime()));
        this.economyPrice.setText(String.valueOf(this.currentFlight.getPrice().getEconomyClassPrice()));
        this.businessPrice.setText(String.valueOf(this.currentFlight.getPrice().getBusinessClassPrice()));
        this.firstClassPrice.setText(String.valueOf(this.currentFlight.getPrice().getFirstClassPrice()));
        List<Airport> airports = this.airportService.findAll();
        this.arrivalAirport.setItems(FXCollections.observableArrayList(airports.stream()
                .map(Airport::getAirport_code).collect(Collectors.toList())));
        this.arrivalAirport.setValue(this.currentFlight.getArrivalAirport().getAirport_code());
        this.departureAirport.setItems(FXCollections.observableArrayList(airports.stream()
                .map(Airport::getAirport_code).collect(Collectors.toList())));
        this.departureAirport.setValue(this.currentFlight.getDepartureAirport().getAirport_code());
        List<Company> companies = this.companyService.findAll();
        this.company.setItems(FXCollections.observableArrayList(companies.stream()
                .map(Company::getName).collect(Collectors.toList())));
        this.company.setValue(this.currentFlight.getPlane().getCompany().getName());
        this.company.setOnAction((e)->{
            this.currentCompany = companyService.findAll().stream().filter(c-> c.getName()
                    .equals(this.company.getSelectionModel().getSelectedItem())).collect(Collectors.toList()).get(0);
            List <Plane> planes = this.currentCompany.getPlanes();
            this.plane.setItems(FXCollections.observableArrayList(planes.stream()
                    .map(Plane::getModel).collect(Collectors.toList())));
        });
        this.plane.setValue(this.currentFlight.getPlane().getModel());
    }

    public void setAppController(AccountAppController appController) {
        this.appController = appController;
    }

    private String checkDepartureTime(String flightDepartureTime) {
        if (flightDepartureTime.matches("^[0-9]{4}-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])\\s(2[0-3]|[0-1][0-9]):([0-5][0-9])$")) {
            return flightDepartureTime;
        }
        throw new IllegalArgumentException("WRONG TIME FORMAT| Expected time format yyyy-MM-dd HH:mm");

    }

    private String checkArrivalTime(String flightArrivalTime) {
        if (flightArrivalTime.matches("^[0-9]{4}-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])\\s(2[0-3]|[0-1][0-9]):([0-5][0-9])$")) {
            return flightArrivalTime;
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
        throw new IllegalArgumentException("WRONG ECONOMY PRICE| Price should contain only digits");
    }

    @FXML
    public void handleUpdateAction(ActionEvent actionEvent) {
        String flightArrivalTime = arrivalTime.getText();
        String flightDepartureTime = departureTime.getText();
        String flightArrivalAirport = arrivalAirport.getValue().toString();
        String flightDepartureAirport = departureAirport.getValue().toString();
        String flightPlane = plane.getValue().toString();
        String economy = economyPrice.getText();
        double ep, fcp, bp;
        String business = businessPrice.getText();
        String firstClass = firstClassPrice.getText();

        try {
            flightArrivalTime = checkArrivalTime(flightArrivalTime);
            flightDepartureTime = checkDepartureTime(flightDepartureTime);
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

        if( reservationsCheck(plane, arrivalTime, departureTime, airportArrival, airportDeparture) ){
            return;
        }

        if(  !plane.equals(this.currentFlight.getPlane())){
            this.currentFlight.setPlane(plane);
        }
        this.currentFlight.setArrivalTime(arrivalTime);
        this.currentFlight.setDepartureTime(departureTime);
        this.currentFlight.setArrivalAirport(airportArrival);
        this.currentFlight.setDepartureAirport(airportDeparture);
        this.currentFlight.getPrice().setPrices(ep, bp, fcp);
        flightService.update(currentFlight);
        appController.showAdminFlights();
   }

   private void showAlert(){
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setTitle("Alert");
       alert.setHeaderText("YOU CANNOT CHANGE THIS FLIGHT");
       alert.setContentText("This flight has been already reserved by customers!");
       alert.showAndWait();
   }

   private boolean reservationsCheck(Plane plane, LocalDateTime arrivalTime, LocalDateTime departureTime, Airport airportArrival, Airport airportDeparture){
       if(  !plane.equals(this.currentFlight.getPlane())
               || !arrivalTime.equals(this.currentFlight.getArrivalTime())
               || !departureTime.equals(this.currentFlight.getDepartureTime())
               || !airportArrival.equals(this.currentFlight.getArrivalAirport())
               || !airportDeparture.equals(this.currentFlight.getDepartureAirport())
         ) {
           if(!this.currentFlight.getReservations().isEmpty()){
               showAlert();
               return true;
           }
       }
       return false;
   }

   @FXML
    public void handleReturnAction(ActionEvent actionEvent) {
        appController.showAdminFlights();
    }
}
