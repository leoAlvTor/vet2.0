package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ControllerAppointments {

    @FXML
    TextField txtCI, txtIDPaciente;

    @FXML
    ComboBox comboEmpleados, comboTipoCita;

    @FXML
    Button btnCargar , btnAbrirPacientes, btnGenerarCita, btnGenerarFactura, btnMain, btnLimpiar, btnCancelar;

    @FXML
    DatePicker dateCita;

    @FXML
    RadioButton radioAlta, radioMedia, radioBaja;

    @FXML
    TextArea txtAreaObs;

    public void initialize(){

    }

}
