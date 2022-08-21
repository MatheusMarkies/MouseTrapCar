package com.matheusmarkies;

import com.matheusmarkies.manager.MouseTrapCarManager;
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
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlMain = new FXMLLoader(MouseTrapCarMain.class.getResource(
                "/com/matheusmarkies/mousetrapcar/MainFrame.fxml"));

        Parent root = fxmlMain.load();

        Scene scene = new Scene(root);

        File applicationFolder = new File(Save.ApplicationFolder);

        try {
            if (!applicationFolder.exists()) {

                applicationFolder.createNewFile();

            } else {
                File carFile = new File(Save.ApplicationFolder + "\\CarSettings.car");
                if (carFile.exists())
                    car = Save.openCarPresets();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        MainFrameController controller = fxmlMain.getController();

        if(car == null)
            car = new Car();

        controller.setCar(car);
        //controller.setMouseTrapCarManager(new MouseTrapCarManager());

        stage.setTitle("Mouse Trap Car");

        scene.getStylesheets().add("/com/matheusmarkies/mousetrapcar/MainFrameCSS.css");

        stage.setScene(scene);
        stage.setMaximized(false);
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
