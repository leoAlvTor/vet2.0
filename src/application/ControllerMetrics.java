package application;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControllerMetrics {
	
	@FXML
	Button btnFacVentas, btnFacCompras, btnReporte;
	
	public void initialize() {
	}

	
	public void facturasVenta() throws IOException {
		getStage(btnFacVentas, "Facturas de Venta", "FacturasVenta.fxml", 600, 500);
	}
	
	public void facturasCompra() throws IOException {
		getStage(btnFacVentas, "Facturas de Compra", "FacturasCompras.fxml", 600, 500);
	}
	
	public void reporte() throws IOException {
		getStage(btnFacVentas, "Reporte", "facturasVenta.fxml", 600, 500);
	}
	
    private void getStage(Button btn, String tittle, String file_name, double width, double height) throws java.io.IOException {
        Stage stage = (Stage) btnReporte.getScene().getWindow();
        stage.setTitle(tittle);
        File file = new File(file_name);
        Parent root = FXMLLoader.load(this.getClass().getResource(file.getPath()));
        stage.setScene(new Scene(root, width, height));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }
	
	
	
}
