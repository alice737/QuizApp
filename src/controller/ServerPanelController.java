package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import kolokwium.Server;

import java.net.URL;
import java.util.ResourceBundle;


public class ServerPanelController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void start(ActionEvent actionEvent) {
        //String [] st= {"a"};
        Server.main();


    }

    public void stop(ActionEvent actionEvent) {

        Platform.exit();

    }
}
