package application;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import application.resources.controller.DBQueries;
import application.resources.fxControllers.ControllerNewProduct;
import application.resources.model.Product;
import application.resources.model.Provider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControllerInventory {
	
	@FXML
	TableView<Product> tblProductos;
	
	@FXML
	TableColumn colNombre, colNomFactura, colCategoria, colStock, colFechaV, colPrecioUnitario, colPrecioCaja, colTarifa;
	
	@FXML
	Button btnMostrar, btnEditar, btnMenu;
	
	private List<Product> lista;
	private Product producto;
	private HashMap<String, Product> productoHashMap;
	
	private List<Provider> listaProveedores = new ArrayList<>();
	private HashMap<String, String> proveedoresMap;

	
	public void initialize(){
		init();
    }
	
	private void init() {
		System.out.println("RECONOCE");
		DBQueries dbQueries = new DBQueries();
		
		productoHashMap = dbQueries.productosHashMap().get(0);
		proveedoresMap = new HashMap<>();
		
		listaProveedores = dbQueries.getProviders();
		for(Provider p : listaProveedores)
			proveedoresMap.put(p.getRuc(), p.getNombre());
		
		lista = new ArrayList<>();
		
		for(Entry<String, Product> entry : productoHashMap.entrySet()) {
			lista.add(entry.getValue());
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void cargarTabla(List<Product> paramProductos) {
		ObservableList<Product> productosObservableList = FXCollections.observableArrayList(paramProductos);
		colNombre.setCellValueFactory(new PropertyValueFactory("id"));
		colNomFactura.setCellValueFactory(new PropertyValueFactory("main_id"));
		colCategoria.setCellValueFactory(new PropertyValueFactory("representation"));
		colStock.setCellValueFactory(new PropertyValueFactory("stock"));
		colFechaV.setCellValueFactory(new PropertyValueFactory("fecha"));
		colPrecioUnitario.setCellValueFactory(new PropertyValueFactory("unit_price"));
		colPrecioCaja.setCellValueFactory(new PropertyValueFactory("box_price"));
		colTarifa.setCellValueFactory(new PropertyValueFactory("tax"));
		tblProductos.setItems(productosObservableList);
	}
	
	private void listenerTabla() {
		tblProductos.setOnMouseClicked(event ->{
			producto = tblProductos.getSelectionModel().getSelectedItem();
			producto = productoHashMap.get(producto.getId());
			
		});
	}
	
	public void mostrar() {
		System.out.println("FUNCA");
		cargarTabla(lista);
		listenerTabla();
	}
	
	private void abrirProducto() throws IOException {
	    	FXMLLoader loader = new FXMLLoader(this.getClass().getResource("NewProduct.fxml"));
	    	Parent root = loader.load();
	    	application.ControllerNewProduct objCont = loader.getController();
	    	
	    	objCont.txtCodProd.setText(producto.getId());
	    	objCont.txtCodFac.setText(producto.getMain_id());
	    	objCont.txtNomProd.setText(producto.getName());
	    	objCont.txtNomFac.setText(producto.getMain_name());
	    	
	    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    	LocalDate localDate = LocalDate.parse(producto.getFecha(), formatter);
	    	objCont.fechaProd.setValue(localDate);
	    	
	    	
	    	objCont.comboProveedor.getSelectionModel().select(proveedoresMap.get(producto.getProv_RUC()));
	    	objCont.comboCategoria.getSelectionModel().select(producto.getCategory());
	    	objCont.comboTipoRep.getSelectionModel().select(producto.getRepresentation());
	    	
	    	objCont.txtCantidadInt.setText(String.valueOf(producto.getInternal_quantity()));
	    	objCont.txtStock.setText(String.valueOf(producto.getStock()));
	    	objCont.txtPrecioComCaja.setText(String.valueOf(producto.getBox_price()));
	    	objCont.txtPrecioComUnidad.setText(String.valueOf(producto.getUnit_price()));
	    	objCont.txtPrecioCajaImp.setText(String.valueOf(producto.getBox_12()));
	    	objCont.txtPrecioUnidadImp.setText(String.valueOf(producto.getUnit_12()));
	    	
	    	if(producto.getTax().equals("12")) {
	    		objCont.radioIVA0.setSelected(false);
	    		objCont.radioIVA12.setSelected(true);
	    	}else {
	    		objCont.radioIVA0.setSelected(true);
	    		objCont.radioIVA12.setSelected(false);
	    	}
	    	
	    	objCont.txtPrecioVCaja.setText(String.valueOf(producto.getSell_box()));
	    	objCont.txtPrecioVUnidad.setText(String.valueOf(producto.getSell_unit()));
	    	
	    	
	    	Stage stage = (Stage) btnEditar.getScene().getWindow();
	    	stage.setTitle("Productos");
	    	stage.setScene(new Scene(root, 693, 620));
	    	stage.centerOnScreen();
	    	stage.setResizable(true);
	    	stage.show();
	}
	
	public void editar() throws IOException {
		if(producto != null) {
			abrirProducto();
		}
	}
	
	public void menu() throws IOException {
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("MainMenu.fxml"));
    	Parent root = loader.load();
    	Stage stage = (Stage) btnEditar.getScene().getWindow();
    	stage.setTitle("Mundo Ganadero");
    	stage.setScene(new Scene(root, 650, 490));
    	stage.centerOnScreen();
    	stage.setResizable(true);
    	stage.show();
	}
}
