package application;

import application.resources.controller.DBConnection;
import application.resources.controller.DBQueries;
import application.resources.model.Employee;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControllerCalendar {
    @FXML
    Label lblDate;

    @FXML
    Button btnCargar, btnCita, btnEliminar, btnMain;

    @FXML
    TableView<String> tblCalendario;

    @FXML
    public void initialize(){
        System.out.println("------------------");
        getCurrentDate();
    }



    public void getCurrentDate(){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        lblDate.setText(dtf.format(now));
    }

    public void menu() throws IOException{
        System.out.println("------------");
        Stage primaryStage = (Stage) btnMain.getScene().getWindow();
        Parent root = FXMLLoader.load(this.getClass().getResource("MainMenu.fxml"));
        primaryStage.setTitle("Mundo Ganadero");
        primaryStage.setScene(new Scene(root, 650, 490));
        primaryStage.show();


    }


}
