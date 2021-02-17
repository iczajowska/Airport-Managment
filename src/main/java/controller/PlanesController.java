package controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Company;
import model.Plane;
import service.PlaneService;

import java.util.Optional;

public class PlanesController {

    private PlaneService planeService;

    private Company company;
    private AccountAppController appController;

    @FXML
    private Label companyName;

    @FXML
    private TextField model;
    @FXML
    private TextField capacity;
    @FXML
    private TextField businessCapacity;
    @FXML
    private TextField firstClassCapacity;

    @FXML
    private TableView<Plane> planesTable;

    @FXML
    private TableColumn<Plane, String> modelColumn;
    @FXML
    private TableColumn<Plane, Number> capacityColumn;
    @FXML
    private TableColumn<Plane, Number> businessCapacityColumn;
    @FXML
    private TableColumn<Plane, Number> firstClassCapacityColumn;

    @FXML
    private Button Add;

    @FXML
    private Button Delete;

    @FXML
    private Button Return;


    @FXML
    private void initialize() {
        planesTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE);


        modelColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getModel()));
        capacityColumn.setCellValueFactory(dataValue -> new SimpleIntegerProperty(dataValue.getValue().getCapacity()));
        businessCapacityColumn.setCellValueFactory(dataValue -> new SimpleIntegerProperty(dataValue.getValue().getBusinessCapacity()));
        firstClassCapacityColumn.setCellValueFactory(dataValue -> new SimpleIntegerProperty(dataValue.getValue().getFirstClassCapacity()));

        Delete.disableProperty().bind(
                Bindings.size(
                        planesTable.getSelectionModel()
                                .getSelectedItems()).isNotEqualTo(1));
    }


    public void setAppController(AccountAppController appController) {
        this.appController = appController;
    }


    public void setData(PlaneService planeService, Company company) {
        this.planeService = planeService;
        this.company = company;
        companyName.setText(company.getName());
        planesTable.setItems(FXCollections.observableArrayList(company.getPlanes()));
    }

    private String checkModelPlane(String modelPlane) {
        if (modelPlane.matches("^([a-zA-Z0-9]|[- @\\.#&!])*$") && !modelPlane.isEmpty()) {
            return modelPlane;
        }
        throw new IllegalArgumentException("WRONG MODEL PLANE|Plane model may contain digits and signs and can not be empty");

    }
    private boolean checkPositiveInteger(String num){
        return num.matches("^[1-9][0-9]*$");
    }
    private void checkCapacity(String capacityPlane, String businessCapacityPlane, String firstClassCapacityPlane) {

        if(!checkPositiveInteger(capacityPlane)){
            throw new IllegalArgumentException("WRONG CAPACITY PLANE|Plane capacity must be a positive integer");
        }
        if(!checkPositiveInteger(businessCapacityPlane)){
            throw new IllegalArgumentException("WRONG BUSINESS CAPACITY PLANE|Plane business capacity must be a positive integer");
        }
        if(!checkPositiveInteger(firstClassCapacityPlane)){
            throw new IllegalArgumentException("WRONG FIRST CLASS CAPACITY PLANE|Plane first class capacity must be a positive integer");
        }

        int c = Integer.parseInt(capacityPlane);
        int bc = Integer.parseInt(businessCapacityPlane);
        int fcc = Integer.parseInt(firstClassCapacityPlane);

        if(!(bc+fcc<c)){
            throw new IllegalArgumentException("WRONG CAPACITY PLANE|Sum of first class capacity and business capacity must be lower than plane capacity");
        }
    }


    private void clearData() {
        model.clear();
        capacity.clear();
        businessCapacity.clear();
        firstClassCapacity.clear();
    }


    @FXML
    public void handleAddAction(ActionEvent actionEvent) {
        String modelPlane = model.getText();
        String capacityPlane = capacity.getText();
        String businessCapacityPlane = businessCapacity.getText();
        String firstClassCapacityPlane = firstClassCapacity.getText();


        try {
            modelPlane = checkModelPlane(modelPlane);
            checkCapacity(capacityPlane,businessCapacityPlane,firstClassCapacityPlane);
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            String[] msg = e.getMessage().split("[|]");
            alert.setHeaderText(msg[0]);
            alert.setContentText(msg[1]);
            alert.showAndWait();
            return;
        }

        Plane newPlane = new Plane(modelPlane,company,Integer.parseInt(capacityPlane),Integer.parseInt(businessCapacityPlane),Integer.parseInt(firstClassCapacityPlane));
        company.addPlane(newPlane);
        newPlane.setCompany(company);
        planeService.persist(newPlane);
        clearData();
        appController.showAdminCompanyPlanes(company);
    }


    @FXML
    public void handleReturnAction(ActionEvent actionEvent) {
        appController.showAdminCompanies();
    }

    @FXML
    public void handleDeleteAction(ActionEvent actionEvent) {
        Plane planeToRemove = planesTable.getSelectionModel()
                .getSelectedItem();
        planeService.delete(planeToRemove);
        company.removePlane(planeToRemove);
        appController.showAdminCompanyPlanes(company);
    }
}
