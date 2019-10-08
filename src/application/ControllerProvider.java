package application;

import application.resources.controller.DBDelete;
import application.resources.controller.DBInserts;
import application.resources.controller.DBQueries;
import application.resources.controller.DBUpdates;
import application.resources.model.Provider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public class ControllerProvider {

    @FXML
    TextField txtRUC, txtNombre, txtDireccion, txtTelefono, txtEmail;

    @FXML
    TextArea areaObservaciones;

    @FXML
    TableView<Provider> tblProvedores;

    @FXML
    Button btnGuardar, btnActualizar, btnEliminar, btnListar, btnBuscar, btnMenu;

    @FXML
    TableColumn colRUC, colNombre, colDireccion, colTelefono, colEmail;

    private List<Provider> lstProviders;
    private String proveedorRUC;
    private ObservableList<Provider> providerObservableList;
    private Provider provider;

    public void initialize(){
        provider = new Provider();
        numberOnlyRUC();
        numberOnlyPhone();
        getProviders();
        listerTable();
    }

    private void numberOnlyRUC(){
        txtRUC.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || txtRUC.getText().length()>13) {
                txtRUC.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void numberOnlyPhone(){
        txtTelefono.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || txtRUC.getText().length()>13) {
                txtTelefono.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void listerTable(){
        providerObservableList = FXCollections.observableArrayList(lstProviders);
        tblProvedores.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(tblProvedores.getSelectionModel().getSelectedItem() != null){
                provider = tblProvedores.getSelectionModel().getSelectedItem();
                txtRUC.setText(provider.getRuc());
                proveedorRUC = provider.getRuc();
                txtNombre.setText(provider.getNombre());
                txtDireccion.setText(provider.getDireccion());
                txtTelefono.setText(provider.getTelefono());
                txtEmail.setText(provider.getEmail());
                areaObservaciones.setText(provider.getObservaciones());
            }
        });

    }

    private boolean txtLenght(){
        int value = txtRUC.getText().length();
        return value == 13;
    }

    private boolean voidText(){
        return txtDireccion.getText().equals("") || txtTelefono.getText().equals("") || txtNombre.getText().equals("") ||
                txtEmail.getText().equals("") || areaObservaciones.getText().equals("");
    }

    public void setProvider() {
        if (txtLenght()) {
            if(!voidText()) {
                DBInserts dbInserts = new DBInserts();
                dbInserts.insertProvider(txtRUC.getText(), txtNombre.getText(), txtDireccion.getText(), txtTelefono.getText(),
                        txtEmail.getText(), areaObservaciones.getText());
            }else{
                alertVoid();
            }
        }else{
            alertRUC();
        }


    }

    private void alertVoid(){
        Alert alert = new Alert(Alert.AlertType.ERROR,"EXISTE VALORES VACIOS");
        alert.show();
    }

    private void alertRUC(){
        Alert alert = new Alert(Alert.AlertType.ERROR,"EL RUC NO CUMPLE CON LOS 13 CARACTERES");
        alert.show();
        txtRUC.requestFocus();
        txtRUC.selectAll();
    }

    private void getProviders(){
        DBQueries dbQueries = new DBQueries();
        lstProviders = dbQueries.getProviders();
    }

    public void updateProvider(){
        if(txtLenght()){
            if(!voidText()){
                DBUpdates dbUpdates = new DBUpdates();
                dbUpdates.updateProvider(proveedorRUC,txtRUC.getText(),txtNombre.getText(),txtDireccion.getText(),
                        txtTelefono.getText(),txtEmail.getText(),areaObservaciones.getText());
            }else
                alertVoid();
        }else
            alertVoid();
    }

    public void deleteProvider(){
        Alert alert;
        if(txtLenght()){
            DBDelete dbDelete = new DBDelete();
            if(dbDelete.deleteProvider(txtRUC.getText())) {
                alert = new Alert(Alert.AlertType.INFORMATION, "Registro eliminado correctamente...");
                alert.show();
            }else{
                alert = new Alert(Alert.AlertType.ERROR, "No se pudo eliminar el registro...");
                alert.show();
            }
        }else
            alertRUC();
    }

    public void showProviders(){
        colRUC.setCellValueFactory(new PropertyValueFactory("ruc"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        tblProvedores.setItems(providerObservableList);
    }

    public void searchProvider(){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Proveedores");
        dialog.setHeaderText("Buscar Proveedor");
        dialog.setContentText("Ingrese el RUC:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::compareRUC);
    }

    private void compareRUC(String name){
        Alert alert;
        boolean bool = false;
        for(Provider p : lstProviders)
            if(p.getRuc().equals(name)){
                txtRUC.setText(p.getRuc());
                txtRUC.setText(p.getRuc());
                txtNombre.setText(p.getNombre());
                txtDireccion.setText(p.getDireccion());
                txtTelefono.setText(p.getTelefono());
                txtEmail.setText(p.getEmail());
                areaObservaciones.setText(p.getObservaciones());
                bool = true;
            }
            if(!bool){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("No se ha encontrado el RUC solicitado");
                    alert.showAndWait();
            }
    }

    public void mainMenu() throws Exception{
        Stage stage = (Stage) btnActualizar.getScene().getWindow();
        stage.setTitle("Menu Principal");
        Parent root = FXMLLoader.load(this.getClass().getResource("MainMenu.fxml"));
        stage.setScene(new Scene(root, 650, 490));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }
}
