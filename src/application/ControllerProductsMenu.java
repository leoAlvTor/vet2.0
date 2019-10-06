package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class ControllerProductsMenu {

    @FXML
    Button btnInventario, btnCompras, btnProveedor, btnProducto;

    @FXML
    public void openInventario() throws Exception{
        //System.out.println("NO EXISTE EL INVENTARIO AUN");
        getStage(btnCompras, "Inventario", "Inventory.fxml", 840,626);
    }

    @FXML
    public void openCompras() throws Exception{
        getStage(btnCompras, "Compras", "InvoicePurchases.fxml", 773,690);
    }

    @FXML
    public void openProveedor() throws Exception{
        getStage(btnProveedor, "Proveedor", "Provider.fxml", 505,564);
    }

    @FXML
    public void openProductos() throws Exception{
        getStage(btnProveedor, "Productos", "NewProduct.fxml", 693,620);
    }

    @FXML
    private void getStage( Button btn, String tittle, String file_name, double width, double height) throws java.io.IOException {
    	Stage stage = (Stage) btn.getScene().getWindow();
        stage.setTitle(tittle);
        File file = new File(file_name);
        Parent root = FXMLLoader.load(this.getClass().getResource(file.getPath()));
        stage.setScene(new Scene(root, width, height));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

}
