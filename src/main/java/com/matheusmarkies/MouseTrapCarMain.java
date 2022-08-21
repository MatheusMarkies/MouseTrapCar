package com.matheusmarkies;

import com.matheusmarkies.manager.utilities.Save;
import com.matheusmarkies.objects.Car;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static javafx.application.Application.launch;

public class MouseTrapCarMain extends Application {
    static Scene scene;

    Car car = new Car();

    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {

        FXMLLoader fxmlMain = new FXMLLoader(MouseTrapCarMain.class.getResource(
                "/com/matheusmarkies/mousetrapcar/MainFrame.fxml"));

        Parent root = fxmlMain.load();

        Scene scene = new Scene(root);

        File applicationFolder = new File(Save.ApplicationFolder);

            if (!applicationFolder.exists()) {
                applicationFolder.mkdirs();
            } else {
                File carFile = new File(applicationFolder.getAbsolutePath() + "\\CarSettings.car");
                if (carFile.exists())
                    car = Save.openCarPresets();
                else {
                    carFile.createNewFile();
                    car = new Car();
                    Save.saveCarSettings(car);
                }
            }

        MainFrameController controller = fxmlMain.getController();

        controller.setCar(car);
        //controller.setMouseTrapCarManager(new MouseTrapCarManager());

        stage.setTitle("Mouse Trap Car");

        scene.getStylesheets().add("/com/matheusmarkies/mousetrapcar/MainFrameCSS.css");

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        MainFrameController FXML_Start = new MainFrameController();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static Scene getScene() {
        return scene;
    }
}
