package application;

import application.resources.controller.DBDelete;
import application.resources.controller.DBInserts;
import application.resources.controller.DBQueries;
import application.resources.controller.DBUpdates;
import application.resources.model.Employee;
import application.resources.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;



public class ControllerEmployee {

    @FXML
    TextField txtNombre, txtTitulo, txtEmail, txtTelefono, txtDireccion;

    @FXML
    Button btnCrear, btnActualizar, btnEliminar, btnListar, btnBuscar, btnMain;

    @FXML
    TableView<Employee> tblEmpleados;
    @FXML
    TableColumn id, nombre, email, telefono, direccion;

    private HashMap<Integer, Employee> empleadosIDHashMap;
    private HashMap<String, Integer> empleadosNombreIDHashMap;
    private int idEmpleado;
    private List<Employee> lista;
    private Employee employee;
    private Thread thread1;
    private DBQueries dbQueries;

    @FXML
    public void initialize(){
        thread1 = new Thread(this::init);
        thread1.start();
    }

    private void init() {
    	dbQueries = new DBQueries();
    	empleadosIDHashMap = new HashMap<>();
    	empleadosNombreIDHashMap = new HashMap<>();
    	obtenerDatosEmpleados();
    	listenerTable();

    }

    public void obtenerDatosEmpleados(){
        empleadosIDHashMap = dbQueries.getEmployees();

        for(Map.Entry<Integer, Employee> entry : empleadosIDHashMap.entrySet())
        	empleadosNombreIDHashMap.put(entry.getValue().getNombre(), entry.getKey());

        lista = new ArrayList<>(empleadosIDHashMap.values());
    }

    public void listenerTable() {
    	tblEmpleados.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
    		System.out.println("Se clickea");
    		employee = tblEmpleados.getSelectionModel().getSelectedItem();
    		setearDatos(employee);
    	});
    }

    public void guardarEmpleado() {
    	System.out.println("Guardar empleado");
    	DBInserts dbInserts = new DBInserts();
    	dbInserts.insertEmpleado(txtNombre.getText(), txtTitulo.getText(), txtEmail.getText(), txtTelefono.getText(), txtDireccion.getText());
    }

    public void actualizarEmpleado() {
    	System.out.println("Actualizar empleado");
    	DBUpdates dbUpdates = new DBUpdates();

    	if(employee != null) {
    		dbUpdates.updateEmpleado(employee.getId(), txtNombre.getText(), txtTitulo.getText(), txtEmail.getText(),
					txtTelefono.getText(), txtDireccion.getText());
    	}
    }

    public void borrarEmpleado() {
    	System.out.println("Eliminar Empleado");
    	DBDelete dbDelete = new DBDelete();
    	if(employee != null) {
    		dbDelete.deleteEmpleado(employee.getId());
    	}
    }

    public void buscarEmpleado() {
    	buscar();
    }

    private void buscar() {
    	if(!thread1.isAlive()) {
    		Dialog<String> dialog = new Dialog<>();
    		dialog.setTitle("Buscar Empleado");
    		dialog.setHeaderText("Ingrese el nombre del empleado a buscar");

    		Label lblNombre = new Label("Nombre:");
    		TextField txtNombre = new TextField();

    		GridPane grid = new GridPane();
    		grid.add(lblNombre, 1, 1);
    		grid.add(txtNombre, 1, 2);
    		dialog.getDialogPane().setContent(grid);

    		ButtonType buttonTypeOK = new ButtonType("Cargar Datos", ButtonBar.ButtonData.OK_DONE);
    		dialog.getDialogPane().getButtonTypes().add(buttonTypeOK);

    		dialog.setResultConverter(button ->{
    			if(button == buttonTypeOK) return txtNombre.getText();
    			return null;
    		});

    		Optional<String> resultado = dialog.showAndWait();
    		resultado.ifPresent(this::empleadoResultado);
    	}
    }

    private void empleadoResultado(String nombre) {
    	if(empleadosNombreIDHashMap.containsKey(nombre)) {
    		idEmpleado = empleadosNombreIDHashMap.get(nombre);
    		employee = empleadosIDHashMap.get(idEmpleado);
    		setearDatos(employee);
    	}else
    		alertaSinResultado(nombre);

    }

    private void setearDatos(Employee empleado) {
    	if(empleado != null) {
    	txtNombre.setText(empleado.getNombre());
		txtTitulo.setText(empleado.getTitulo());
		txtEmail.setText(empleado.getEmail());
		txtTelefono.setText(empleado.getTelefono());
		txtDireccion.setText(empleado.getDireccion());
    	}
    }

    private void alertaSinResultado(String nombre) {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Informacion");
    	alert.setHeaderText("No hay resultado para la busqueda");
    	alert.setContentText("El usuario: " + nombre + ", no esta registrado.");
    	alert.showAndWait();
    }

    public void menuPrincipal() throws IOException {
    	Stage primaryStage = (Stage) btnMain.getScene().getWindow();
    	Parent root = FXMLLoader.load(this.getClass().getResource("MainMenu.fxml"));
        primaryStage.setTitle("Mundo Ganadero");
        primaryStage.setScene(new Scene(root, 650, 490));
        primaryStage.show();
    }

    @FXML
    public void cargarEmpleados(){
        id.setCellValueFactory(new PropertyValueFactory("id"));
        nombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        email.setCellValueFactory(new PropertyValueFactory("email"));
        telefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        direccion.setCellValueFactory(new PropertyValueFactory("direccion"));

        ObservableList<Employee> data = FXCollections.observableArrayList(lista);

        tblEmpleados.setItems(data);
    }

}
