package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Created by kifkif on 20/03/2017.
 */

public class LoginController  {
    private Login login;

    @FXML private TextField serverField;
    @FXML private TextField portField;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;

    @FXML
    public void authenticateUser(ActionEvent actionEvent)
    {
        this.login.displayHomeScreen(serverField.getText(), portField.getText(), usernameField.getText(), passwordField.getText());
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
}