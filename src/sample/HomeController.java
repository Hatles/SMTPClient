package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/**
 * Created by kifkif on 20/03/2017.
 */

public class HomeController extends BaseController{

    public HomeController() throws Exception {
        super("POP3S Client", "home", 300, 300);
    }

    @Override
    protected void init() {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                quit();
            }
        });
    }

    private void quit() {
        Client.getInstance().quit();
    }
}