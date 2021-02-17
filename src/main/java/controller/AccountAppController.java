package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Account;
import model.Company;
import model.Flight;
import service.Services;
import org.hibernate.Session;
import util.Emails;

import java.io.IOException;

public class AccountAppController {
    private final Stage primaryStage;
    private Account currentAccount;
    private final Services services;


    public AccountAppController(Stage primaryStage,  Session session) {
        this.primaryStage = primaryStage;
        this.services = new Services(session);

        Thread emails = new Thread(new Emails(services));
        emails.start();
    }

    public void initLoginLayout() {
        try {
            this.primaryStage.setTitle("Ponczusie App Log in");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AccountAppController.class
                    .getResource("../view/AccountLogIn.fxml"));
            StackPane rootLayout = loader.load();

            // set initial data into controller
            AccountLoginController controller = loader.getController();
            controller.setAppController(this);
            controller.setData(services.getAccountService());

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAllFlights() {
        try {
            this.primaryStage.setTitle("Ponczusie App Flights");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AccountAppController.class
                    .getResource("../view/FlightReservationPane.fxml"));
            BorderPane rootLayout = loader.load();

            // set initial data into controller
            FlightReservationController controller = loader.getController();
            controller.setAppController(this);
//            controller.setData(dataGenerator.getFlightData());
            controller.setData(services.getFlightService());

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddReservationLayout(Flight flight) {
        try {
            this.primaryStage.setTitle("Ponczusie App Add Reservation");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AccountAppController.class
                    .getResource("../view/AddReservationPane.fxml"));
            BorderPane rootLayout = loader.load();

            // set initial data into controller
            AddReservationController controller = loader.getController();
            controller.setAppController(this);
            controller.setData(services.getReservationService(), flight);

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showClientAllReservations(){
        try {
            this.primaryStage.setTitle("Ponczusie Show Reservations");
            System.out.println("INIT LAYOUT");
            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AccountAppController.class
                    .getResource("../view/ClientAllReservationPane.fxml"));
            BorderPane rootLayout = loader.load();

            // set initial data into controller
            ClientAllReservationController controller = loader.getController();
            controller.setAppController(this);
            controller.setData(services.getReservationService(), currentAccount);

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSignUpLayout() {
        try {
            this.primaryStage.setTitle("Ponczusie App Sign Up");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AccountAppController.class
                    .getResource("../view/AccountSignUp.fxml"));
            StackPane rootLayout = loader.load();

            // set initial data into controller
            AccountSignUpController controller = loader.getController();
            controller.setAppController(this);
            controller.setData(services.getAccountService());

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO buttons to different views Authorisation
    public void showAdminPanel() { //main admin panel
        try {
            this.primaryStage.setTitle("Ponczusie App Admin Panel");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AccountAppController.class
                    .getResource("../view/AdminPanel.fxml"));
            BorderPane rootLayout = loader.load();

            // set initial data into controller
            AdminController controller = loader.getController();
            controller.setAppController(this);
//            controller.setData(dataGenerator.getCompanyData());

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAdminAirports() {
        try {
            this.primaryStage.setTitle("Ponczusie App Airports");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AccountAppController.class
                    .getResource("../view/AirportsPanel.fxml"));
            BorderPane rootLayout = loader.load();

            // set initial data into controller
            AirportController controller = loader.getController();
            controller.setAppController(this);
//            controller.setData(dataGenerator.getAirportData());
            controller.setData(services.getAirportService());

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAdminFlights() {
        // MOVE TO DAO
        try {
            this.primaryStage.setTitle("Ponczusie App Flights");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AccountAppController.class
                    .getResource("../view/FlightsPanel.fxml"));
            BorderPane rootLayout = loader.load();

            // set initial data into controller
            FlightController controller = loader.getController();
            controller.setAppController(this);
            controller.setData(services);

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateAdminFlights(Flight currentFlight) {
        // MOVE TO DAO
        try {
            this.primaryStage.setTitle("Ponczusie App Update Flights");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AccountAppController.class
                    .getResource("../view/UpdateFlightPane.fxml"));
            StackPane rootLayout = loader.load();

            // set initial data into controller
            UpdateFlightController controller = loader.getController();
            controller.setAppController(this);
            controller.setData(currentFlight, services);

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAdminCompanyPlanes(Company company) {
        try {
            this.primaryStage.setTitle("Ponczusie App Company Plane");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AccountAppController.class
                    .getResource("../view/PlanesPanel.fxml"));
            BorderPane rootLayout = loader.load();

            // set initial data into controller
            PlanesController controller = loader.getController();
            controller.setAppController(this);
            controller.setData(services.getPlaneService(), company);

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showAdminCompanies() {
        try {
            this.primaryStage.setTitle("Ponczusie App Companies");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AccountAppController.class
                    .getResource("../view/CompaniesPane.fxml"));
            BorderPane rootLayout = loader.load();

            // set initial data into controller
            CompanyController controller = loader.getController();
            controller.setAppController(this);
            controller.setData(services.getCompanyService());

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCompanyStatistics(Company company) {
        try {
            this.primaryStage.setTitle("Ponczusie App Companies Statistics");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AccountAppController.class
                    .getResource("../view/CompanyStatistics.fxml"));
            BorderPane rootLayout = loader.load();

            // set initial data into controller
            CompanyStatisticsController controller = loader.getController();
            controller.setAppController(this);
            controller.setData(services.getCompanyService(),company);

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public Account getCurrentAccount() {
        return this.currentAccount;
    }

    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
    }


}
