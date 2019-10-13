package application;

import application.resources.controller.DBDelete;
import application.resources.controller.DBInserts;
import application.resources.controller.DBQueries;
import application.resources.controller.DBUpdates;
import application.resources.model.Customers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class ControllerCustomer {

	@FXML
	TextField txtCedula, txtNombre, txtTelefono, txtDireccion, txtEmail;

	@FXML
	Button btnCrear, btnActualizar, btnEliminar, btnListar, btnBuscar, btnMain;

	@FXML
	TableView<Customers> tblClientes;

	@FXML
	TableColumn colCI, colNombre, colTelefono, colDireccion;

	// Instancia de la clase Alert para generar alertas
	private Alert alerta;
	private DBQueries dbQueries;
	private Customers customer;
	private HashMap<String, Customers> mapaIdCliente;
	private List<Customers> listaClientes;
	private String cedulaSeleccionada = "";

	public void initialize(){
		init();
	}

	private void init() {
		alerta = new Alert(null);
		dbQueries = new DBQueries();
		mapaIdCliente = new HashMap<>();
		mapaIdCliente = dbQueries.getIdCliente();
		listaClientes = new ArrayList<>(mapaIdCliente.values());
		listenerTable();

	}


	public void crear() {
		System.out.println("Crear");
		if(!comprobarCamposVacios())
			insertar();
	}

	private boolean comprobarCamposVacios() {
		if(txtCedula == null || txtCedula.getText().equals("")) {
			alerta("Cedula");
			return true;
		}else if(txtNombre == null || txtNombre.getText().equals("")) {
			alerta("Nombre");
			return true;
		}else if(txtTelefono == null || txtTelefono.getText().equals("")) {
			alerta("Telefono");
			return true;
		}else if(txtDireccion == null || txtDireccion.getText().equals("")) {
			alerta("Direccion");
			return true;
		}else if(txtEmail == null || txtEmail.getText().equals("")) {
			alerta("Email");
			return true;
		}else
			return false;

	}

	private void insertar() {
		DBInserts dbInserts = new DBInserts();
		String retorno = dbInserts.insertCliente(txtCedula.getText(), txtNombre.getText(), txtTelefono.getText(),
				txtDireccion.getText(), txtEmail.getText());
		if(!retorno.equals(""))
			System.out.println(retorno);
	}

	private void alerta(String paramCampo) {
		alerta.setAlertType(AlertType.ERROR);
		alerta.setTitle("ERROR");
		alerta.setHeaderText("Campo vacio");
		alerta.setContentText("El campo + " + paramCampo + ", se encuentra vacio, por favor reviselo.");
		alerta.showAndWait();
	}

	public void actualizar() {
		DBUpdates dbu = new DBUpdates();
		dbu.updateCustomer(Integer.valueOf(cedulaSeleccionada), txtNombre.getText(), txtTelefono.getText(), txtDireccion.getText(), txtEmail.getText());
		System.out.println("actualizado correctamente");
	}

	public void eliminar() {
		System.out.println("Eliminar Empleado");
		DBDelete dbDelete = new DBDelete();
		//if(customer != null) {
		System.out.println(txtCedula.getText());
		int var=dbDelete.deleteCustomer(Integer.valueOf(txtCedula.getText()));
		dbDelete.deleteCustomer(Integer.valueOf(txtCedula.getText()));
		if(var==0){
			System.out.println("Eliminado correctamente");
		}else if(var==1){
			System.out.println("-----");
			alerta.setAlertType(AlertType.ERROR);
			alerta.setTitle("ERROR");
			alerta.setHeaderText("Imposible Eliminar");
			alerta.setContentText("No se puede eliminar el cliente por que \ntiene un/os paciente/s asociados!.");
			alerta.showAndWait();
		}

	}
	public void listenerTable() {
		tblClientes.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
			System.out.println("Se clickea");
    		customer = tblClientes.getSelectionModel().getSelectedItem();
			setearDatos(customer);
		});
	}

	private void setearDatos(Customers customer) {
		if(customer != null) {
			txtNombre.setText(customer.getNombre());
			txtCedula.setText(customer.getNombre());
			txtEmail.setText(customer.getEmail());
			txtTelefono.setText(customer.getTelefono());
			txtDireccion.setText(customer.getDireccion());
		}
	}

	public void listar() {
		//colCI, colNombre, colTelefono, colDireccion;
		System.out.println("--------------");
		colCI.setCellValueFactory(new PropertyValueFactory("ci"));
		colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
		colTelefono.setCellValueFactory(new PropertyValueFactory("email"));
		colDireccion.setCellValueFactory(new PropertyValueFactory("direccion"));
		ObservableList<Customers> data = FXCollections.observableArrayList(listaClientes);
		tblClientes.setItems(data);
	}

	public void buscar() {
		Dialog dialogo = new Dialog<>();

		GridPane grid = new GridPane();

		Label lblCantidad = new Label("Cedula/RUC Cliente:");
		grid.add(lblCantidad, 0, 0);
		TextField txtNombre = new TextField();
		grid.add(txtNombre, 1, 0);

		dialogo.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialogo.getDialogPane().getButtonTypes().add(ButtonType.NO);
		dialogo.getDialogPane().setContent(grid);

		dialogo.show();

		if(dialogo.getResult().equals(ButtonType.OK)){
			customer = mapaIdCliente.get(txtNombre.getText());
			txtCedula.setText(customer.getCi());
			cedulaSeleccionada = customer.getCi();
			txtDireccion.setText(customer.getDireccion());
			txtEmail.setText(customer.getEmail());
			this.txtNombre.setText(customer.getNombre());
			txtTelefono.setText(customer.getTelefono());
			dialogo.close();
		}
	}

	public void main() throws IOException {
		Stage primaryStage = (Stage) btnMain.getScene().getWindow();
		Parent root = FXMLLoader.load(this.getClass().getResource("MainMenu.fxml"));
		primaryStage.setTitle("Mundo Ganadero");
		primaryStage.setScene(new Scene(root, 650, 490));
		primaryStage.show();


	}

}
