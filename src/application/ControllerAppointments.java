package application;

import application.resources.controller.DBConnection;
import application.resources.controller.DBInserts;
import application.resources.controller.DBQueries;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javax.swing.ButtonGroup;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

public class ControllerAppointments {

    @FXML
    TextField txtCI, txtIDPaciente;

    @FXML
    ComboBox comboEmpleados, comboTipoCita;

    @FXML
    Button btnCargar, btnAbrirPacientes, btnGenerarCita, btnGenerarFactura, btnMain, btnLimpiar, btnCancelar;

    @FXML
    DatePicker dateCita;

    @FXML
    RadioButton radioAlta, radioMedia, radioBaja;

    @FXML
    TextArea txtAreaObs;

    private AutoCompletionBinding<String> autocompletePaciente;

    public void initialize() {
        System.out.println("Leo");
        List<String> lista = new ArrayList<>();
        lista.add("leo");
        lista.add("alvarado");
        ObservableList<String> strings = FXCollections.observableArrayList(lista);
        comboEmpleados.setItems(strings);
        List<String> listatipo = new ArrayList<>();
        listatipo.add("Vacunacion");
        listatipo.add("Castracion");
        listatipo.add("Inseminación");
        listatipo.add("Ecografia");
        ObservableList<String> stringstipo = FXCollections.observableArrayList(listatipo);
        comboTipoCita.setItems(stringstipo);
        autoTextoPaciente();
    }

    private void autoTextoPaciente(){
        System.out.println("Leonardo");
        DBQueries dBQueries = new DBQueries();
        HashMap<String, Integer> mapaClientesNombreID = dBQueries.getPatientsNomID();
        List<String> listaPacientesNombre = new ArrayList<>(mapaClientesNombreID.keySet());


        txtIDPaciente.setOnKeyPressed(event ->{
            if(event.getCode().equals(KeyCode.ENTER))
                System.out.println(mapaClientesNombreID.get(txtIDPaciente.getText()));
        });
        autocompletePaciente = TextFields.bindAutoCompletion(txtIDPaciente, listaPacientesNombre);

    }



    public void generarCita() {
        System.out.println("--------------");
        DBInserts dbInserts = new DBInserts();
        dbInserts.connect();
        String empleado = String.valueOf(comboEmpleados.getSelectionModel().getSelectedItem());
        String tipo_cita = String.valueOf(comboTipoCita.getSelectionModel().getSelectedItem());
        System.out.println("empleado"+ empleado);
        System.out.println("cita"+ tipo_cita);
        int valorradio=0;
        if (radioAlta.isSelected() == true) {
            System.out.print("Seleccionó opción 1");
            valorradio=1;
        }else if(radioMedia.isSelected()==true){
            valorradio=2;


        }else{
            valorradio=3;
        }

        dbInserts.insertAppointment(txtCI.getText(), empleado, tipo_cita, java.sql.Date.valueOf(dateCita.getValue()), valorradio, txtAreaObs.getText());

    }

    public void menu() throws IOException {
        Stage primaryStage = (Stage) btnMain.getScene().getWindow();
        Parent root = FXMLLoader.load(this.getClass().getResource("MainMenu.fxml"));
        primaryStage.setTitle("Mundo Ganadero");
        primaryStage.setScene(new Scene(root, 650, 490));
        primaryStage.show();

    }

    public void limpiar() {
        txtAreaObs.setText("");
        txtCI.setText("");
        txtIDPaciente.setText("");

    }

}
