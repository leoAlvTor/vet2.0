package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ControllerInvoiceMenu {

    @FXML
    Button btnFacturacion, btnMenuProd, btnDeudas, btnMetricas;
    
    Stage stage;

    public void abrirFacturacion(){
            try {
				getStage(btnFacturacion, "NUEVA FACTURA", "Invoice.fxml", 960, 800);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }

    public void abrirMenuProductos(){
        try {
            getStage(btnFacturacion, "MENU PRODUCTOS", "ProductMenu.fxml", 600, 400);
        } catch (IOException e) {
            System.out.println("Error al abrir el modulo Productos");
        }
    }

    public void abrirDeudas(){
        try {
            getStage(btnFacturacion, "DEUDAS", "Debts.fxml", 600, 400);
        } catch (IOException e) {
            System.out.println("Error al abrir el modulo Deudas");
        }
    }

    public void abrirMetricas(){
        try {
            getStage(btnFacturacion, "REPORTES Y METRICAS", "Metrics.fxml", 960, 800);
        } catch (IOException e) {
            System.out.println("Error al abrir el modulo METRICAS");
        }
    }

    @FXML
    private void getStage( Button btn, String tittle, String file_name, double width, double height) throws java.io.IOException {
    	stage = (Stage) btn.getScene().getWindow();
        stage.setTitle(tittle);
        File file = new File(file_name);
        Parent root = FXMLLoader.load(this.getClass().getResource(file.getPath()));
        stage.setScene(new Scene(root, width, height));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

}
