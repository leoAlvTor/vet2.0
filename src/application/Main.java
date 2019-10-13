package application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import application.resources.controller.DBQueries;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Main extends Application {

	private Controller controller = new Controller();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(this.getClass().getResource("MainMenu.fxml"));
        primaryStage.setTitle("Mundo Ganadero");
        primaryStage.setScene(new Scene(root, 650, 490));

    	Dialog dialogo = new Dialog();
		GridPane gridPane = new GridPane();
		Label user = new Label("Usuario:  ");
		TextField txtUser = new TextField();

		Label contra = new Label("Contraseña:  ");
		PasswordField passwordField = new PasswordField();

		gridPane.add(user, 0, 0);
		gridPane.add(txtUser, 1,0);

		gridPane.add(contra, 0,1);
		gridPane.add(passwordField, 1, 1);

		dialogo.getDialogPane().setContent(gridPane);
		dialogo.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialogo.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

		dialogo.showAndWait();

		if(dialogo.getResult().equals(ButtonType.OK)) {
			if (checkUser(txtUser.getText(), passwordField.getText()))
				abrirMenu(primaryStage);
			else{
				alerta();
				System.exit(0);
			}
		}else if(dialogo.getResult().equals(ButtonType.NO))
    			System.exit(0);

    }

    public static void main(String[] args) throws Exception {
    	launch(args);
    }

    private boolean checkUser(String user, String pass) throws Exception{
    	if(user.equals("Usuario1") && pass.equals("123")) {
			controller.escribirArchivo(user);
			return true;
		}else if(user.equals("Usuario2") && pass.equals("123")) {
			controller.escribirArchivo(user);
			return true;
		}else
    		return false;
	}

	private void alerta(){
    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
    	alert.setTitle("Error al logeo");
    	alert.setHeaderText("Usuario o Password Incorrectos");
    	alert.setContentText("El usuario ingresado no existe o no este registrado con esa contraseña");
    	alert.showAndWait();
	}

	private void abrirMenu(Stage primaryStage){
		DBQueries dbQueries = new DBQueries();
		HashMap<String, String> values =dbQueries.getVencimientos();
		Dialog dialog = new Dialog();
		dialog.setTitle("Productos");
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
		TextArea txtArea = new TextArea();
		txtArea.setStyle("-fx-text-fill: red ; -fx-font-size: 12pt");
		txtArea.setEditable(false);
		for(Entry<String, String> entry : values.entrySet())
			txtArea.setText(txtArea.getText() + "\n" + entry.getKey()+entry.getValue());
		dialog.getDialogPane().setContent(txtArea);
		dialog.setX(0);
		dialog.setY(0);
		dialog.show();
		primaryStage.setX(dialog.getWidth());
		primaryStage.show();
	}

}
