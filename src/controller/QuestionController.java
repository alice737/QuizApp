package controller;

import client.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Alicja on 2017-12-27.
 */
public class QuestionController implements Initializable {

    @FXML  private Label tresc1;
    @FXML  private RadioButton odp11;
    @FXML  private RadioButton odp21;
    @FXML  private RadioButton odp31;
    @FXML  private RadioButton odp41;
    @FXML  private Label numerNIU;
    private String iloscPytan;
    @FXML  private Label ilePytan;
    @FXML  private Label pozostalo;
    @FXML  private Button nastepne;
    @FXML  private Label podsumowanie;
    private int pytanie;
    private  int pozostaloPytan;
    private boolean zakonczenie;
    @FXML  private Button start;
    @FXML   private TextField baza;Client client;

    @Override

    public void initialize(URL location, ResourceBundle resources) {
        DataOutputStream out;
        out = client.OutputConnection();

        //out.writeUTF("JAVA_PROJEKT");
        nastepne.setDisable(true);

    }

    public QuestionController() {
        this.client = new Client();

    }

    public void onStart(){

    DataInputStream in;
    in = client.InputConnection();
    DataOutputStream out;
    out = client.OutputConnection();
    if(!baza.getText().equals("JAVA_PROJEKT")){
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText("Podana nazwa bazy jest nieprawidłowa!!");
        a.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Platform.exit();

            }
        });


    }
    numerNIU.setText(client.getNIU());
    try {

        out.writeUTF(client.getNIU());
        iloscPytan = in.readUTF();
        tresc1.setText(in.readUTF());
        odp11.setText(in.readUTF());
        odp21.setText(in.readUTF());
        odp31.setText(in.readUTF());
        odp41.setText(in.readUTF());
    } catch (IOException e) {
        e.printStackTrace();
    }
    ilePytan.setText(iloscPytan);
    pozostalo.setText(iloscPytan);
    pytanie = Integer.parseInt(iloscPytan);
    pozostaloPytan = pytanie - 1;
    start.setDisable(true);
    nastepne.setDisable(false);
}
    public void pokaz() {
        Integer x=pozostaloPytan;
        pozostalo.setText(x.toString());
        DataInputStream in;
        in = client.InputConnection();;

        if (pozostaloPytan > 0) {
            wynik();

            odp11.selectedProperty().set(false);
            odp21.selectedProperty().set(false);
            odp31.selectedProperty().set(false);
            odp41.selectedProperty().set(false);

            try {
                tresc1.setText(in.readUTF());
                odp11.setText(in.readUTF());
                odp21.setText(in.readUTF());
                odp31.setText(in.readUTF());
                odp41.setText(in.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
            pozostaloPytan = pozostaloPytan - 1;
            zakonczenie = false;
        } else {

                    try {
                if (!zakonczenie){
                    wynik();
                    System.out.println("koniec..... ");
                    nastepne.setText("ZAKONCZ");
                podsumowanie.setText("TWÓJ WYNIK  "+ in.readUTF() + " udzielone poprawnie odpowiedzi na "+iloscPytan +" pytania.");

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (zakonczenie) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                client.close();
                Platform.exit();
            }
            zakonczenie = true;

        }
    }

    public void wynik() {
        DataOutputStream out;
        out = client.OutputConnection();
        if (odp11.isSelected()) {
            try {
                out.writeUTF(odp11.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (odp21.isSelected()) {
            try {
                out.writeUTF(odp21.getText());
            } catch (IOException e) {
            }
        } else if (odp31.isSelected()) {
            try {
                out.writeUTF(odp31.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (odp41.isSelected()) {
            try {
                out.writeUTF(odp41.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
