package com.matheusmarkies.popup;

import com.matheusmarkies.MainFrameController;
import com.matheusmarkies.manager.utilities.Save;
import com.matheusmarkies.objects.Car;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CarSettingsController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button save_button;

    @FXML
    private TextField number_axle_tooth;

    @FXML
    private TextField number_encoder_tooth;

    @FXML
    private TextField wheel_diameter_inputfield;

    private MainFrameController mainFrameController;

    @FXML
    void OnClickInSaveButton(ActionEvent event) {
        try{
            double wheelDiameter = Double.parseDouble(wheel_diameter_inputfield.getText());

            int encoderTooth = Integer.parseInt(number_encoder_tooth.getText());
            int axleTooth = Integer.parseInt(number_axle_tooth.getText());

            Car car = new Car(wheelDiameter, encoderTooth, axleTooth, 500);

            Save.saveCarSettings(car);
            System.out.println("Salvando...");
        }catch (Exception exception){
            System.out.println("Nao foi possivel salvar");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
           wheel_diameter_inputfield.setText(Save.openCarPresets().getWheelDiameter() + "");
            number_axle_tooth.setText(Save.openCarPresets().getAxleTooth() + "");
            number_encoder_tooth.setText(Save.openCarPresets().getEncoderTooth() + "");
        }catch (Exception exception){System.err.println(exception);}
    }

    public void setMainFrameController(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }
}
