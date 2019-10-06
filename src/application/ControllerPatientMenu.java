package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControllerPatientMenu {

    @FXML
    Button btnCalendario, btnCitas, btnPacientes;
    
    private FXMLLoader fxml;
    
    public void initialize(){
        fxml = new FXMLLoader();
    }

    public void abrirCalendario() throws IOException{
        System.out.println("calendario");
        getStage(btnCalendario, "Calendario Clinico", "ClinicCalendar.fxml", 600, 400);
    }
    
    public void abrirCitas() throws IOException{
        System.out.println("citas");
        getStage(btnCitas, "Citas", "Appointments.fxml", 700, 636);
    }
    
    public void abrirPacientes() throws IOException{
        System.out.println("pacientes");
        getStage(btnPacientes, "Pacientes", "PatientMain.fxml", 713, 574);
    }
    
    private void getStage( Button btn, String tittle, String file_name, double width, double height) throws java.io.IOException {
    	Stage stage = (Stage) btn.getScene().getWindow();
        stage.setTitle(tittle);
        File file = new File(file_name);
        Parent root = FXMLLoader.load(this.getClass().getResource(file.getPath()));
        stage.setScene(new Scene(root, width, height));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

}
