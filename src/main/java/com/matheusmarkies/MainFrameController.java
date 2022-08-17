package com.matheusmarkies;

import com.matheusmarkies.manager.MouseTrapCarManager;
import com.matheusmarkies.popup.ConnectPopUpController;
import com.matheusmarkies.serialport.SerialReadder;
import com.matheusmarkies.serialport.SerialRunnable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.util.Duration;

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
    private LineChart<String, Double> line_chart;

    @FXML
    private MenuItem specifications_menu_button;

    @FXML
    private MenuBar menu_bar;

    XYChart.Series<String, Double> series = new XYChart.Series<String, Double>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        line_chart.setTitle("RPM/Time");

        series.setName("Data Series");

        line_chart.getXAxis().setLabel("Tempo (s)");
        line_chart.getYAxis().setLabel("RPM");
        line_chart.getData().addAll(series);

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

    public void setMouseTrapCarManager(MouseTrapCarManager mouseTrapCarManager) {
        this.mouseTrapCarManager = mouseTrapCarManager;
        this.mouseTrapCarManager.setMainFrameController(this);
    }

    public MouseTrapCarManager getMouseTrapCarManager() {
        return mouseTrapCarManager;
    }

    public LineChart<?, ?> getLineChart() {
        return line_chart;
    }

    public XYChart.Series<String, Double> getSeries() {
        return series;
    }

    public SerialReadder getSerialReadder() {
        return serialReadder;
    }
}
