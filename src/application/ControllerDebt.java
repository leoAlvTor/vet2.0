package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ControllerDebt {

    @FXML
    TextField txtDebCI, txtDebIDFac, txtDebMontoORG, txtDebMontoTot, txtDebValorCancel;

    @FXML
    DatePicker dateDebCancel;

    @FXML
    Button btnDebAgregar, btnDebEliminar, btnDebMain;

    @FXML
    TableView tblDebDeudas;

    public void initialize(){

    }

}
