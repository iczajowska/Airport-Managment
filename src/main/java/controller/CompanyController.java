package controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Company;
import model.Flight;
import model.Plane;
import service.CompanyService;

import java.util.Optional;
import java.util.regex.Pattern;

public class CompanyController {

    private CompanyService companyService;

    private AccountAppController appController;

    @FXML
    private TextField name;
    @FXML
    private TextField street;
    @FXML
    private TextField localNumber;
    @FXML
    private TextField city;
    @FXML
    private TextField postcode;
    @FXML
    private TextField country;
    @FXML
    private TextField nip;
    @FXML
    private TextField regon;

    @FXML
    private Button Add;

    @FXML
    private Button showPlanesButton;

    @FXML
    private Button showStatisticsButton;

    @FXML
    private TableView<Company> companyTable;
    @FXML
    private TableColumn<Company, String> nameCompanyColumn;
    @FXML
    private TableColumn<Company, String> streetCompanyColumn;
    @FXML
    private TableColumn<Company, String> numberCompanyColumn;
    @FXML
    private TableColumn<Company, String> cityCompanyColumn;
    @FXML
    private TableColumn<Company, String> countryCompanyColumn;
    @FXML
    private TableColumn<Company, String> postcodeCompanyColumn;
    @FXML
    private TableColumn<Company, String> nipCompanyColumn;
    @FXML
    private TableColumn<Company, String> regonCompanyColumn;


    public void initialize() {

        companyTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);

        nameCompanyColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getName()));
        streetCompanyColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getStreet()));
        numberCompanyColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getNumber()));
        cityCompanyColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getCity()));
        countryCompanyColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getCountry()));
        postcodeCompanyColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getPostcode()));
        nipCompanyColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getNIP()));
        regonCompanyColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue()
                .getREGON()));


        showPlanesButton.disableProperty().bind(
                Bindings.size(
                        companyTable.getSelectionModel()
                                .getSelectedItems()).isNotEqualTo(1));

        showStatisticsButton.disableProperty().bind(
                Bindings.size(
                        companyTable.getSelectionModel()
                                .getSelectedItems()).isNotEqualTo(1));
    }

    public void setAppController(AccountAppController appController) {
        this.appController = appController;
    }

    public void setData(CompanyService companyService) {
        this.companyService = companyService;
//        this.data = company;
        this.companyTable.setItems(FXCollections.observableArrayList(companyService.findAll()));
    }

    private String checkCompanyName(String companyName) {
        if (companyName.matches("^[A-Z]([a-zA-Z0-9]|[- @\\.#&!])*$")) {
            return companyName;
        }
        throw new IllegalArgumentException("WRONG COMPANY NAME|Company name should start with uppercase letter, may contain digits and signs");
    }

    private String checkCompanyStreet(String companyStreet) {
        if (companyStreet.matches("^[A-Z][a-z]*([ ][A-Z][a-z]*)*$")) {
            return companyStreet;
        }
        throw new IllegalArgumentException("WRONG STREET NAME|Street name should start with uppercase letter followed by lower case (no digits or special signs).");
    }

    private String checkCompanyNumber(String companyNumber) {
        if (companyNumber.matches("^[1-9][0-9]*([ /][1-9][0-9]*)*$")) {
            return companyNumber;
        }
        throw new IllegalArgumentException("WRONG COMPANY NUMBER|Expected number format 111/123 or 111.");
    }

    private String checkCompanyCity(String companyCity) {
        if (companyCity.matches("^[A-Z][a-z]*([ ][A-Z][a-z]*)*$")) {
            return companyCity;
        }
        throw new IllegalArgumentException("WRONG CITY NAME|City should start with uppercase letter followed by lower case (no digits or special signs).");
    }

    private String checkPostCode(String postCode) {
        if (postCode.matches("^[0-9]{2}([-][0-9]{3})$")) {
            return postCode;
        }
        throw new IllegalArgumentException("WRONG POST CODE|Expected post code format: 11-111");
    }

    private String checkCompanyCountry(String companyCountry) {
        if (companyCountry.matches("^[A-Z][a-z]*([ ][A-Z][a-z]*)*$")) {
            return companyCountry;
        }
        throw new IllegalArgumentException("WRONG COUNTRY NAME|All parts of country name should start with uppercase letter followed by lower case (no digits or special signs).");
    }

    private String checkNip(String nip) {
        if (nip.matches("^[0-9]{10}$")) {
            return nip;
        }
        throw new IllegalArgumentException("WRONG NIP NUMBER|Expected NIP format: 1111111111 (10 signs)");
    }

    private String checkRegon(String regon) {
        if (regon.matches("^[0-9]{9}$")) {
            return regon;
        }
        throw new IllegalArgumentException("WRONG REGON NUMBER|Expected REGON format: 111111111 (9 signs)");
    }

    private void clearData() {
        name.clear();
        street.clear();
        localNumber.clear();
        city.clear();
        country.clear();
        postcode.clear();
        nip.clear();
        regon.clear();
    }

    @FXML
    public void handleAddAction(ActionEvent actionEvent) {
        String nameCompany = name.getText();
        String streetCompany = street.getText();
        String localNumberCompany = localNumber.getText();
        String cityCompany = city.getText();
        String postCodeCompany = postcode.getText();
        String countryCompany = country.getText();
        String nipCompany = nip.getText();
        String regonCompany = regon.getText();

        try {
            nameCompany = checkCompanyName(nameCompany);
            streetCompany = checkCompanyStreet(streetCompany);
            localNumberCompany = checkCompanyNumber(localNumberCompany);
            cityCompany = checkCompanyCity(cityCompany);
            postCodeCompany = checkPostCode(postCodeCompany);
            countryCompany = checkCompanyCountry(countryCompany);
            nipCompany = checkNip(nipCompany);
            regonCompany = checkRegon(regonCompany);
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            String[] msg = e.getMessage().split("[|]");
            alert.setHeaderText(msg[0]);
            alert.setContentText(msg[1]);
            alert.showAndWait();
            return;
        }

        String finalNipCompany = nipCompany;
        if (FXCollections.observableArrayList(companyService.findAll()).stream().noneMatch(company -> company.getNIP().equals(finalNipCompany))) {
            Company c = new Company(nameCompany, streetCompany, localNumberCompany, cityCompany, countryCompany, postCodeCompany, nipCompany, regonCompany);
            companyService.persist(c);
            clearData();
            appController.showAdminCompanies();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText("COMPANY WITH NIP NUMBER ALREADY EXISTS!");
            alert.setContentText("If company is not already in system, check your nip again");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleReturnAction(ActionEvent actionEvent) {
        appController.showAdminPanel();
    }

    @FXML
    public void handleShowPlanesAction(ActionEvent actionEvent) {
        Company company = companyTable.getSelectionModel()
                .getSelectedItem();
        if (company != null) {
            appController.showAdminCompanyPlanes(company);
        }
    }

    @FXML
    public void handleShowStatisticsAction(ActionEvent actionEvent) {
        Company company = companyTable.getSelectionModel()
                .getSelectedItem();
        if (company != null) {
            appController.showCompanyStatistics(company);
        }
    }
}
