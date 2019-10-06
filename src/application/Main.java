package application;

import java.util.HashMap;
import java.util.Map.Entry;

import application.resources.controller.DBQueries;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(this.getClass().getResource("MainMenu.fxml"));
        primaryStage.setTitle("Mundo Ganadero");
        primaryStage.setScene(new Scene(root, 650, 490));
        
        
        
        DBQueries dbQueries = new DBQueries();
    	HashMap<String, String> values =dbQueries.getVencimientos();
    	
    	Dialog<Pair<String, String>> dialog = new Dialog<>();
    	dialog.setTitle("Productos");
    	dialog.setHeaderText("Inventariado de productos");
    	
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

    public static void main(String[] args) {

    	launch(args);

    }

}
