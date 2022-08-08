package com.matheusmarkies;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class MouseTrapCarMain extends Application {
    static Scene scene;

    public static MainFrameManager bridgeManager = new MainFrameManager();

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlMain = new FXMLLoader(MouseTrapCarMain.class.getResource(
                "/com/matheusmarkies/mousetrapcar/MainFrame.fxml"));

        Parent root = fxmlMain.load();

        Scene scene = new Scene(root);

        MainFrameController controller = fxmlMain.getController();

        stage.setTitle("Mouse Trap Car");

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
