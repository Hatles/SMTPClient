package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by kifkif on 20/03/2017.
 */

public class Home {

    private Parent parent;
    private Stage stage;
    private Scene scene;
    private HomeController controller;

    public Home(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/home.fxml"));

        try {
            parent = loader.load();
            controller = loader.getController();
            controller.setHome(this);
            // set height and width here for this home scene
            scene = new Scene(parent, 300, 300);
        } catch (IOException e) {
            // manage the exception
        }
    }

    public void displayHomeScreen(Stage stage){
        this.stage = stage;
        stage.setTitle("POP3 Client");
        stage.setScene(scene);

        // Must write
        stage.hide();
        stage.show();
    }
}