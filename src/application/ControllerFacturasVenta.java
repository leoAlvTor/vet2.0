package application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.resources.controller.DBQueries;
import application.resources.model.FacturaCabecera;
import application.resources.model.FacturaDetalle;
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

public class ControllerFacturasVenta {
	
	@FXML
	TableView<FacturaCabecera> tblFacturas;
	@FXML
	TableColumn colID, colCliente, colFecha, colSubtotal, colIVA, colDescuento, colTotal;
	@FXML
	Button btnAbrir, btnBuscarCodigo, btnListarFecha, btnMenu;
	
	private DBQueries dbQueries;
	
	private HashMap<Integer, FacturaCabecera> mapaIDFactura;
	private List<FacturaCabecera> listaFacturaCabeceras;
	private ObservableList<FacturaCabecera> observableFacturasCabecera;
	private FacturaCabecera facturaCabecera;
	
	public void initialize() {
		dbQueries = new DBQueries();
		mapaIDFactura = new HashMap<>();
		listaFacturaCabeceras = new ArrayList<>();
		
		prepararCampos();
		listenerTabla();
		
		init();
	}


	
	private void init() {
		mapaIDFactura = dbQueries.getMapaIDFacturaCab();
		
		listaFacturaCabeceras = new ArrayList<>(mapaIDFactura.values());
		cargarTabla(listaFacturaCabeceras);
		
	}
	
	private void cargarTabla(List<FacturaCabecera> paramLista) {
		observableFacturasCabecera = FXCollections.observableArrayList(paramLista);
		tblFacturas.setItems(observableFacturasCabecera);
	}
	
	private void listenerTabla() {
		tblFacturas.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
			facturaCabecera = tblFacturas.getSelectionModel().getSelectedItem();
			
		});
	}
	
	private void prepararCampos() {
		colID.setCellValueFactory(new PropertyValueFactory("numeroFactura"));
		colCliente.setCellValueFactory(new PropertyValueFactory("ruc"));
		colFecha.setCellValueFactory(new PropertyValueFactory("fecha"));
		colSubtotal.setCellValueFactory(new PropertyValueFactory("subtotal"));
		colIVA.setCellValueFactory(new PropertyValueFactory("iva"));
		colDescuento.setCellValueFactory(new PropertyValueFactory("descuento"));
		colTotal.setCellValueFactory(new PropertyValueFactory("total"));
	}
	
	public void abrir() throws IOException {
		if(facturaCabecera != null) {
			cargarFactura(facturaCabecera, facturaCabecera.anulada);
		}else
			alertaFactura();
	}
	
	private void cargarFactura(FacturaCabecera param, boolean paramAnulada) throws IOException {
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Invoice.fxml"));
    	Parent root = loader.load();
    	ControllerInvoice invoice = loader.getController();
    	
    	dbQueries.connect();
    	List<FacturaDetalle> detalles = dbQueries.getFacturasDetalles(Integer.parseInt(param.getNumeroFactura()));
    	
    	invoice.abrirFactura(param, detalles, paramAnulada);
    	
    	Stage stage = (Stage) btnAbrir.getScene().getWindow();

    	stage.setTitle("Apertura de Factura");
    	stage.setScene(new Scene(root, 960, 800));
    	stage.sizeToScene();
    	stage.centerOnScreen();
    	stage.setResizable(true);
    	stage.show();
	}

	private void alertaNoEncontrado(int param){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Sin resultado");
		alert.setHeaderText("No se ha encontrado la/s factura/s");
		if(param == 1)
			alert.setContentText("La busqueda por rango de fechas no ha devuelto ninguna factura.");
		else if(param == 2)
			alert.setContentText("La busqueda por codigo de la factura no ha devuelto ningun valor.");
		alert.showAndWait();
	}

	private void alertaFactura() {
		Alert alerta = new Alert(AlertType.WARNING);
		alerta.setTitle("Advertencia");
		alerta.setHeaderText("Error al intentar abrir la factura.");
		alerta.setContentText("Al parecer no ha seleccionado ninguna \n"
				+ "factura, por favor seleccione una.");
	}
	
	public void buscarCodigo() {
		Dialog dialogoCodigo = new Dialog();

		GridPane grid = new GridPane();
		grid.add(new Label("Ingrese el codigo:"),0,0);

		TextField txtCodigo = new TextField();
		grid.add(txtCodigo, 1, 0);

		dialogoCodigo.getDialogPane().setContent(grid);

		dialogoCodigo.setTitle("Busqueda por codigo");

		dialogoCodigo.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialogoCodigo.getDialogPane().getButtonTypes().add(ButtonType.NO);

		dialogoCodigo.showAndWait();

		if(dialogoCodigo.getResult().equals(ButtonType.OK))
			cargarPorCodigo(txtCodigo.getText());
	}

	private void cargarPorCodigo(String codigo){
		FacturaCabecera facturaCabecera = mapaIDFactura.get(Integer.parseInt(codigo));


		if(facturaCabecera != null) {
			listaFacturaCabeceras = null;
			listaFacturaCabeceras = new ArrayList<>();
			listaFacturaCabeceras.add(facturaCabecera);
			cargarTabla(listaFacturaCabeceras);
		}else
			alertaNoEncontrado(2);
	}
	
	public void listarFecha() {
		Dialog dialog = new Dialog();

		GridPane gridPane = new GridPane();

		Label lblFecha1 = new Label("Fecha inicial:");
		Label lblFecha2 = new Label("Fecha final:");

		gridPane.add(lblFecha1, 0, 0);
		gridPane.add(lblFecha2, 0, 1);

		DatePicker datePicker1 = new DatePicker();
		DatePicker datePicker2 = new DatePicker();

		gridPane.add(datePicker1, 1, 0);
		gridPane.add(datePicker2, 1, 1);

		dialog.getDialogPane().setContent(gridPane);

		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.NO);

		dialog.setTitle("Seleccione un rango");

		dialog.showAndWait();

		if(dialog.getResult().equals(ButtonType.OK)) {
			String fecha1 = dateToString(datePicker1.getValue());
			String fecha2 = dateToString(datePicker2.getValue());
			cargarXFecha(fecha1, fecha2);
		}
	}

	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");;

	private String dateToString(LocalDate localDate){
		if(localDate == null)
			return "";
		else
			return dateTimeFormatter.format(localDate);
	}

	private void cargarXFecha(String fecha1, String fecha2){
		HashMap<Integer, FacturaCabecera> mapaFacturaVentaXFecha = dbQueries.getMapaFacturaVentaXFecha(fecha1, fecha2);

		if(mapaFacturaVentaXFecha != null){
			listaFacturaCabeceras = new ArrayList<>(mapaFacturaVentaXFecha.values());
			cargarTabla(listaFacturaCabeceras);
		}else
			alertaNoEncontrado(1);


	}
	
	public void menu() {
		
	}
	
}
