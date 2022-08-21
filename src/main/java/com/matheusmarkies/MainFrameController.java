package com.matheusmarkies;

import com.matheusmarkies.manager.MouseTrapCarManager;
import com.matheusmarkies.objects.Car;
import com.matheusmarkies.popup.CarSettingsController;
import com.matheusmarkies.popup.ConnectPopUpController;
import com.matheusmarkies.serialport.SerialReadder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainFrameController implements Initializable {

    MouseTrapCarManager mouseTrapCarManager = new MouseTrapCarManager(this);

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem connect_menu_button;

    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();

    @FXML
    private LineChart<String, Double> rotation_chart;

    @FXML
    private MenuItem specifications_menu_button;

    @FXML
    private MenuBar menu_bar;

    XYChart.Series<String, Double> rotationSeries = new XYChart.Series<String, Double>();
    private Car car;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rotation_chart.setTitle("Rotation/Time");

        rotationSeries.setName("Data Series");

        rotation_chart.getXAxis().setLabel("Tempo (s)");
        rotation_chart.getYAxis().setLabel("Rotacoes");
        rotation_chart.getData().addAll(rotationSeries);

        rotation_chart.setCreateSymbols(false);

        rotation_chart.getStyleClass().add("chart");

        connect_menu_button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                openConnectPopUp();
            }
        });
    }

    private SerialReadder serialReadder = new SerialReadder();

    @FXML
    void onClickInConnectButton(ActionEvent event) {

    }

    @FXML
    void onClickInSpecificationsButton(ActionEvent event) {
        openCarSettingsPopUp();
    }

    void openConnectPopUp(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/com/matheusmarkies/mousetrapcar/ConnectPopUp.fxml"));
            Parent root = fxmlLoader.load();

            ConnectPopUpController connectPopUpController = (ConnectPopUpController)fxmlLoader.getController();

            connectPopUpController.setMouseTrapCarManager(this.mouseTrapCarManager);
            connectPopUpController.setSerialReadder(this.serialReadder);

            Stage stage = new Stage();
            stage.setTitle("Conectar");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException ignored) {
            System.err.println(ignored);
        }
    }

    void openCarSettingsPopUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/com/matheusmarkies/mousetrapcar/CarSettings.fxml"));
            Parent root = fxmlLoader.load();

            CarSettingsController carSettingsController = (CarSettingsController) fxmlLoader.getController();
            carSettingsController.setMainFrameController(this);

            Stage stage = new Stage();
            stage.setTitle("Predefinicoes");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException ignored) {
            System.err.println(ignored);
        }
    }

    private boolean chartRefresh;
    public void chartRefresh(boolean b){
        if(!chartRefresh && b){
        //Start Reciver Samples
        }
        if(chartRefresh && !b){
        //Stop Reciver Samples
        }
        chartRefresh = b;
    }

    public void setMouseTrapCarManager(MouseTrapCarManager mouseTrapCarManager) {
        this.mouseTrapCarManager = mouseTrapCarManager;
        this.mouseTrapCarManager.setMainFrameController(this);
    }

    public MouseTrapCarManager getMouseTrapCarManager() {
        return mouseTrapCarManager;
    }

    public LineChart<?, ?> getLineChart() {
        return rotation_chart;
    }

    public XYChart.Series<String, Double> getRotationSeries() {
        return rotationSeries;
    }

    public SerialReadder getSerialReadder() {
        return serialReadder;
    }

    public Car getCar() { return car; }

    public void setCar(Car car) { this.car = car; }
}
