package com.matheusmarkies;

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
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainFrameController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem connect_menu_button;

    @FXML
    private LineChart<?, ?> line_chart;

    @FXML
    private MenuBar menu_bar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    void openConnectPopUp(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/com/matheusmarkies/mousetrapcar/ConnectPopUp.fxml"));
            Parent root = fxmlLoader.load();

            ConnectPopUpController connectPopUpController = (ConnectPopUpController)fxmlLoader.getController();

            connectPopUpController.setSerialReadder(serialReadder);

            Stage stage = new Stage();
            stage.setTitle("Conectar");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException ignored) {
            System.err.println(ignored);
        }
    }

}
