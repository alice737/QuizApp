package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.SQLException;

public class ClientStart extends Application {

    public static String screen2ID = "screen2";
    public static String screen2File = "/view/question.fxml";


    public static void main(String[] args) throws SQLException {

        launch(args);


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {



            Parent root = FXMLLoader.load(getClass().getResource(screen2File));


            primaryStage.setScene(new Scene(root, 800, 569));
            primaryStage.show();

            primaryStage.setOnCloseRequest((WindowEvent we) -> {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setTitle("Ostrzezenie");
                a.setHeaderText("Czy na pewno chcesz zakonczyc program?");
                a.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        Platform.exit();

                    } else {
                        we.consume();
                    }
                });
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}