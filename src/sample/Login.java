package sample;

/**
 * Created by kifkif on 20/03/2017.
 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {

    private Stage stage;
    private Scene scene;
    private Parent parent;
    private LoginController controller;

    @FXML
    private Button gotoHomeButton;

    public Login()  throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/login.fxml"));

        try {
            parent = (Parent)loader.load();
            controller = loader.getController();
            controller.setLogin(this);
            // set height and width here for this login scene
            scene = new Scene(parent, 300, 300);
        } catch (IOException ex) {
            System.out.println("Error displaying login window");
            throw new RuntimeException(ex);
        }
    }

    // create a launcher method for this. Here I am going to take like below--
    public void launchLoginScene(Stage stage) {
        this.stage = stage;
        stage.setTitle("POP3 Client Connect");
        stage.setScene(scene);
        stage.setResizable(true);

        stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                setCurrentWidthToStage(number2);
            }
        });

        stage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                setCurrentHeightToStage(number2);
            }
        });

        //Don't forget to add below code in every controller
        stage.hide();
        stage.show();

    }

    private void setCurrentWidthToStage(Number number2) {
        stage.setWidth((double) number2);
    }

    private void setCurrentHeightToStage(Number number2) {
        stage.setHeight((double) number2);
    }

    public void displayHomeScreen(String server, String port, String username, String password) {

        Client client = Client.getInstance();

        if(client.connect(server, port, username, password))
            new Home().displayHomeScreen(stage);
        //else
            
    }
}