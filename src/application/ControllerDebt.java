package application;

import application.resources.controller.DBQueries;
import application.resources.model.DeudaCabecera;
import application.resources.model.DeudaDetalle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ControllerDebt {

    public TableView<DeudaCabecera> tblDeudas;
    public TableView<DeudaDetalle> tblDeudaSel;

    public TableColumn ci1, idFactura1, montoCancelar1, montoCancelado1, estado1;
    public TableColumn ci2, idFactura2, montoCancelar2, montoCancelado2, fechaCancelar2;

    public Button btnAbrir, btnMenuP;

    public TextField txtCI, txtIDFactura, txtMontoORG, txtMontoTot;

    public Button btnAgregar, btnCancelar, btnMain;

    //----------------------------------\\

    private DBQueries dbQueries;
    private DeudaCabecera deudaCabecera;
    private List<DeudaCabecera> deudaCabeceraList;
    private List<DeudaDetalle> deudaDetalleList;
    private HashMap<Integer, DeudaCabecera> deudaCabeceraHashMap;

    public void initialize(){
        init();
    }

    private void init(){
        dbQueries = new DBQueries();
        deudaCabeceraHashMap = dbQueries.deudaCabeceraHashMap();

        deudaCabeceraList = new ArrayList<>(deudaCabeceraHashMap.values());
        prepararTabla1();
        prepararTabla2();
    }

    private void prepararTabla1(){
        ci1.setCellValueFactory(new PropertyValueFactory("ruc"));
        idFactura1.setCellValueFactory(new PropertyValueFactory("id_factura"));
        montoCancelar1.setCellValueFactory(new PropertyValueFactory("monto_original"));
        montoCancelado1.setCellValueFactory(new PropertyValueFactory("monto_cancelado"));
        estado1.setCellValueFactory(new PropertyValueFactory("estado"));
    }

    private void prepararTabla2(){
        ci2.setCellValueFactory(new PropertyValueFactory("id"));
        idFactura2.setCellValueFactory(new PropertyValueFactory("deuda_id"));
        montoCancelar2.setCellValueFactory(new PropertyValueFactory("monto_cancelado"));
        montoCancelado2.setCellValueFactory(new PropertyValueFactory("fecha"));
    }

    public void abrirDeuda(){
        deudaCabecera = null;
        deudaCabecera = tblDeudas.getSelectionModel().getSelectedItem();
        if(deudaCabecera != null)
            cargar(deudaCabecera.getId());
    }

    private void cargar(int id){
        deudaDetalleList = dbQueries.deudaDetalleList(id);
    }

    private void cargarDeudaDetalle(){

    }

    public void agregarPago(){

    }

    public void cancelarDeuda(){

    }



    public void menuP(){

    }

}
