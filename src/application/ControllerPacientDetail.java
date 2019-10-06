package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import application.resources.controller.DBDelete;
import application.resources.controller.DBInserts;
import application.resources.controller.DBQueries;
import application.resources.controller.DBUpdates;
import application.resources.model.Patient;
import application.resources.model.RegistroMedico;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ControllerPacientDetail {

    // Components for Tab Details:
    @FXML
    TextField txtDetPaciente, txtDetDireccion, txtDetPelaje, txtDetPropietario, txtDetCiudad, txtDetEdad, txtDetRaza;
    @FXML
    ComboBox<String> comboDetClase, comboDetGenero;
    @FXML
    DatePicker dateDet;
    @FXML
    Button btnDetCrear, btnDetActualizar, btnDetEliminar, btnDetMenu;

    // Components for Tab Register
    @FXML
    TextField txtRegFecha, txtRegNombre, txtRegHora;
    @FXML
    DatePicker dateReg;
    @FXML
    ComboBox<String> comboReg;
    @FXML
    TextArea txtAreaReg;
    @FXML
    Button btnRegGuardar, btnRegActualizar, btnRegLimpiar, btnRegCargar, btnRegAbrir, btnRegEliminar, btnRegMain;
    @FXML
    TableView<RegistroMedico> tblReg;
    
    // Lista para cargar los pacientes
    List<Patient> pacientes;
    
    List<String> listaClases, listaGeneros, listaTiposNotas;
    
    List<String> clientes;
    
    
    // Lista para cargar los clientes Cedula / Nombre
    HashMap<String, String> clientesCN;
    // Lista inversa de carga de los clientes Nombre / Cedula
    HashMap<String, String> clientesNomCli;
    // Instancia para el autocompletado
    private AutoCompletionBinding<String> autoTextField;
    // Thread para la carga de datos
    Thread thread;
    // String para almacenar la cedula del propietario elegido
    private String cedulaProp;
    // int para almacenar el id del paciente a actualizar
    int paciente_id;
    
    private DBQueries dbQueries;
    
    
    public void initialize(){
    	thread = new Thread(this::init);
    	thread.start();
    }
    
    public void init() {
    	dbQueries = new DBQueries();
    	pacientes = new ArrayList<>();
    	clientesCN = new HashMap<String, String>();
    	clientesNomCli = new HashMap<String, String>();
    	inicializarRegistro();
    	
    	cargarDatos();
    	
    	dateDet.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				edadActual();
			}
		});
    	
    	txtDetPropietario.setOnKeyPressed(event -> {
    		if(event.getCode().equals(KeyCode.ENTER)) {
    			cedulaProp = clientesCN.get(txtDetPropietario.getText());
    		}
    	});
    }
    
    public void cargarDatos() {
    	clientesCN = dbQueries.getClientsCN();
    	for(Map.Entry<String, String> entry : clientesCN.entrySet())
    		clientesNomCli.put(entry.getValue(), entry.getKey());
    	listaClases = new ArrayList<>();
    	listaGeneros = new ArrayList<>();
    	listaTiposNotas = new ArrayList<>();
    	clientes = new ArrayList<>(clientesCN.keySet());
    	autoComPropietario();
    	cargarCombos();
    	autoTextField = TextFields.bindAutoCompletion(txtDetPropietario, "");
    	cedulaProp = "";
    }
    
    private String cargarFecha(String fechaOriginal) {
    	DateFormat originalFormat = new SimpleDateFormat("yyy-MM-dd");
		DateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		try {
			date = originalFormat.parse(fechaOriginal);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String formattedDate = targetFormat.format(date);
		return formattedDate;
    }
    
    public void edadActual() {
    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    	LocalDateTime now = LocalDateTime.now();
    	
    	String fechaCalendario = dateDet.getValue().toString();
    	fechaCalendario = fechaCalendario.replaceAll("-", "/");
    	
    	Date d1 = null;
    	Date d2 = null;
    	
    	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    	
    	try {
    		d1 = format.parse(fechaCalendario);
    		d2 = format.parse(dtf.format(now).toString());
    		
    		DateTime dt1 = new DateTime(d1);
    		DateTime dt2 = new DateTime(d2);
    		
    		Period p = new Period(dt1, dt2, PeriodType.yearMonthDayTime());

    		txtDetEdad.setText(String.valueOf(p.getYears() + "." + p.getMonths() + "." + p.getDays()));
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
    public void detCrear() {
    	if(!camposVaciosDetalle() && !cedulaProp.equals("")) {
    		DBInserts dbInserts = new DBInserts();
    		
    		dbInserts.insertPatient(txtDetPaciente.getText(), comboDetClase.getValue(), comboDetGenero.getValue(),
    				txtDetCiudad.getText(), cargarFecha(dateDet.getValue().toString()), txtDetRaza.getText(), txtDetPelaje.getText(),
    				cedulaProp, txtDetCiudad.getText(), txtDetEdad.getText());
    	}else if(cedulaProp.equals(""))
    		alertaCedula();
    }
    
    
    
    public boolean camposVaciosDetalle() {
    	if(txtDetPaciente == null || txtDetPaciente.getText().equals("")) {
    		alertaVacio("Paciente");
    		return true;
    	}else if(txtDetPropietario == null || txtDetPropietario.getText().equals("")) {
    		alertaVacio("Propietario");
    		return true;
    	}else if(txtDetDireccion == null || txtDetDireccion.getText().equals("")) {
    		alertaVacio("Direccion");
    		return true;
    	}else if(txtDetEdad == null || txtDetEdad.getText().equals("")) {
    	    alertaVacio("Edad");
    	    return true;
    	}else if(txtDetPelaje == null || txtDetPelaje.getText().equals("")) {
    		alertaVacio("Pelaje");
    		return true;
    	}else if(txtDetCiudad == null || txtDetCiudad.getText().equals("")) {
    		alertaVacio("Ciudad");
    		return true;
    	}else
    		return false;
    }
    
    public void detActualizar() {
    	if(!camposVaciosDetalle() && !cedulaProp.equals("")) {
    		DBUpdates dbUpdates = new DBUpdates();
    		dbUpdates.updatePatient(paciente_id, txtDetPaciente.getText(), comboDetClase.getValue(), comboDetGenero.getValue(),
    				txtDetCiudad.getText(), dateDet.getValue().toString(), txtDetRaza.getText(), txtDetPelaje.getText(), cedulaProp,
    				txtDetCiudad.getText(), txtDetEdad.getText());
    	}
    }
    
    public void detEliminar() {
    	DBDelete dbDelete = new DBDelete();
    	dbDelete.deletePatient(paciente_id);
    }
    
    public void menuPrincipal() throws Exception {
    	Stage primaryStage = (Stage) btnDetMenu.getScene().getWindow();
    	Parent root = FXMLLoader.load(this.getClass().getResource("MainMenu.fxml"));
        primaryStage.setTitle("Mundo Ganadero");
        primaryStage.setScene(new Scene(root, 650, 490));
        primaryStage.show();
    }
    
    private void alertaVacio(String fuenteError) {
    	Alert alerta = new Alert(AlertType.WARNING);
    	alerta.setTitle("Advertencia");
    	alerta.setHeaderText("Un campo se encuentra vacio");
    	alerta.setContentText("La caja de texto: " + fuenteError + ", se encuentra vacia, por favor revisela.");
    	alerta.showAndWait();
    }
    
    private void alertaCedula() {
    	Alert alerta = new Alert(AlertType.WARNING);
    	alerta.setTitle("Advertencia");
    	alerta.setHeaderText("No se ha podido obtener los datos del propietario");
    	alerta.setContentText("Asegurese de haber seleccionado un cliente existente o de haber dado enter al momento de seleccionarlo");
    	alerta.showAndWait();
    }
    
    //Autocompletado de los propietarios
    private void autoComPropietario() {
    	autoTextField = TextFields.bindAutoCompletion(txtDetPropietario, clientes);
    }
    
    private void cargarCombos() {
    	listaClases.add("Aves");
        listaClases.add("Bovinos");
        listaClases.add("Equinos");
        listaClases.add("Porcinos");
        listaClases.add("Felinos");
        listaClases.add("Caninos");
        listaClases.add("Ovinos");
        comboDetClase.setItems(FXCollections.observableArrayList(listaClases));
        
        listaGeneros.add("Masculino");
        listaGeneros.add("Femenino");
        comboDetGenero.setItems(FXCollections.observableArrayList(listaGeneros));
        
        listaTiposNotas.add("Nota");
        listaTiposNotas.add("Observacion");
        listaTiposNotas.add("Medicacion");
        listaTiposNotas.add("Alergias");
        listaTiposNotas.add("Comportamiento");
        listaTiposNotas.add("Procedimiento Medico");
        comboReg.setItems(FXCollections.observableArrayList(listaTiposNotas));
    }
    
    public void editarPaciente(Patient paciente) {
    	try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		paciente_id = paciente.getId();
        	txtDetPaciente.setText(paciente.getNombre());
        	txtDetDireccion.setText(paciente.getDireccion());
        	txtDetPelaje.setText(paciente.getPelaje());
        	txtDetPropietario.setText(clientesNomCli.get(paciente.getProp_ci()));
        	txtDetCiudad.setText(paciente.getCiudad());
        	txtDetEdad.setText(paciente.getEdad());
        	txtDetRaza.setText(paciente.getRaza());
        	comboDetClase.getSelectionModel().select(paciente.getClase());
        	comboDetGenero.getSelectionModel().select(paciente.getGenero());
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        	LocalDate localDate = LocalDate.parse(paciente.getFecha_nac(), formatter);
        	dateDet.setValue(localDate);
        	
        	cedulaProp = clientesCN.get(txtDetPropietario.getText());
    	
    }
    
    
    /**
     * Trabajo solo con los registros medicos
     */
    
    // Lista para guardar los registros medicos de la Base de Datos
    private List<RegistroMedico> registrosMedicos;
    // Columnas de la tabla registros
    @FXML
    TableColumn colID, colPaciente, colFecha, colTipoNota;
    // Lista observable para la tabla
    private ObservableList<RegistroMedico> registrosObservableList;
    // Registro
    private RegistroMedico registroMedico;
    private HashMap<String, Integer> pacientesHashMap;
    private HashMap<Integer, String> pacientesIDNomHashMap;
    private int regPacienteId = 0;
    private AutoCompletionBinding<String> txtPacienteAuto;
    private List<String> pacientesList;
    
    private void inicializarRegistro() {
    	 registrosMedicos = new ArrayList<RegistroMedico>();
    	 pacientesHashMap = dbQueries.getPatientsNomID();
    	 pacientesIDNomHashMap = new HashMap<Integer, String>();
    	 
    	 for(Map.Entry<String, Integer> entry : pacientesHashMap.entrySet())
    		 pacientesIDNomHashMap.put(entry.getValue(), entry.getKey());
    	 
    	 txtPacienteAuto = TextFields.bindAutoCompletion(txtRegNombre, "");
    	 
    	 cargarAutoCompletado();
    	 setLocalTime();
    }
    
    private void setLocalTime() {
    	 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
         LocalDateTime now = LocalDateTime.now();
         txtRegFecha.setText(dtf.format(now));
         
         int hora = now.getHour();
         int minuto = now.getMinute();
         String time = String.valueOf(hora + ":" + minuto);
         txtRegHora.setText(time);
    }
    
    private void cargarAutoCompletado() {
    	pacientesList = new ArrayList<>(pacientesHashMap.keySet());
    	txtPacienteAuto = TextFields.bindAutoCompletion(txtRegNombre, pacientesList);
    	
    }
    
    public void actualizarRegistro() {
    	DBUpdates dbUpdates = new DBUpdates();
    	
    	if(registroMedico != null) {
    		regPacienteId = pacientesHashMap.get(txtRegNombre.getText());
    		dbUpdates.updateRegistro(registroMedico.getId(), txtRegFecha.getText(), txtRegHora.getText(),
    			cargarFecha(dateReg.getValue().toString()), comboReg.getValue(), txtAreaReg.getText(), regPacienteId);
    	}else
    		alertaGeneral("EXISTE UN/OS CAMPO/S VACIO/S, POR FAVOR REVISELOS");
    }
    
    public void grdRegistro() {
    	DBInserts dbInserts = new DBInserts();
    	regPacienteId = pacientesHashMap.get(txtRegNombre.getText());
    	String value = dbInserts.insertRegistroMedico(txtRegFecha.getText(), txtRegHora.getText(),
    			cargarFecha(dateReg.getValue().toString()), comboReg.getValue(), txtAreaReg.getText(), regPacienteId);	
    	if(value.equals("")) 
    		alertaIngresoCorrecto();
    	else 
    		alertaGeneral(value);
    }
    
    public void limpiarRegistro() {
    	System.out.println("Limpiar registro");
    	txtRegNombre.setText("");
    	txtAreaReg.setText("");
    }
    
    public void cargarRegistros() {
    	System.out.println("Cargar registros");
    	registrosMedicos = dbQueries.getRegistrosMedicos();
    	mostrarPacientes();
    	listenerTablaRegistros();
    }
    
    public void abrirRegistro() {
    	System.out.println("Abrir Registro");
    	if(registroMedico != null) {
    		txtRegFecha.setText(registroMedico.getFecha());
    		txtRegHora.setText(registroMedico.getHora());
    		txtRegNombre.setText(pacientesIDNomHashMap.get(registroMedico.getPaciente_id()));
    		
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        	LocalDate localDate = LocalDate.parse(registroMedico.getNext_fecha(), formatter);
        	dateReg.setValue(localDate);
    		
    		comboReg.getSelectionModel().select(registroMedico.getTipo());
    		txtAreaReg.setText(registroMedico.getDescripcion());
    	}else
    		alertaRegistro();
    }
    
    public void eliminarRegistro() {
    	System.out.println("Eliminar registro");
    	
    	if(registroMedico != null) {
    		DBDelete dbDelete = new DBDelete();
        	dbDelete.deleteRegistroMedico(registroMedico.getId());
    	}
    	
    	
    }
    
    private void mostrarPacientes() {
    	colID.setCellValueFactory(new PropertyValueFactory("id"));
    	colPaciente.setCellValueFactory(new PropertyValueFactory("paciente_id"));
    	colFecha.setCellValueFactory(new PropertyValueFactory("next_fecha"));
    	colTipoNota.setCellValueFactory(new PropertyValueFactory("tipo"));
    	
    	registrosObservableList = FXCollections.observableArrayList(registrosMedicos);
    	
    	tblReg.setItems(registrosObservableList);
    	
    }
    
    private void listenerTablaRegistros() {
    	tblReg.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
    		registroMedico = tblReg.getSelectionModel().getSelectedItem();
    	});
    }

    private void alertaRegistro() {
    	Alert alert = new Alert(AlertType.ERROR);
    	alert.setTitle("Error de apertura");
    	alert.setHeaderText("No se ha podido abrir el registro");
    	alert.setContentText("Al parecer no se ha seleccionado ningun registro medico, por lo que no se puede abrir");
    	alert.showAndWait();
    }
   
    
    private void alertaGeneral(String fuente) {
    	Alert alert = new Alert(AlertType.ERROR);
    	alert.setTitle("ERROR");
    	alert.setHeaderText("A ocurrido un error al momento del proceso de ejecucion, \n"
    			+ fuente);
    }
    
    private void alertaIngresoCorrecto() {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Ingreso correcto");
    	alert.setHeaderText("Se ha ingresado el nuevo registro \n de manera satisfactoria");
    	alert.showAndWait();
    }
}
