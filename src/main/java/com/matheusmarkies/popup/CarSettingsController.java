package com.matheusmarkies.popup;

import com.matheusmarkies.MainFrameController;
import com.matheusmarkies.manager.utilities.Save;
import com.matheusmarkies.objects.Car;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static com.matheusmarkies.manager.utilities.Save.ApplicationFolder;

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

            Save.write(car, ApplicationFolder + "\\CarSettings.car");
            System.out.println("Salvando...");
        }catch (Exception exception){
            System.out.println("Nao foi possivel salvar");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Car car = (Car)Save.read(new File(ApplicationFolder + "\\CarSettings.car"));
           wheel_diameter_inputfield.setText(car.getWheelDiameter() + "");
            number_axle_tooth.setText(car.getAxleTooth() + "");
            number_encoder_tooth.setText(car.getEncoderTooth() + "");
        }catch (Exception exception){System.err.println(exception);}
    }

    public void setMainFrameController(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }
}
