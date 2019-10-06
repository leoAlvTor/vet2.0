package application;

import application.resources.controller.DBInserts;
import application.resources.controller.DBQueries;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;



public class ControllerCustomer {

    @FXML
    TextField txtCedula, txtNombre, txtTelefono, txtDireccion, txtEmail;

    @FXML
    Button btnCrear, btnActualizar, btnEliminar, btnListar, btnBuscar, btnMain;

    @FXML
    TableView tblClientes;

    // Instancia de la clase Alert para generar alertas
    private Alert alerta;
    private DBQueries dbQueries;

    public void initialize(){
    	init();
    }
    
    private void init() {
    	alerta = new Alert(null);
    	dbQueries = new DBQueries();
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
    	System.out.println("ACTUALIZAR");
    }
    
    public void eliminar() {
    	
    }
    
    public void listar() {
    	System.out.println("Listar");
    }
    
    public void buscar() {
    	
    }
    
    public void main() {
    	
    }

}
