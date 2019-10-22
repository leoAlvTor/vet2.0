package application;

import application.resources.controller.DBDelete;
import application.resources.controller.DBInserts;
import application.resources.controller.DBQueries;
import application.resources.controller.DBUpdates;
import application.resources.model.Product;
import application.resources.model.Provider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class ControllerNewProduct {

    @FXML
    MenuItem mPCrear, mPBuscar, mPActualizar, mPEliminar,
        mELimpiar, mESalir;

    @FXML
    DatePicker fechaProd;

    @FXML
    TextField txtCodProd, txtCodFac, txtNomProd, txtNomFac, txtCantidadInt, txtStock, txtPrecioComCaja, txtPrecioComUnidad,
            txtPrecioCajaImp, txtPrecioUnidadImp, txtPrecioVCaja, txtPrecioVUnidad;

    @FXML
    ComboBox<String> comboProveedor, comboCategoria, comboTipoRep;

    @FXML
    RadioButton radioIVA12, radioIVA0;

    private List<Provider> providerList = new ArrayList<>();
    private List<String> providerName = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();
    private HashMap<String, String> mapaProveedores;
    private HashMap<String, String> mapaRUCProveedores = new HashMap<>();
    private HashMap<String, Product> productHashMap = new HashMap<>();
    private String prod_id;
    public String proveedorID;

    private double valorCajaIVA, valorUnidadIVA;


    private DBQueries dbQueries;

    public void initialize(){
        Thread thread1 = new Thread(this::loadCombos);
        thread1.start();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        radioIVA12.setOnAction(event -> {
            radioIVA0.setSelected(false);
            txtPrecioVCaja.setText(txtPrecioCajaImp.getText());
            txtPrecioVUnidad.setText(txtPrecioUnidadImp.getText());

            valorCajaIVA = Double.parseDouble(txtPrecioVCaja.getText());
            valorCajaIVA -= (valorCajaIVA*0.12);
            valorUnidadIVA = Double.parseDouble(txtPrecioVUnidad.getText());
            valorUnidadIVA -= (valorUnidadIVA*0.12);

            txtPrecioCajaImp.setText(String.valueOf(valorCajaIVA));
            txtPrecioUnidadImp.setText(String.valueOf(valorUnidadIVA));

        });

        radioIVA0.setOnAction(event -> {
            radioIVA12.setSelected(false);

            txtPrecioVCaja.setText(txtPrecioCajaImp.getText());
            txtPrecioVUnidad.setText(txtPrecioUnidadImp.getText());

            valorCajaIVA = Double.parseDouble(txtPrecioVCaja.getText());
            valorUnidadIVA = Double.parseDouble(txtPrecioVUnidad.getText());
        });

    }

    private void loadCombos(){
        dbQueries = new DBQueries();
        getProducts();
        getProviders();

        onlyNumbers(txtCantidadInt);
        onlyNumbers(txtStock);
        onlyNumbers(txtPrecioComCaja);
        onlyNumbers(txtPrecioComUnidad);
        onlyNumbers(txtPrecioCajaImp);
        onlyNumbers(txtPrecioUnidadImp);
        // COMBO CATEGORIAS
        List<String> categorias = new ArrayList<>();
        categorias.add("Golosinas");
        categorias.add("Granos");
        categorias.add("Medicamentos");
        categorias.add("Bebidas");
        categorias.add("OTROS");
        categorias.add("Hormonas");
        categorias.add("Semillas");
        categorias.add("Pestisidas");
        categorias.add("Insecticidas");
        comboCategoria.setItems(FXCollections.observableArrayList(categorias));

        // COMBO TIPO PRESENTACION
        List<String> presentaciones = new ArrayList<>();
        presentaciones.add("EMPAQUE");
        presentaciones.add("UNIDAD");
        comboTipoRep.setItems(FXCollections.observableArrayList(presentaciones));
        EventHandler<ActionEvent> eventHandler = event -> {
            if(comboTipoRep.getValue().equals("UNIDAD")){
                
            	txtCantidadInt.setText("1");
                txtCantidadInt.setEditable(false);
                
                txtPrecioComCaja.setText("0");
                txtPrecioComCaja.setEditable(false);
                
                txtPrecioVCaja.setText("0");
                txtPrecioVCaja.setEditable(false);
                
                txtPrecioCajaImp.setText("0");
                txtPrecioCajaImp.setEditable(true);
                
            }else if(comboTipoRep.getValue().equals("EMPAQUE")){
                txtCantidadInt.setEditable(true);
                txtPrecioComCaja.setEditable(true);
                txtPrecioVCaja.setEditable(true);
            }
        };
        comboTipoRep.setOnAction(eventHandler);

        mapaProveedores = new HashMap<>();
        for(Provider p : providerList) {
            mapaProveedores.put(p.getNombre(), p.getRuc());
            mapaRUCProveedores.put(p.getRuc(), p.getNombre());
            providerName.add(p.getNombre());
        }
        ObservableList<String> listaProv = FXCollections.observableArrayList(providerName);
        Collections.sort(listaProv);
        comboProveedor.setItems(FXCollections.observableArrayList(listaProv));
    }

    private void getProducts(){
        productList = dbQueries.getProducts();
        for(Product p : productList)
            productHashMap.put(p.getId(), p);
    }

    private void getProviders(){
        providerList = dbQueries.getProviders();
    }

    private void onlyNumbers( TextField txt){
        txt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("[a-z]*") || newValue.matches("[A-Z]")) {
                txt.setText(newValue.replaceAll("[a-zA-z]*", ""));
            }
        });


    }

    public void crearProducto() {
        DBInserts dbInserts = new DBInserts();
        proveedorID = mapaProveedores.get(comboProveedor.getValue());
        if (radioIVA12.isSelected()) {
            dbInserts.insertProduct(txtCodProd.getText(), txtCodFac.getText(), txtNomProd.getText(), txtNomFac.getText(),
                    fechaProd.getValue().toString(), proveedorID, comboCategoria.getValue().toString(), comboTipoRep.getValue().toString(),
                    Integer.parseInt(txtCantidadInt.getText()), Integer.parseInt(txtStock.getText()),
                    Double.parseDouble(txtPrecioComCaja.getText()), Double.parseDouble(txtPrecioComUnidad.getText()), "12",
                    Double.parseDouble(txtPrecioCajaImp.getText()), Double.parseDouble(txtPrecioUnidadImp.getText()),
                    Double.parseDouble(txtPrecioVCaja.getText()), Double.parseDouble(txtPrecioVUnidad.getText()));

        }else if(radioIVA0.isSelected()){

            int backInt = dbInserts.insertProduct(txtCodProd.getText(), txtCodFac.getText(), txtNomProd.getText(), txtNomFac.getText(),
                    fechaProd.getValue().toString(), proveedorID, comboCategoria.getValue().toString(), comboTipoRep.getValue().toString(),
                    Integer.parseInt(txtCantidadInt.getText()), Integer.parseInt(txtStock.getText()),
                    Double.parseDouble(txtPrecioComCaja.getText()), Double.parseDouble(txtPrecioComUnidad.getText()), "0",
                    Double.parseDouble(txtPrecioCajaImp.getText()), Double.parseDouble(txtPrecioUnidadImp.getText()),
                    Double.parseDouble(txtPrecioVCaja.getText()), Double.parseDouble(txtPrecioVUnidad.getText()));
            if(backInt == 1){
                alertDuplicate();
            }else if(backInt == 2){
                alertBadEntry();
            }else if(backInt == 0){
                alertOK();
            }
        }
        limpiar();
    }

    public void buscarProducto(){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Producto");
        dialog.setHeaderText("Buscar Producto");
        dialog.setContentText("Ingrese el codigo principal del producto:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> compareProduct(name));
    }

    private void compareProduct(String id){
        getProducts();
        Product product;
        if(productHashMap.containsKey(id)) {
            product = productHashMap.get(id);
            txtCodProd.setText(product.getId());
            prod_id = product.getId();
            txtCodFac.setText(product.getMain_id());
            txtNomProd.setText(product.getName());
            txtNomFac.setText(product.getMain_name());
            fechaProd.setUserData(product.getFecha());
            comboProveedor.setValue(mapaRUCProveedores.get(product.getProv_RUC()));
            comboCategoria.setValue(product.getCategory());
            comboTipoRep.setValue(product.getRepresentation());
            txtCantidadInt.setText(String.valueOf(product.getInternal_quantity()));
            txtStock.setText(String.valueOf(product.getStock()));
            txtPrecioComCaja.setText(String.valueOf(product.getBox_price()));
            txtPrecioComUnidad.setText(String.valueOf(product.getUnit_price()));
            if(product.getTax().equals("12"))
                radioIVA12.setSelected(true);
            else if(product.getTax().equals("10"))
                radioIVA0.setSelected(true);
            txtPrecioUnidadImp.setText("");
            txtPrecioVCaja.setText("");
            txtPrecioVUnidad.setText("");

            txtPrecioCajaImp.setText(String.valueOf(product.getBox_12()));
            txtPrecioUnidadImp.setText(String.valueOf(product.getUnit_12()));
            txtPrecioVCaja.setText(String.valueOf(product.getSell_box()));
            txtPrecioVUnidad.setText(String.valueOf(product.getSell_unit()));
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error de busqueda");
            alert.setContentText("No existe el producto buscado, asegurese de que el codigo ingresado sea el correcto.");
            alert.showAndWait();
        }
    }

    public void actualizarProducto(){
        DBUpdates dbUpdates = new DBUpdates();
        String proveedorID = mapaProveedores.get(comboProveedor.getValue());
        if (radioIVA12.isSelected()) {
            dbUpdates.updateProduct(prod_id ,txtCodProd.getText(), txtCodFac.getText(), txtNomProd.getText(), txtNomFac.getText(),
                    fechaProd.getValue().toString(), proveedorID, comboCategoria.getValue().toString(), comboTipoRep.getValue().toString(),
                    Integer.parseInt(txtCantidadInt.getText()), Integer.parseInt(txtStock.getText()),
                    Double.parseDouble(txtPrecioComCaja.getText()), Double.parseDouble(txtPrecioComUnidad.getText()), "12",
                    Double.parseDouble(txtPrecioCajaImp.getText()), Double.parseDouble(txtPrecioUnidadImp.getText()),
                    Double.parseDouble(txtPrecioVCaja.getText()), Double.parseDouble(txtPrecioVUnidad.getText()));

        }else if(radioIVA0.isSelected()){
            dbUpdates.updateProduct(prod_id, txtCodProd.getText(), txtCodFac.getText(), txtNomProd.getText(), txtNomFac.getText(),
                    fechaProd.getValue().toString(), proveedorID, comboCategoria.getValue().toString(), comboTipoRep.getValue().toString(),
                    Integer.parseInt(txtCantidadInt.getText()), Integer.parseInt(txtStock.getText()),
                    Double.parseDouble(txtPrecioComCaja.getText()), Double.parseDouble(txtPrecioComUnidad.getText()), "0",
                    Double.parseDouble(txtPrecioCajaImp.getText()), Double.parseDouble(txtPrecioUnidadImp.getText()),
                    Double.parseDouble(txtPrecioVCaja.getText()), Double.parseDouble(txtPrecioVUnidad.getText()));
        }
    }

    public void eliminarProducto(){
        DBDelete dbDelete = new DBDelete();
        dbDelete.deleteProduct(txtCodProd.getText());
    }

    public void limpiar(){
        txtCodProd.setText("");
        txtCodFac.setText("");
        txtNomProd.setText("");
        txtNomFac.setText("");
        txtCantidadInt.setText("");
        txtStock.setText("");
        txtPrecioComCaja.setText("");
        txtPrecioComUnidad.setText("");
        txtPrecioCajaImp.setText("");
        txtPrecioUnidadImp.setText("");
        txtPrecioVCaja.setText("");
        txtPrecioVUnidad.setText("");
        comboTipoRep.setValue("");
        comboCategoria.setValue("");
        comboProveedor.setValue("");
        radioIVA0.setSelected(false);
        radioIVA12.setSelected(false);
    }

    public void salir() throws IOException {
        Stage stage = (Stage) txtPrecioCajaImp.getScene().getWindow();
        stage.setTitle("Menu Principal");
        Parent root = FXMLLoader.load(this.getClass().getResource("MainMenu.fxml"));
        stage.setScene(new Scene(root, 650, 490));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    public void alertDuplicate(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("Error de INGRESO");
        alert.setContentText("Esta intentando ingresar un valor cuyo Codigo ya existe.");
        alert.showAndWait();
    }

    public void alertBadEntry(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("Error de INGRESO");
        alert.setContentText("Por favor revise que los campos no esten vacios o los datos no sean los solicitados.");
        alert.showAndWait();
    }

    public void alertOK(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("EXITO");
        alert.setHeaderText("Ingreso Correcto");
        alert.setContentText("Se ha ingresado el producto correctamente.");
        alert.show();
    }


}
