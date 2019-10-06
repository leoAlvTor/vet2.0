package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import application.resources.controller.DBQueries;
import application.resources.model.Patient;

public class ControllerPacientMain {

    @FXML
    TextField txtNombre, txtBuscarNombre;

    @FXML
    ComboBox<String> comboClasesAnim;

    @FXML
    TableView<Patient> tblPacientes;

    @FXML
    Button btnNuevaFicha, btnAbrirFicha, btnMain;
    
    @FXML
    TableColumn colID, colNombre, colTipo;
    
    private ObservableList<Patient> pacientes;
    private HashMap<Integer, Patient> pacientesHashMap;
    private List<Patient> pacienteList;
    private List<String> nombresPacientes;
    private DBQueries dbQueries;
    private Thread thread;
    private HashMap<String, Integer> nombreIDPacienteMap;
    private int valueID;
    private Patient paciente;
    
    

    @FXML
    public void initialize(){
    	thread = new Thread(this::init);
    	thread.start();
        //init();
    }
    
    private void init() {
    	dbQueries = new DBQueries();
    	cargarPacientes();
    }
    
    private void mostrarPacientes() {
    	colID.setCellValueFactory(new PropertyValueFactory("id"));
    	colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
    	colTipo.setCellValueFactory(new PropertyValueFactory("clase"));
    	
    	tblPacientes.setItems(pacientes);
    }
    

    private void cargarPacientes() {
    	inicializarVaribles();
    	
    	for(Patient p: pacienteList) {
    		nombreIDPacienteMap.put(p.getNombre(), p.getId());
    		nombresPacientes.add(p.getNombre());
    	}
    	eventoTxtNombre();
    	AutoCompletionBinding<String> autoCompleteNombre = TextFields.bindAutoCompletion(txtBuscarNombre, nombresPacientes);
    	mostrarPacientes();
    	eventoTabla();
    }

    private void inicializarVaribles() {
    	pacientesHashMap = dbQueries.getPatients();
    	pacienteList = new ArrayList<>(pacientesHashMap.values());
    	pacientes = FXCollections.observableArrayList(pacienteList);
    	nombreIDPacienteMap = new HashMap<String, Integer>();
    	nombresPacientes = new ArrayList<String>();
    	paciente = new Patient();
    }
    
    private void eventoTxtNombre() {
    	txtBuscarNombre.setOnKeyPressed(event ->{
    		if(event.getCode().equals(KeyCode.ENTER)) 
    			buscarPorNombre(txtBuscarNombre.getText());
    	});
    }
    
    private void buscarPorNombre(String nombre) {
    	valueID = nombreIDPacienteMap.get(nombre);
    	paciente = pacientesHashMap.get(valueID);
    }
    
    private void eventoTabla() {
    	tblPacientes.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
    	{
    		paciente = tblPacientes.getSelectionModel().getSelectedItem();
    	});
    }


    @FXML
    public void nuevaFicha() throws IOException{
    	FXMLLoader loader = new FXMLLoader(this.getClass().getResource("PacientDetail.fxml"));
    	Parent root = loader.load();
    	ControllerPacientDetail patientDetail = loader.getController();
    	patientDetail.txtDetPaciente.setText(txtNombre.getText());
    	
    	Stage stage = (Stage) btnMain.getScene().getWindow();
    	stage.setTitle("Modulo Pacientes");
    	stage.setScene(new Scene(root, 691, 669));
    	stage.centerOnScreen();
    	stage.setResizable(false);
    	stage.show();
    }

    @FXML
    public void abrirFicha() throws IOException{

    	FXMLLoader loader = new FXMLLoader(this.getClass().getResource("PacientDetail.fxml"));
    	Parent root = loader.load();
    	ControllerPacientDetail patientDetail = loader.getController();
    	patientDetail.editarPaciente(paciente);
    	Stage stage = (Stage) btnMain.getScene().getWindow();

    	stage.setTitle("Modulo Pacientes");
    	stage.setScene(new Scene(root, 691, 669));
    	stage.sizeToScene();
    	stage.centerOnScreen();
    	stage.setResizable(false);
    	stage.show();
    }

    @FXML
    public void regresarMain() throws Exception{
    	Stage primaryStage = (Stage) btnMain.getScene().getWindow();
    	Parent root = FXMLLoader.load(this.getClass().getResource("MainMenu.fxml"));
        primaryStage.setTitle("Mundo Ganadero");
        primaryStage.setScene(new Scene(root, 650, 490));
        primaryStage.show();
    }

   
}
