package application;

import application.resources.controller.DBQueries;
import application.resources.model.CabeceraCompra;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public void abrirFactura(){

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

    public void listarFecha(){

    }

    public void menu(){

    }




}
