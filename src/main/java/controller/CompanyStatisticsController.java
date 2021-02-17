package controller;

import com.sun.javafx.charts.Legend;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import model.Company;
import model.Flight;
import model.Plane;
import service.CompanyService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompanyStatisticsController {
    @FXML
    public Label totalFlightsNumber;
    @FXML
    public Label mostCommonDeparture;
    @FXML
    public Label mostCommonArrival;
    @FXML
    public Label mostUsedPlane;

    private CompanyService companyService;
    private Company data;
    private AccountAppController appController;

    @FXML
    private Pane statisticsChart;

    public void initialize() {

    }

    public void setAppController(AccountAppController appController) {
        this.appController = appController;
    }

    public void setData(CompanyService companyService, Company company) {
        this.companyService = companyService;
        this.data = company;

        ObservableList<XYChart.Series> seriesList = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> aList = FXCollections.observableArrayList();

        Map<Integer, Double> map = company.getBenefit(LocalDateTime.now().getYear()-1);
        for(int key=1; key<=12; key++){
            if(map.containsKey(key)){
                System.out.println("Key:" + key + "Value" + map.get(key));
                aList.add(new XYChart.Data(key, map.get(key)));
            }
            else{
                aList.add(new XYChart.Data(key, 0));
            }
        }

        seriesList.add(new XYChart.Series("Company" + company.getName(), aList));

        Axis xAxis = new NumberAxis("Months", 1, 12, 1);
        Axis yAxis = new NumberAxis("Earnings", 0, 1000, 100);
        LineChart chart = new LineChart(xAxis, yAxis, seriesList);
        chart.setTitle(company.getName()+ " Statistics");
        statisticsChart.getChildren().add(chart);

        updateStats();

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

    private void updateStats() {
        if(data.getPlanes().isEmpty() || data.getPlanes().stream().mapToLong(p->p.getFlights().size()).sum() == 0){
            totalFlightsNumber.setText(String.valueOf(0));
            mostCommonDeparture.setText("No data");
            mostCommonArrival.setText("No data");
            mostUsedPlane.setText("No data");
        }else{
            totalFlightsNumber.setText(String.valueOf(
                    data.getPlanes().stream().mapToLong(p -> p.getFlights().size()).sum()
            ));
            mostCommonDeparture.setText(String.valueOf(
                    mostCommon(data.getPlanes().stream().map(
                            p -> p.getFlights().stream().map(
                                    Flight::getDepartureAirport).collect(Collectors.toList())
                    ).collect(Collectors.toList()).stream().flatMap(List::stream).collect(Collectors.toList())).getCity())
            );
            mostCommonArrival.setText(String.valueOf(
                    mostCommon(data.getPlanes().stream().map(
                            p -> p.getFlights().stream().map(
                                    Flight::getArrivalAirport).collect(Collectors.toList())
                    ).collect(Collectors.toList()).stream().flatMap(List::stream).collect(Collectors.toList())).getCity())
            );
            mostUsedPlane.setText(String.valueOf(
                    mostCommon(new ArrayList<>(data.getPlanes())).getModel())
            );
        }
    }

    @FXML
    public void handleReturnAction(ActionEvent actionEvent) {
        appController.showAdminCompanies();
    }
}

