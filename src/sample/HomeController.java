package sample;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

/**
 * Created by kifkif on 20/03/2017.
 */

public class HomeController extends BaseController{

    @FXML private TableView<Message> table_messages;

    private final ObservableList<Message> data = FXCollections.observableArrayList();

    public HomeController() throws Exception {
        super("POP3S Client", "home", 600, 300);
    }

    @Override
    protected void init() {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                quit();
            }
        });

        Client client = Client.getInstance();

        int nbrMessages = client.stats();

        List<Message> messages = client.getMessages();
        data.addAll(messages);

        table_messages.setRowFactory( tv -> {
            TableRow<Message> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Message message = row.getItem();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    try {
                        alert.setHeaderText(message.getHeader("Subject"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        alert.setHeaderText("undifined");
                    }
                    alert.setTitle("Message");
                    alert.setContentText(message.getMessage());

                    alert.showAndWait();
                }
            });
            return row ;
        });

        table_messages.getColumns().get(0).setCellValueFactory(cell ->
        {
                try {
                    return new ReadOnlyObjectWrapper(cell.getValue().getHeader("Date"));
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ReadOnlyObjectWrapper("undefined");
                }
            }
        );

        table_messages.getColumns().get(1).setCellValueFactory(cell ->
        {
                try {
                    return new ReadOnlyObjectWrapper(cell.getValue().getHeader("From"));
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ReadOnlyObjectWrapper("undefined");
                }
            }
        );

        table_messages.getColumns().get(2).setCellValueFactory(cell ->
        {
                try {
                    return new ReadOnlyObjectWrapper(cell.getValue().getHeader("Subject"));
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ReadOnlyObjectWrapper("undefined");
                }
            }
        );

        table_messages.setItems(data);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Messages Stat");
        alert.setHeaderText(null);
        alert.setContentText("You have "+nbrMessages+" message(s).");

        alert.showAndWait();
    }

    private void quit() {
        Client.getInstance().quit();
    }
}