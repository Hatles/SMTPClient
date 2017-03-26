package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by kifkif on 26/03/2017.
 */
public abstract class BaseController {
    protected Stage stage;
    protected Scene scene;
    protected Parent parent;
    protected String title;

    public BaseController(String title, String view, int width, int height)  throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/"+view+".fxml"));

        try {
            loader.setController(this);
            parent = (Parent)loader.load();
            // set height and width here for this login scene
            scene = new Scene(parent, width, height);
            this.title = title;
        } catch (IOException ex) {
            System.out.println("Error displaying window");
            throw new RuntimeException(ex);
        }
    }

    protected abstract void init();

    // create a launcher method for this. Here I am going to take like below--
    public void launchScene(Stage stage) {
        stage.hide();
        this.stage = stage;
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setResizable(false);

        this.init();

        //Don't forget to add below code in every controller

        stage.show();
    }
}
