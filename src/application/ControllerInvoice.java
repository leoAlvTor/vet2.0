package application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import application.resources.controller.DBInserts;
import application.resources.controller.DBQueries;
import application.resources.controller.DBUpdates;
import application.resources.model.Customers;
import application.resources.model.FacturaCabecera;
import application.resources.model.FacturaDetalle;
import application.resources.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ControllerInvoice {

    @FXML
    TextField txtRUC, txtNombre, txtDireccion, txtTelefono, txtFecha,
        txtNumFac, txtProducto,
        txtSubtotal, txtIVA, txtDescuento, txtTotal;

    @FXML
    RadioButton radioContado, radioCredito, radioCodigo, radioNombre;

    @FXML
    TableView<FacturaDetalle> tblDatos;

    @FXML
    Button btnGuardar, btnActualizar, btnCancelar, btnAnular;
    
    @FXML
    TableColumn colCodigo, colDescripcion, colTarifa, colCantidad, colPrecioUnit, colPrecioCaja, colTotal;

    private DBQueries dbQueries;
    
    private Thread t;

    private Controller c = new Controller();
    private String empleado = c.leerArchivo();
    
    public void initialize(){
    	dbQueries = new DBQueries();
    	t = new Thread(this::init);
    	t.run();
    }
    
    private HashMap<String, String> mapaClienteRuc;
    private List<Customers> listaClientes;
    private HashMap<String, Customers> mapaRucCliente;
    private AutoCompletionBinding<String> autocompleteCliente;
    private List<String> nombreClientes;
    private Customers clienteSeleccinado;
    
    
    private HashMap<String, Product> productosIdProdMap;
    private HashMap<String, Product> productosNombreProdMap;
    
    private List<String> productosList;
    private AutoCompletionBinding<String> autocompleteProductos;
    
    private List<FacturaDetalle> detallesProductos;
    private FacturaDetalle facturaDetalle;
    private FacturaCabecera facturaCabecera;
    
    private void init() {
    	listaClientes = dbQueries.clientes();
    	mapaClienteRuc = new HashMap<>();
    	mapaRucCliente = new HashMap<>();
    	nombreClientes = new ArrayList<String>();
    	clienteSeleccinado = new Customers();
    	autocompleteProductos = TextFields.bindAutoCompletion(txtProducto, "");
    	prepararTabla();
    	
    	txtDescuento.setText("0");
    	txtRUC.setText("9999999999999");
    	txtNombre.setText("Consumidor Final");
    	txtDireccion.setText("NA");
    	txtTelefono.setText("NA");
    	
    	txtSubtotal.setText("0");
    	
    	detallesProductos = new ArrayList<>();
    	facturaDetalle = new FacturaDetalle();
    	facturaCabecera = new FacturaCabecera();
    	LocalDate now = LocalDate.now();
    	txtFecha.setText(now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    	for(Customers c : listaClientes) {
    		mapaClienteRuc.put(c.getNombre(), c.getCi());
    		mapaRucCliente.put(c.getCi(), c);
    		nombreClientes.add(c.getNombre());
    	}
    	usuarioAutoComplete();
    	List<HashMap<String, Product>> listaHash = dbQueries.productosHashMap();
    	
    	productosIdProdMap = listaHash.get(0);
    	productosNombreProdMap = listaHash.get(1);

    	radioCodigo.setOnAction(event ->{
    		eventoRadioCodigo();
    	});
    	
    	radioNombre.setOnAction(event ->{
    		eventoRadioNombre();
    	});
    	
    	radioContado.setOnAction(event->{
    		radioCredito.setSelected(false);
    	});
    	
    	radioCredito.setOnAction(event->{
    		radioContado.setSelected(false);
    	});
    	
    	listenerCampo();
    	eventoDescuento();
    	
    	txtNumFac.setText(dbQueries.getFacturaActual());
    }
    
    private void eventoDescuento() {
    	txtDescuento.setOnKeyPressed(event ->{
    		if(event.getCode().equals(KeyCode.ENTER) && !txtDescuento.getText().equals("")) {
    			calcular();
    		}
    	});
    }
    
    
    private void eventoRadioCodigo() {
    	radioNombre.setSelected(false);
    	buscarProductoPorCodigo();
    }
    
    private void eventoRadioNombre() {
    	radioCodigo.setSelected(false);
    	buscarProductoPorNombre();
    }
    
    private void usuarioAutoComplete() {
    	txtNombre.setOnKeyPressed(event ->{
    		if(event.getCode().equals(KeyCode.ENTER) && !txtNombre.getText().equals("")) {
    			clienteSeleccinado = mapaRucCliente.get(mapaClienteRuc.get(txtNombre.getText()));
    			txtRUC.setText(clienteSeleccinado.getCi());
    			txtNombre.setText(clienteSeleccinado.getNombre());
    			txtDireccion.setText(clienteSeleccinado.getDireccion());
    			txtTelefono.setText(clienteSeleccinado.getTelefono());
    		}
    	});
    	
    	autocompleteCliente = TextFields.bindAutoCompletion(txtNombre, nombreClientes);
    	
    	
    }
    
    private void eliminarCampo(FacturaDetalle param) {
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	alert.setTitle("Eliminar Producto");
    	alert.setHeaderText("Desea eliminar el producto ingresado?");
    	alert.setContentText("Presione OK para eliminar o CANCEL para no hacerlo");
    	
    	Optional<ButtonType> result = alert.showAndWait();
    	if(result.get() == ButtonType.OK) {
    		for(int i = 0; i < detallesProductos.size(); i++)
        		if(detallesProductos.get(i).equals(param))
        			detallesProductos.remove(i);
    		
    		cargarTabla(detallesProductos);
    	}
    }
    
    private void editarCampo(FacturaDetalle param) {
    	
    	for(int i = 0; i < detallesProductos.size(); i++)
    		if(detallesProductos.get(i).equals(param))
    			detallesProductos.remove(i);
    	
    	Dialog dialogo = new Dialog<>();
    	final String flag = String.valueOf(param.getPrecioCaja());
    	
    	GridPane grid = new GridPane();
    	
    	Label lblCantidad = new Label("Cantidad:");
    	grid.add(lblCantidad, 0, 0);
    	TextField txtCantidad = new TextField("1");
    	grid.add(txtCantidad, 1, 0);
    	
    	Label lblPrecioUnit = new Label("Precio Unitario:");
    	grid.add(lblPrecioUnit, 0, 1);
    	TextField txtPrecioUnit = new TextField();
    	grid.add(txtPrecioUnit, 1, 1);
    	
    	Label lblPrecioCaja = new Label("Precio Caja:");
    	grid.add(lblPrecioCaja, 0, 2);
    	TextField txtPrecioCaja = new TextField();
    	grid.add(txtPrecioCaja, 1, 2);
    	
    	if(param.getPrecioCaja() == 0) {
    		txtPrecioCaja.setText("0");
    		txtPrecioUnit.setText(String.valueOf(param.getPrecioUnit()));
    	}else {
    		txtPrecioCaja.setText(String.valueOf(param.getPrecioCaja()));
    		txtPrecioUnit.setText(String.valueOf(param.getPrecioUnit()));
    	}
    	
    	txtCantidad.requestFocus();
 
    	Button botonCargar = new Button("Ingresar Producto");
    	grid.add(botonCargar, 0, 3);
    	Button botonCancelar = new Button("Cancelar");
    	grid.add(botonCancelar, 1, 3);    	
    	dialogo.getDialogPane().setContent(grid);

    	dialogo.show();
    	
    	botonCargar.setOnAction(event ->{
    		dialogo.setResult(Boolean.TRUE);
    		facturaDetalle = new FacturaDetalle();
    		
    		facturaDetalle.setCodigo(param.getCodigo());
    		facturaDetalle.setDescripcion(param.getDescripcion());
    		facturaDetalle.setCantidad(Double.parseDouble(txtCantidad.getText()));
    		facturaDetalle.setPrecioCaja(Double.parseDouble(txtPrecioCaja.getText()));
    		facturaDetalle.setPrecioUnit(Double.parseDouble(txtPrecioUnit.getText()));
    		facturaDetalle.setTarifa(param.getTarifa());
    		if(flag.equals("0.0")) {
    			facturaDetalle.setTotal(Double.parseDouble(txtCantidad.getText()) * Double.parseDouble(txtPrecioUnit.getText()));
    		}else if(!flag.equals("0")){
    			facturaDetalle.setTotal(Double.parseDouble(txtCantidad.getText()) * Double.parseDouble(txtPrecioCaja.getText()));
    		}
    		
    		
    		
    		detallesProductos.add(facturaDetalle);	
    		cargarTabla(detallesProductos);
    		
    		dialogo.close();
    	});

    	botonCancelar.setOnAction(event ->{
    		dialogo.setResult(Boolean.TRUE);
    		dialogo.close();
    	});
    }
    
    private void listenerCampo() {
    	tblDatos.addEventHandler(MouseEvent.MOUSE_CLICKED, event->{
    		FacturaDetalle objFacDetalle = tblDatos.getSelectionModel().getSelectedItem();
    		if(objFacDetalle != null && event.getButton() == MouseButton.PRIMARY)
    			editarCampo(objFacDetalle);
    		else if(objFacDetalle != null && event.getButton() == MouseButton.SECONDARY)
    			eliminarCampo(objFacDetalle);
    	});
    }

    
    private void buscarProductoPorNombre() {
    	txtProducto.setOnKeyPressed(null);
    	autocompleteProductos.dispose();
    	
    	txtProducto.setOnKeyPressed(event ->{
    		if(event.getCode().equals(KeyCode.ENTER))
    			getProductoNombre(productosNombreProdMap.get(txtProducto.getText()));
    	});
    	
    	productosList = new ArrayList<>(productosNombreProdMap.keySet());
    	autocompleteProductos = TextFields.bindAutoCompletion(txtProducto, productosList);
    	
    }
    
    private void getProductoNombre(Product paramProducto) {
    	dialogoProduct(paramProducto);
    }
    
    private void buscarProductoPorCodigo() {
    	txtProducto.setOnKeyPressed(null);
    	autocompleteProductos.dispose();
    	
    	txtProducto.setOnKeyPressed(event ->{
    		if(event.getCode().equals(KeyCode.ENTER)) {
    			getProductoCodigo(productosIdProdMap.get(txtProducto.getText()));
    			
    		}
    	});
    	
    	productosList = new ArrayList<>(productosIdProdMap.keySet());
    	autocompleteProductos = TextFields.bindAutoCompletion(txtProducto, productosList);
    	
    }
    
    private void getProductoCodigo(Product paramProducto) {
    	dialogoProduct(paramProducto);
    }
    
    private void dialogoProduct(Product param) {
    	Dialog dialogo = new Dialog<>();
    	final String flag = String.valueOf(param.getBox_price());
    	
    	GridPane grid = new GridPane();
    	
    	Label lblCantidad = new Label("Cantidad:");
    	grid.add(lblCantidad, 0, 0);
    	TextField txtCantidad = new TextField("1");
    	grid.add(txtCantidad, 1, 0);
    	
    	Label lblPrecioUnit = new Label("Precio Unitario:");
    	grid.add(lblPrecioUnit, 0, 1);
    	TextField txtPrecioUnit = new TextField();
    	grid.add(txtPrecioUnit, 1, 1);
    	
    	Label lblPrecioCaja = new Label("Precio Caja:");
    	grid.add(lblPrecioCaja, 0, 2);
    	TextField txtPrecioCaja = new TextField();
    	grid.add(txtPrecioCaja, 1, 2);
    	
    	if(param.getBox_price() == 0) {
    		txtPrecioCaja.setText("0");
    		txtPrecioUnit.setText(String.valueOf(param.getSell_unit()));
    	}else {
    		txtPrecioCaja.setText(String.valueOf(param.getSell_box()));
    		txtPrecioUnit.setText(String.valueOf(param.getSell_unit()));
    	}
    	
    	txtCantidad.requestFocus();
    	
    	Button btnCargar = new Button("Ingresar Producto");
    	grid.add(btnCargar, 0, 3);
    	
    	Button botonCancelar = new Button("Cancelar");
    	grid.add(botonCancelar, 1, 3);
    	
    	dialogo.getDialogPane().setContent(grid);
    	
    	
    	
    	dialogo.show();
    	
    	btnCargar.setOnAction(event ->{
    		System.out.println("leyo");
    		dialogo.setResult(Boolean.TRUE);
    		facturaDetalle = new FacturaDetalle();
    		
    		facturaDetalle.setCodigo(param.getId());
    		facturaDetalle.setDescripcion(param.getName());
    		facturaDetalle.setCantidad(Double.parseDouble(txtCantidad.getText()));
    		facturaDetalle.setPrecioCaja(Double.parseDouble(txtPrecioCaja.getText()));
    		facturaDetalle.setPrecioUnit(Double.parseDouble(txtPrecioUnit.getText()));
    		facturaDetalle.setTarifa(param.getTax());
    		if(flag.equals("0.0")) {
    			facturaDetalle.setTotal(Double.parseDouble(txtCantidad.getText()) * Double.parseDouble(txtPrecioUnit.getText()));
    		}else if(!flag.equals("0")){
    			facturaDetalle.setTotal(Double.parseDouble(txtCantidad.getText()) * Double.parseDouble(txtPrecioCaja.getText()));
    		}
    		System.out.println(facturaDetalle.toString());
    		detallesProductos.add(facturaDetalle);	
    		cargarTabla(detallesProductos);
    		dialogo.close();
    	});
    	
    	botonCancelar.setOnAction(event ->{
    		dialogo.setResult(Boolean.TRUE);
    		dialogo.close();
    	});
    	
    }
    private ObservableList<FacturaDetalle> detalleTabla;
    
    private void prepararTabla() {
    	colCodigo.setCellValueFactory(new PropertyValueFactory("codigo"));
    	colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
    	colCantidad.setCellValueFactory(new PropertyValueFactory("cantidad"));
    	colPrecioUnit.setCellValueFactory(new PropertyValueFactory("precioUnit"));
    	colPrecioCaja.setCellValueFactory(new PropertyValueFactory("precioCaja"));
    	colTotal.setCellValueFactory(new PropertyValueFactory("total"));
    	colTarifa.setCellValueFactory(new PropertyValueFactory("tarifa"));
    }
    
    private void cargarTabla(List<FacturaDetalle> detalles) {
    	detalleTabla = FXCollections.observableArrayList(detalles);
    	tblDatos.setItems(detalleTabla);
    	calcular();
    }
    
    private void calcular() {
    	double subtotal = 0;
    	double iva = 0;
    	double descuento = Double.parseDouble(txtDescuento.getText());
    	double total = 0;
    	
    	if(tblDatos.getItems() != null) {
    		for(FacturaDetalle d : tblDatos.getItems()) {
    			subtotal += d.getTotal();
    			if(d.getTarifa().equals("12"))
    				iva += d.getTotal() * 0.12;
    		}
    		
    		total = (subtotal) - descuento;
    		txtSubtotal.setText(String.valueOf(subtotal));
    		txtIVA.setText(String.valueOf(iva));
    		txtDescuento.setText(String.valueOf(descuento));
    		txtTotal.setText(String.valueOf(total));
    	}
    }
    
    public void anularFactura() {
    	dialogoAnular();
    	
    	if(banderaUpdate)
    		anular();
    	else
    		alertaAnular();
    		
    }
    
    private void anular() {
    	DBUpdates dbupdates = new DBUpdates();
    	if(dialogoAnular()) {
    		dbupdates.anularFactura(txtNumFac.getText());
    		devolverProductos(dbupdates);
    	}	
    }
    
    private void devolverProductos(DBUpdates paramDB) {
    	for(FacturaDetalle f : tblDatos.getItems())
    		paramDB.devolverProducto(f.getCodigo(), String.valueOf(f.getCantidad()));
    	paramDB.disconnect();
    }
    
    private boolean dialogoAnular() {
    	Dialog dialogo = new Dialog();
    	dialogo.setTitle("Anular Factura");
    	dialogo.setContentText("Esta seguro que desea anular la factura?");
    	dialogo.getDialogPane().getButtonTypes().add(ButtonType.NO);
    	dialogo.getDialogPane().getButtonTypes().add(ButtonType.OK);
    	dialogo.showAndWait();
    	if(dialogo.getResult().equals(ButtonType.OK))
    		return true;
    	else if(dialogo.getResult().equals(ButtonType.NO))
    		return false;
    	else 
    		return false;
    	
    }
    
    private void alertaAnular() {
    	Alert alerta = new Alert(AlertType.INFORMATION);
    	alerta.setTitle("Informacion");
    	alerta.setHeaderText("No se ha podido anular la factura");
    	alerta.setContentText("No se puede anular la factura, por favor abra la \n"
    			+ " factura desde el modulo correspondiente.");
    	alerta.showAndWait();
    }
    
    public void cancelarFactura() throws IOException {
        	Stage primaryStage = (Stage) btnActualizar.getScene().getWindow();
        	Parent root = FXMLLoader.load(this.getClass().getResource("MainMenu.fxml"));
            primaryStage.setTitle("Mundo Ganadero");
            primaryStage.setScene(new Scene(root, 650, 490));
            primaryStage.show();
    }
    
    public void actualizarFactura(){
    	if(!comprobador()) {
    		actualizarDatosCabecera();
    	}
    }
    
    private void actualizarDatosCabecera() {
    	DBUpdates dbUpdate = new DBUpdates();
    	
    	if(radioContado.isSelected()) {
    		dbUpdate.updateFacturaCabecera(clienteSeleccinado.getCi(), "contado", txtSubtotal.getText(), txtIVA.getText(),
    				txtDescuento.getText(), txtTotal.getText(), txtNumFac.getText());
    	}else if(radioCredito.isSelected()) {
    		dbUpdate.updateFacturaCabecera(clienteSeleccinado.getCi(), "credito", txtSubtotal.getText(), txtIVA.getText(),
    				txtDescuento.getText(), txtTotal.getText(), txtNumFac.getText());
    	}
    }
    
    
    public void guardarFactura() {
    	if(!comprobador()) 
    		guardarDatosCabecera();
    }

	private void limpiar(){
		txtDescuento.setText("0");
		txtProducto.setText("");
		txtNombre.setText("");
		txtSubtotal.setText("0");
		txtIVA.setText("0");
		txtNumFac.setText(String.valueOf(Integer.parseInt(txtNumFac.getText())+1));
		txtTotal.setText("0");
		txtDireccion.setText("");
		txtTelefono.setText("");
		txtRUC.setText("");
		tblDatos.setItems(null);
	}
    
    private void guardarDatosCabecera() {
    	DBInserts dbInserts = new DBInserts();
    	dbInserts.connect();
    	if(radioContado.isSelected()) {
    		dbInserts.insertFacturaCabecera(clienteSeleccinado.getCi(), "contado", txtFecha.getText(), txtSubtotal.getText(), txtIVA.getText(),
    				txtDescuento.getText(), txtTotal.getText(), empleado);
    	}else if(radioCredito.isSelected()) {
    		dbInserts.insertFacturaCabecera(clienteSeleccinado.getCi(), "credito", txtFecha.getText(), txtSubtotal.getText(), txtIVA.getText(),
    				txtDescuento.getText(), txtTotal.getText(), empleado);
    	}
    	guardarFacturaDetalle(dbInserts);
    }
    
    private void guardarFacturaDetalle(DBInserts dbInserts) {
    	for(FacturaDetalle p : tblDatos.getItems())
    		dbInserts.insertFacturaDetalle(txtNumFac.getText(), p.getCodigo(), p.getDescripcion(), p.getTarifa(), p.getCantidad(),
    				p.getPrecioUnit(), p.getPrecioCaja(), p.getTotal());
    	dbInserts.disconnect();
    	limpiar();
    }
    
    private boolean comprobador() {
    	if(clienteSeleccinado == null) {
    		alerta("Nombre Del Cliente");
    		return true;
    	}else if(radioContado.isSelected() == false && radioCredito.isSelected() == false) {
    		alerta("Contado | Credito");
    		return true;
    	}else {
    		return false;
    	}
    }
    
    private void alerta(String value) {
    	Alert alerta = new Alert(AlertType.WARNING);
    	alerta.setTitle("Error");
    	alerta.setHeaderText("Un campo se encuentra vacio");
    	alerta.setContentText("El campo " + value + " esta vacio o no ha seleccionado \n "
    							+ "correctamente al cliente, por favor reviselo.");
    	alerta.showAndWait();
    }

    private boolean banderaUpdate = false;
    
    public void abrirFactura(FacturaCabecera param, List<FacturaDetalle> paramDetalles, boolean paramAnulada) {
    	try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	if(paramAnulada)
    		btnAnular.setVisible(false);
    	
    	banderaUpdate = true;
    	
    	txtProducto.setEditable(false);
    	btnGuardar.setVisible(false);
    	
    	
    	txtRUC.setText(param.getRuc());
    	
    	Customers c = mapaRucCliente.get(param.getRuc());
    	
    	txtNombre.setText(c.getNombre());
    	txtDireccion.setText(c.getDireccion());
    	txtTelefono.setText(c.getTelefono());
    	
    	txtFecha.setText(param.getFecha());
    	txtNumFac.setText(param.getNumeroFactura());
    	radioContado.setSelected(true);
    	txtSubtotal.setText(String.valueOf(param.getSubtotal()));
    	txtIVA.setText(String.valueOf(param.getIva()));
    	txtDescuento.setText(String.valueOf(param.getDescuento()));
    	txtTotal.setText(String.valueOf(param.getTotal()));
    	cargarTabla(paramDetalles);
    }

}