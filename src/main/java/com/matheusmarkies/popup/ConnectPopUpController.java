package com.matheusmarkies.popup;

import com.matheusmarkies.manager.RotationManager;
import com.matheusmarkies.serialport.SerialManager;
import com.matheusmarkies.serialport.SerialReadder;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ConnectPopUpController implements Initializable {

    private RotationManager rotationManager;

    @FXML
    private ChoiceBox<String> choisebox_serialport;

    @FXML
    private Button connect_button;

    private SerialReadder serialReadder;

    public ConnectPopUpController() {
    }

    public void setSerialReadder(SerialReadder serialReadder) {
        this.serialReadder = serialReadder;
    }

    @FXML
    void onClickInConnectButton(ActionEvent event) {
        try {
            serialReadder = new SerialReadder(rotationManager, choisebox_serialport.getValue());
            if(serialReadder.connect()) {
                System.out.println("Connect!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> list = SerialManager.getSerialPortNames();
        choisebox_serialport.setItems(FXCollections.observableArrayList(list));
    }

    public void setMouseTrapCarManager(RotationManager rotationManager) {
        this.rotationManager = rotationManager;
    }
}
