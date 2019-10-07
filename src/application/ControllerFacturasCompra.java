package application;

import application.resources.controller.DBQueries;
import application.resources.model.CabeceraCompra;
import application.resources.model.DetalleCompra;
import application.resources.model.FacturaCabecera;
import application.resources.model.ModelDetailPurchase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerFacturasCompra {

    @FXML
    TableView<CabeceraCompra> tblFacturas;

    @FXML
    TableColumn colID, colRUC, colAutorizacion, colFecha, colSub12, colIVA, colTotal;

    @FXML
    Button btnAbrir, btnBuscarProv, btnBuscarFecha, btnMenu;

    private DBQueries dbQueries;
    private HashMap<String, CabeceraCompra> mapaIDFacturaCompra;
    private List<CabeceraCompra> listaFacturas;
    private ObservableList<CabeceraCompra> cabeceraCompraObservableList;
    private CabeceraCompra cabeceraCompra;

    public void initialize(){
        initCompra();
    }

    public void initCompra(){
        dbQueries = new DBQueries();
        mapaIDFacturaCompra = dbQueries.getMapaIDFacturaDet();
        listaFacturas = new ArrayList<>(mapaIDFacturaCompra.values());

        prepararTabla();
        cargarTabla(listaFacturas);
        listenerTabla();
    }

    private void prepararTabla(){
        colID.setCellValueFactory(new PropertyValueFactory("compra_numero"));
        colRUC.setCellValueFactory(new PropertyValueFactory("prov_ruc"));
        colAutorizacion.setCellValueFactory(new PropertyValueFactory("compra_autorizacion"));
        colFecha.setCellValueFactory(new PropertyValueFactory("compra_fecha"));
        colSub12.setCellValueFactory(new PropertyValueFactory("compra_subtotal12"));
        colIVA.setCellValueFactory(new PropertyValueFactory("compra_iva"));
        colTotal.setCellValueFactory(new PropertyValueFactory("compra_total"));
    }

    private void cargarTabla(List<CabeceraCompra> paramLista){
        cabeceraCompraObservableList = FXCollections.observableArrayList(paramLista);
        tblFacturas.setItems(cabeceraCompraObservableList);
    }

    private void listenerTabla(){
        tblFacturas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            cabeceraCompra = tblFacturas.getSelectionModel().getSelectedItem();
        });
    }

    public void abrirFactura() throws IOException {
        if(cabeceraCompra != null)
            cargarFactura(cabeceraCompra);
        else
            alertaFactura();
    }

    private void cargarFactura(CabeceraCompra param1) throws IOException{
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("InvoicePurchases.fxml"));
        Parent root = loader.load();
        ControllerInvoicePurch invoicePurch = loader.getController();

        dbQueries.connect();
        List<ModelDetailPurchase> detalleCompras = dbQueries.getDetalleCompras(param1.getCompra_autorizacion());

        invoicePurch.abrirCompra(param1, detalleCompras);

        Stage stage = (Stage) btnAbrir.getScene().getWindow();

        stage.setTitle("Apertura de Factura");
        stage.setScene(new Scene(root, 960, 800));
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    private void alertaFactura(){
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Advertencia");
        alerta.setHeaderText("Error al intentar abrir la factura.");
        alerta.setContentText("Al parecer no ha seleccionado ninguna \n"
                + "factura, por favor seleccione una.");
    }

    public void buscarProveedor(){
        dialogoBuscarProveedor();
    }

    private void dialogoBuscarProveedor(){
        Dialog dialogo = new Dialog();
        dialogo.setTitle("Buscar Por Proveedor");

        GridPane grid = new GridPane();
        grid.add(new Label("RUC:"), 0, 0);

        TextField txtProveedor = new TextField();
        grid.add(txtProveedor, 1, 0);

        dialogo.getDialogPane().setContent(grid);
        dialogo.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialogo.getDialogPane().getButtonTypes().add(ButtonType.NO);

        dialogo.showAndWait();

        txtProveedor.requestFocus();

        if(dialogo.getResult().equals(ButtonType.OK)){
            cabeceraCompra = null;
            cabeceraCompra = dbQueries.getCabeceraCompraProveedor(txtProveedor.getText());
            if(cabeceraCompra != null){
                listaFacturas = null;
                listaFacturas = new ArrayList<>();
                listaFacturas.add(cabeceraCompra);
                cargarTabla(listaFacturas);
            }else
                alertaNotFound("{Busqueda Por Proveedor}");
        }
    }

    private void alertaNotFound(String param){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sin Resultado");
        alert.setHeaderText("No se ha obtenido ninguna factura.");
        alert.setContentText("El metodo de busqueda " + param + "\n no ha devuelto ningun resultado.");
        alert.showAndWait();
    }

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private String dateToString(LocalDate localDate){
        if(localDate != null)
            return dateTimeFormatter.format(localDate);
        else
            return "";
    }

    public void listarFecha(){
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

    private void cargarXFecha(String fecha1, String fecha2){
        HashMap<String, CabeceraCompra> mapaFacturaVentaXFecha = dbQueries.getMapaIDFacturaCompraFecha(fecha1, fecha2);
        if(mapaFacturaVentaXFecha != null){
            listaFacturas = new ArrayList<>(mapaFacturaVentaXFecha.values());
            cargarTabla(listaFacturas);
        }else
            alertaNotFound("{FECHA}");
    }

    public void menu() throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("MainMenu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnMenu.getScene().getWindow();
        stage.setTitle("Mundo Ganadero");
        stage.setScene(new Scene(root, 650, 490));
        stage.centerOnScreen();
        stage.setResizable(true);
        stage.show();
    }




}
