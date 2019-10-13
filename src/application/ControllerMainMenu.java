package application;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControllerMainMenu {
    
    
    
    @FXML
    Button btnPacientes, btnVentas, btnEmpleados, btnClientes;

    @FXML
    public void openPacientes() throws Exception{
        getStage(btnPacientes, "Pacientes", "PatientMenu.fxml", 713,574);
    }

    @FXML
    public void openCalendario() throws Exception{
        getStage(btnPacientes, "Calendario Clinico", "ClinicCalendar.fxml", 600,400);
    }

    @FXML
    public void openCitas() throws Exception{
        getStage(btnPacientes, "Citas", "Appointments.fxml", 700,636);
    }

    // FALTA <------------------->
    @FXML
    public void openVentas() throws Exception{
        getStage(btnPacientes, "Ventas", "InvoiceMenu.fxml", 600,467);
    }

    @FXML
    public void openEmployees() throws Exception{
        getStage(btnPacientes, "Empleados", "Employee.fxml", 658,645);
    }

    @FXML
    public void openClientes() throws Exception{
        getStage(btnPacientes, "Clientes", "Customer.fxml", 696,707);
    }

    // FALTA <------------------->
    @FXML
    public void openInventario() throws Exception{
        getStage(btnPacientes, "Inventario", "Inventory.fxml", 300,300);
    }

    // FALTA <------------------->
    @FXML
    public void openMetricas() throws Exception{
        getStage(btnPacientes, "Metricas", "Employee.fxml", 300,300);
    }

    @FXML
    private void getStage(Button btn, String tittle, String file_name, double width, double height) throws java.io.IOException {
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.setTitle(tittle);
        File file = new File(file_name);
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(file.getPath()));
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root, width, height));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }
}
