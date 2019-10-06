package application;

import application.resources.controller.DBConnection;
import application.resources.controller.DBQueries;
import application.resources.model.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ControllerCalendar {
    @FXML
    Label lblDate;

    @FXML
    Button btnCargar, btnCita, btnEliminar, btnMain;

    @FXML
    TableView<String> tblCalendario;

    @FXML
    public void initialize(){
        getCurrentDate();
    }



    public void getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        lblDate.setText(dtf.format(now));
    }



}
