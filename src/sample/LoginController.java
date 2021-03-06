package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Created by kifkif on 20/03/2017.
 */

public class LoginController extends BaseController
{
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button btn_login;


    public LoginController() throws Exception {
        super("Login", "login", 300, 150);
    }

    @FXML
    public void authenticateUser(ActionEvent actionEvent)
    {
        Client client = Client.getInstance();

        if(client.login(usernameField.getText(), passwordField.getText())) {
            try {
                new HomeController().launchScene(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Password or username invalid. "+client.getTry()+" login tries remaining.");

            alert.showAndWait();
        }
    }

    @Override
    protected void init() {
        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                authenticateUser(event);
            }
        });
    }
}