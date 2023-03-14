package com.matheusmarkies;

import com.matheusmarkies.manager.RotationManager;
import com.matheusmarkies.manager.analysis.ChartIntegration;
import com.matheusmarkies.manager.utilities.Save;
import com.matheusmarkies.manager.utilities.XLS;
import com.matheusmarkies.objects.Car;
import com.matheusmarkies.objects.FileTypeFilter;
import com.matheusmarkies.objects.RotationList;
import com.matheusmarkies.popup.CarSettingsController;
import com.matheusmarkies.popup.ConnectPopUpController;
import com.matheusmarkies.serialport.SerialReadder;
import com.matheusmarkies.serialport.SerialRunnable;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.matheusmarkies.manager.utilities.Save.ApplicationFolder;
import static com.matheusmarkies.manager.utilities.Save.ImportantDirectories;

public class MainFrameController implements Initializable {

    RotationManager rotationManager = new RotationManager(this);

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
    private AreaChart<String, Double> movement_chart;

    @FXML
    private LineChart<String, Double> corrention_curve_chart;

    @FXML
    private LineChart<String, Double> average_chart;

    @FXML
    private MenuItem specifications_menu_button;

    @FXML
    private MenuBar menu_bar;

    @FXML
    private javafx.scene.control.Button openButton;

    @FXML
    private javafx.scene.control.Button saveButton;
    private ChartIntegration chartIntegration = new ChartIntegration();

    private Car car;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rotation_chart.setTitle("Rotacao/Tempo");
        corrention_curve_chart.setTitle("Velocidade/Tempo");
        average_chart.setTitle("Distancia/Tempo");
        movement_chart.setTitle("Tipo de Movimento");

        chartIntegration =  new ChartIntegration(this);
//
        chartIntegration.getRotationSeries().setName("Rotacoes");
        chartIntegration.getAverageSeries().setName("Media");
        chartIntegration.getSmoothedSeries().setName("Curva Media");

        chartIntegration.getMovementDetailsChart().acceleratedSeries.setName("Acelerado");
        chartIntegration.getMovementDetailsChart().constantSeries.setName("Constante");
        chartIntegration.getMovementDetailsChart().retardedSeries.setName("Retardado");

        rotation_chart.getXAxis().setLabel("Tempo (s)");
        rotation_chart.getYAxis().setLabel("RPM");

        average_chart.getXAxis().setLabel("Tempo (s)");
        average_chart.getYAxis().setLabel("Distancia (m)");

        corrention_curve_chart.getXAxis().setLabel("Tempo (s)");
        corrention_curve_chart.getYAxis().setLabel("Velocidade (m/s)");

        movement_chart.getXAxis().setLabel("Tempo (s)");
        movement_chart.getYAxis().setLabel("Velocidade (m/s)");

        rotation_chart.getData().addAll(
                chartIntegration.getRotationSeries()
        );

        average_chart.getData().addAll(chartIntegration.getAverageSeries());
        corrention_curve_chart.getData().addAll(chartIntegration.getSmoothedSeries());

        movement_chart.getData().addAll(
                chartIntegration.getMovementDetailsChart().acceleratedSeries,
                chartIntegration.getMovementDetailsChart().retardedSeries,
                chartIntegration.getMovementDetailsChart().constantSeries
        );

        rotation_chart.setCreateSymbols(false);
        movement_chart.setCreateSymbols(false);
        average_chart.setCreateSymbols(false);
        corrention_curve_chart.setCreateSymbols(false);

        rotation_chart.setAnimated(false);
        movement_chart.setAnimated(false);
        average_chart.setAnimated(false);
        corrention_curve_chart.setAnimated(false);

        rotation_chart.getStyleClass().add("chart");
        //movement_chart.getStyleClass().add("chart");
        average_chart.getStyleClass().add("chart");
        corrention_curve_chart.getStyleClass().add("chart");

        ScheduledExecutorService scheduledExecutorService;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                chartIntegration.setAnalysisToChart(0);
            });
        }, 0, 200, TimeUnit.MILLISECONDS);

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
    void onClickInSave(ActionEvent event) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        try {
            Save.write(rotationManager.getRotationList(),ImportantDirectories[0] + "\\CarInfo_"+timeStamp+".rsf");
        } catch (IOException e) {
            System.err.println(e);
        }
        try {
            XLS.createXLSFile(rotationManager.getRotationList());
        } catch (IOException e) {
            System.err.println(e);
        }
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

            connectPopUpController.setMouseTrapCarManager(this.rotationManager);
            connectPopUpController.setSerialReadder(this.serialReadder);

            Stage stage = new Stage();
            stage.setTitle("Conectar");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException ignored) {
            System.err.println(ignored);
        }
    }

    @FXML
    void onClickInReset(ActionEvent event) {
        getMouseTrapCarManager().getRotationsHistory().clear();
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

    @FXML
    void onClickInOpen(ActionEvent event) {
        RotationList rotationList = new RotationList();
        JFileChooser jfc = new JFileChooser(new File(ImportantDirectories[0]));
        FileFilter bridgeFilter = new FileTypeFilter(".rsf", "Race Save File");

        jfc.setFileFilter(bridgeFilter);
        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            try{
                rotationList = (RotationList) Save.read(selectedFile);
                rotationManager.setRotationList(rotationList);
            }catch (Exception e){

            }
        }

    }

    boolean seriesAdded = false;

    private boolean chartRefresh;
    private int rotationHistoryIndex = 0;

    int seriesAddedCount = 0;

    int oldAdded = 0;

    public void chartRefresh(boolean b){
            if (rotationHistoryIndex != rotationManager.getRotationsHistory().size()) {
                if (seriesAddedCount > 10) {
                    //chartIntegration.setAnalysisToChart(0);
                    seriesAddedCount = 0;
                }
                seriesAddedCount++;
            }else
                rotationHistoryIndex = rotationManager.getRotationsHistory().size();
    }

    public void setMouseTrapCarManager(RotationManager rotationManager) {
        this.rotationManager = rotationManager;
        this.rotationManager.setMainFrameController(this);
    }

    public RotationManager getMouseTrapCarManager() {
        return rotationManager;
    }

    public LineChart<String, Double> getLineChart() {
        return rotation_chart;
    }

    public XYChart.Series<String, Double> getRotationSeries() {
        return chartIntegration.getRotationSeries();
    }

    public SerialReadder getSerialReadder() {
        return serialReadder;
    }

    public Car getCar() throws IOException, ClassNotFoundException { return (Car) Save.read(new File(ApplicationFolder + "\\CarSettings.car")); }

    public void setCar(Car car) { this.car = car; }
}
