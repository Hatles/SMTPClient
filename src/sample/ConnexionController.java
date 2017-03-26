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

public class ConnexionController extends BaseController{

    @FXML private TextField serverField;
    @FXML private TextField portField;
    @FXML private Button btn_connect;

    public ConnexionController() throws Exception {
        super("Connexion", "connexion", 300, 150);
    }

    public void connect(ActionEvent actionEvent)
    {
        Client client = Client.getInstance();

        if(client.connect(serverField.getText(), portField.getText()))
        {
            try {
                new LoginController().launchScene(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText(null);
            alert.setContentText("Error during connection to the server.");

            alert.showAndWait();
        }
    }

    @Override
    protected void init() {
        btn_connect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                connect(event);
            }
        });
    }
}