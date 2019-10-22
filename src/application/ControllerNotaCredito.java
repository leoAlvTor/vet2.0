package application;

import application.resources.controller.DBDelete;
import application.resources.controller.DBInserts;
import application.resources.controller.DBQueries;
import application.resources.controller.DBUpdates;
import application.resources.model.CabeceraCompra;
import application.resources.model.ModelDetailPurchase;
import application.resources.model.Product;
import application.resources.model.Provider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ControllerNotaCredito {

    @FXML
    MenuButton menuButton;
    @FXML
    TextField txtCodigo, txtNumCredito, txtAutorizacionFAC, txtNumeroFAC, txtRUC, txtNomProveedor, txtCantTiempo,
            txtPagoInicial, txtSubtotal12, txtSubtotal0, txtIVA, txtICE, txtIRBP, txtTotal;
    @FXML
    DatePicker dateFecha;
    @FXML
    ComboBox<String> comboFormaPago;
    @FXML
    Label lblCodigo;
    @FXML
    TableView<Product> tblBusqueda;
    @FXML
    TableView<ModelDetailPurchase> tblCompras;
    @FXML
    Button btnCalcular, btnGuardar, btnActualizar, btnEliminar, btnBuscarProveedor;
    @FXML
    TableColumn colCodigoFactura, colCodigoProducto, colNombreFactura, colNombreProducto, colPrecioUnitario, colPrecioCaja;
    @FXML
    TableColumn c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11;
    @FXML
    MenuItem opCodigoFactura, opCodigoProducto, opNombreFactura, opNombreProducto, opProductosProveedor;

    // Banderas para no "RECARGAR"
    private boolean cargaCodigoFactura, cargaCodigoProducto, cargaNombreFactura, cargaNombreProducto, cargaProveedores;
    private boolean noProveedor = true;
    private boolean flag;
    // Lista para almacenar todos los productos registrados
    private List<Product> productList, productosCompra = new ArrayList<>();
    // Lista para obtener todos los proveedores registrados
    private List<Provider> providerList = new ArrayList<>();
    // Mapa hash para la busqueda de un producto dependendiendo de la seleccion del usuario
    private HashMap<String, Product> productHashMap = new HashMap<>();
    // Mapa hash para la busqueda de un proveedor por medio de su Nombre
    private HashMap<String, Provider> providerHashMap = new HashMap<>();
    // Thread para ejecutar la carga de los datos sin afectar a la interfaz.
    private Thread thread;
    // Lista para obtener los mapaHash de cada proveedor con todos sus productos
    private List<HashMap<String, Product>> hashMapListProduct = new ArrayList<>();
    // Permite obtener los datos de la base
    private DBQueries dbQueries;
    // Lista para los datos de la tabla superior (Productos)
    private ObservableList<Product> productObservableList;
    private ObservableList<ModelDetailPurchase> compraObservableList;
    // Objecto para el control de la caja de texto con al autocompletado
    private AutoCompletionBinding<String> txtCodigos;
    // Lista de productos para ser agregados a la tabla de compras.
    private List<ModelDetailPurchase> invoiceProductList = new ArrayList<>();

    private ModelDetailPurchase model;

    private CabeceraCompra cabeceraCompra;

    @FXML
    public void initialize(){
        thread = new Thread(this::loadData);
        thread.start();
        loadComboFormaPago();
    }

    // Carga el combobox con sus dos formas de pago
    private void loadComboFormaPago(){
        List<String> forma = new ArrayList<>();
        forma.add("Efectivo");
        forma.add("Credito");
        comboFormaPago.setItems(FXCollections.observableArrayList(forma));
        txtCodigos  = TextFields.bindAutoCompletion(txtCodigo, "");

        txtICE.setText("0");
        txtIRBP.setText("0");
    }

    /* Carga de datos
     *  Carga los datos necesarios para el funcionamiento como:
     *  - Proveedores
     *  - Productos
     */
    private HashMap<String, String> mapaAutorizacionID;

    private void loadData(){
        mapaAutorizacionID = new HashMap<>();
        dbQueries = new DBQueries();

        mapaAutorizacionID = dbQueries.mapaAutorizacionIDFacturaNota();

        for(Map.Entry<String, String> e : mapaAutorizacionID.entrySet())
            System.out.println("Key : " + e.getKey() + " __ Value: " + e.getValue());

        productList = dbQueries.getProducts();
        providerList = dbQueries.getProviders();
        cabeceraCompra = new CabeceraCompra();
        eventoSeleccionTabla();

    }

    // Carga los productos por su codigo en la factura
    public void cargarCodigoFactura(){
        if(noProveedor){
            txtCodigos.dispose();
            txtCodigo.setOnKeyPressed(null);

            List<String> list;
            String[] lista;
            if(!cargaCodigoFactura){
                cargaCodigoFactura = true;
                list = new ArrayList<>();
                for(Product p: productList){
                    list.add(p.getMain_id());
                    productHashMap.put(p.getMain_id(), p);
                }
                lista = new String[list.size()];
                for (int i = 0; i < list.size(); i++)
                    lista[i] = list.get(i);
                txtCodigo.setOnKeyPressed(event -> {
                    if(event.getCode().equals(KeyCode.ENTER) && !txtCodigo.getText().equals("")){
                        Product p = productHashMap.get(txtCodigo.getText());
                        model = new ModelDetailPurchase();
                        if(p.getBox_price() == 0)
                            calculateUNIT(p);
                        else
                            calculateBOX(p);
                    }
                });

                txtCodigos = TextFields.bindAutoCompletion(txtCodigo, lista);

            }
        }
    }

    // Carga los productos por su codigo propio (Puesto por el administrador)
    public void cargarCodigoProducto(){
        txtCodigos.dispose();
        txtCodigo.setOnKeyPressed(null);

        List<String> list;
        String[] lista;
        if(!cargaCodigoProducto){
            cargaCodigoProducto = true;
            list = new ArrayList<>();
            for(Product p: productList){
                list.add(p.getId());
                productHashMap.put(p.getId(), p);
            }
            lista = new String[list.size()];
            for (int i = 0; i < list.size(); i++)
                lista[i] = list.get(i);
            txtCodigo.setOnKeyPressed(event -> {
                if(event.getCode().equals(KeyCode.ENTER) && !txtCodigo.getText().equals("")){
                    Product p = productHashMap.get(txtCodigo.getText());
                    model = new ModelDetailPurchase();
                    if(p.getBox_price() == 0)
                        calculateUNIT(p);
                    else
                        calculateBOX(p);
                }
            });
            txtCodigos = TextFields.bindAutoCompletion(txtCodigo, lista);
        }
    }

    // Carga los productos por su nombre en la factura
    public void cargarNombreFactura(){

        txtCodigos.dispose();
        txtCodigo.setOnKeyPressed(null);

        List<String> list;
        String[] lista;
        if(!cargaNombreFactura){
            cargaNombreFactura = true;
            list = new ArrayList<>();
            for(Product p: productList){
                list.add(p.getMain_name());
                productHashMap.put(p.getMain_name(), p);
            }
            lista = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                lista[i] = list.get(i);
            }
            txtCodigo.setOnKeyPressed(event -> {
                if(event.getCode().equals(KeyCode.ENTER) && !txtCodigo.getText().equals("")){
                    Product p = productHashMap.get(txtCodigo.getText());
                    model = new ModelDetailPurchase();
                    if(p.getBox_price() == 0)
                        calculateUNIT(p);
                    else
                        calculateBOX(p);
                }
            });
            txtCodigos = TextFields.bindAutoCompletion(txtCodigo, lista);
        }
    }

    // Carga los productos por su nombre propio (Puestro por el administrador)
    public void cargarNombreProducto(){
        txtCodigos.dispose();
        txtCodigo.setOnKeyPressed(null);
        List<String> list;
        String[] lista;
        if(!cargaNombreProducto){
            cargaNombreProducto = true;
            list = new ArrayList<>();
            for(Product p: productList){
                list.add(p.getName());
                productHashMap.put(p.getName(), p);
            }
            lista = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                lista[i] = list.get(i);
            }
            txtCodigo.setOnKeyPressed(event -> {
                if(event.getCode().equals(KeyCode.ENTER) && !txtCodigo.getText().equals("")){
                    Product p = productHashMap.get(txtCodigo.getText());
                    model = new ModelDetailPurchase();
                    if(p.getBox_price() == 0)
                        calculateUNIT(p);
                    else
                        calculateBOX(p);
                }
            });
            txtCodigos = TextFields.bindAutoCompletion(txtCodigo, lista);
        }
    }

    /* Carga los datos de los proveedores
     *  Permite la carga de los proveedores para el autocompletado cuando el usuario ingrese
     *  el nombre o una parte del nombre del mismo.
     *  Se ejecuta solo cuando el hilo haya terminado para que todos los datos esten cargados.
     * ----- BOTON PROVEEDORES -----
     */
    public void cargaProveedores(){
        if(!thread.isAlive()){
            int cantidad = providerList.size();
            String[] nombreProveedores = new String[cantidad];
            for (int i = 0; i < cantidad; i++) {
                String value = providerList.get(i).getNombre();
                nombreProveedores[i] = value;
                providerHashMap.put(value, providerList.get(i));
            }
            TextField txtBuscarProv = new TextField();
            TextFields.bindAutoCompletion(txtBuscarProv, nombreProveedores);

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Proveedor");
            dialog.setHeaderText("Ingrese el nombre del proveedor, se listaran \nautomaticamente los resultados con ese nombre:");
            Label label1 = new Label("Proveedor: ");

            GridPane grid = new GridPane();
            grid.add(label1, 1, 1);
            grid.add(txtBuscarProv, 2, 1);
            dialog.getDialogPane().setContent(grid);

            ButtonType buttonTypeOk = new ButtonType("Cargar Datos", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

            dialog.setResultConverter(b -> {
                if (b == buttonTypeOk) return txtBuscarProv.getText();
                return null;
            });
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(this::cargarProveedor);
        }
    }

    private void alertaProveedor(String paramNombre, String paramDireccion, String paramObs){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Proveedor");
        alert.setHeaderText("Datos del proveedor seleccionado...");
        alert.setContentText("Nombre: " + paramNombre + "\n Direccion: " + paramDireccion + "\n Observaciones: " + paramObs);
        alert.showAndWait();
    }

    private void cargarProveedor(String provName){
        Provider provider = providerHashMap.get(provName);
        alertaProveedor(provider.getNombre(), provider.getDireccion(), provider.getObservaciones());
        txtRUC.setText(provider.getRuc());
        txtNomProveedor.setText(provName);
    }

    /* 1. Carga los proveedores a la caja de texto para el autocompletado.
     * 2. Carga los productos asociados a dicho proveedor en la tabla superior.
     */
    public void cargaPorProveedor(/*ActionEvent actionEvent*/){
        txtCodigos.dispose();
        List<HashMap<String, List<Product>>> ultraHashMap = new ArrayList<>();
        if(!cargaProveedores){
            dbQueries.connect();
            String[] arrayProvider = new String[providerList.size()];
            int size = providerList.size();
            for (int i = 0; i < size; i++) {
                arrayProvider[i] = providerList.get(i).getNombre();
                ultraHashMap.add(dbQueries.productsHashMap(providerList.get(i).getNombre()));
            }
            dbQueries.disconnect();
            txtCodigos = TextFields.bindAutoCompletion(txtCodigo, arrayProvider);
            cargaProveedores = true;
            txtCodigo.setOnKeyPressed(event -> {
                if(event.getCode().equals(KeyCode.ENTER)){
                    for (int i = 0; i < ultraHashMap.size(); i++) {
                        if(ultraHashMap.get(i).containsKey(txtCodigo.getText())) {

                            listerTable(ultraHashMap.get(i).get(txtCodigo.getText()));
                        }
                    }
                }
            });
        }
    }

    private void listerTable(List<Product> paramProducts){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItemAgregar = new MenuItem("Agregar producto");
        contextMenu.getItems().add(menuItemAgregar);
        tblBusqueda.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            contextMenu.show(tblBusqueda, event.getScreenX(), event.getScreenY());
        });
        menuItemAgregar.setOnAction(event -> {
            if(tblBusqueda.getSelectionModel().getSelectedItem() != null){
                Product p = tblBusqueda.getSelectionModel().getSelectedItem();
                model = new ModelDetailPurchase();
                if(p.getBox_price() == 0)
                    calculateUNIT(p);
                else
                    calculateBOX(p);
            }
        });
        productObservableList = FXCollections.observableArrayList(paramProducts);
        showProducts();
    }

    private void showProducts(){
        colCodigoFactura.setCellValueFactory(new PropertyValueFactory("main_id"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory("id"));
        colNombreFactura.setCellValueFactory(new PropertyValueFactory("main_name"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory("name"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory("unit_price"));
        colPrecioCaja.setCellValueFactory(new PropertyValueFactory("box_price"));
        tblBusqueda.setItems(productObservableList);
    }

    private void loadCompra(List<ModelDetailPurchase> paramProducts){
        compraObservableList = FXCollections.observableList(paramProducts);
        c1.setCellValueFactory(new PropertyValueFactory("codigo"));
        c2.setCellValueFactory(new PropertyValueFactory("descripcion"));
        c3.setCellValueFactory(new PropertyValueFactory("cantidad"));
        c4.setCellValueFactory(new PropertyValueFactory("p_unit"));
        c5.setCellValueFactory(new PropertyValueFactory("v_total"));
        c6.setCellValueFactory(new PropertyValueFactory("pvp_unit"));
        c7.setCellValueFactory(new PropertyValueFactory("tarifa"));
        c8.setCellValueFactory(new PropertyValueFactory("p_caja"));
        c9.setCellValueFactory(new PropertyValueFactory("v_total_caja"));
        c10.setCellValueFactory(new PropertyValueFactory("pvp_caja"));
        c11.setCellValueFactory(new PropertyValueFactory("fecha_vencimiento"));
        tblCompras.setItems(compraObservableList);
    }

    private int dialogCantidad(String paramProducto){
        TextInputDialog dialog = new TextInputDialog("CANTIDAD");
        dialog.setTitle("Compras");
        dialog.setHeaderText("Producto seleccionado -> " + paramProducto);
        dialog.setContentText("Ingrese la cantidad del producto:");
        Optional<String> resultado = dialog.showAndWait();
        return resultado.map(Integer::parseInt).orElse(0);
    }


    private void calculateBOX(Product p){
        model.setCodigo(p.getId());
        model.setDescripcion(p.getName());
        model.setCantidad(dialogCantidad(p.getName()));
        model.setP_unit(p.getUnit_price());
        model.setV_total(p.getUnit_price());
        model.setPvp_unit(p.getSell_unit());
        model.setTarifa(p.getTax());
        model.setP_caja(p.getBox_price());
        model.setV_total_caja(model.getCantidad()*model.getP_caja());
        model.setFecha_vencimiento(p.getFecha());
        invoiceProductList.add(model);
        loadCompra(invoiceProductList);
    }

    private void calculateUNIT(Product p){
        model.setCodigo(p.getId());
        model.setDescripcion(p.getName());
        model.setCantidad(dialogCantidad(p.getName()));
        model.setP_unit(p.getUnit_price());
        model.setV_total(model.getCantidad()*model.getP_unit());
        model.setPvp_unit(p.getSell_unit());
        model.setTarifa(p.getTax());
        model.setP_caja(p.getBox_price());
        model.setV_total_caja(0);
        model.setFecha_vencimiento(p.getFecha());
        invoiceProductList.add(model);
        loadCompra(invoiceProductList);
    }

    public void calculoFinal(){
        double subtotal0 = 0;
        double subtotal12 = 0;
        double totalIVA = 0;
        double ICE = Double.parseDouble(txtICE.getText());
        double IRBP = Double.parseDouble(txtIRBP.getText());
        double total = 0;
        for(ModelDetailPurchase m : tblCompras.getItems()) {
            if (m.getP_caja() == 0) {
                subtotal12 += m.getV_total() - (m.getV_total() * 0.12);
                if(m.getTarifa().equals("0"))
                    subtotal0 += m.getV_total();
                totalIVA += m.getV_total() * 0.12;
            } else if(m.getP_caja() != 0){
                subtotal12 += m.getV_total_caja()-(m.getV_total_caja() * 0.12);
                if(m.getTarifa().equals("0"))
                    subtotal0 += m.getV_total_caja();
                totalIVA += m.getV_total_caja() * 0.12;

            }
        }
        total += subtotal0 + subtotal12 + totalIVA;
        total += ICE + IRBP;

        txtSubtotal0.setText(String.valueOf(subtotal0));
        txtSubtotal12.setText(String.valueOf(subtotal12));
        txtIVA.setText(String.valueOf(totalIVA));
        txtTotal.setText(String.valueOf(total));
    }


    private void eventoSeleccionTabla() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem itemEditar = new MenuItem("Editar producto");
        contextMenu.getItems().add(itemEditar);
        tblCompras.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
            contextMenu.show(tblCompras, event.getScreenX(), event.getScreenY());
        });
        itemEditar.setOnAction(event ->{
            ModelDetailPurchase modeloVenta = tblCompras.getSelectionModel().getSelectedItem();
            String codigo = tblCompras.getSelectionModel().getSelectedItem().getCodigo();
            if(modeloVenta != null) {
                modeloVenta = cambios(modeloVenta);
                for(int i=0; i < invoiceProductList.size(); i++) {
                    if(invoiceProductList.get(i).getCodigo().equals(codigo)) {
                        invoiceProductList.remove(i);
                        invoiceProductList.add(modeloVenta);
                    }
                }
                compraObservableList = FXCollections.observableArrayList(invoiceProductList);
                tblCompras.setItems(compraObservableList);
                for(int i = 0; i < tblCompras.getItems().size(); i++) {
                    tblCompras.getColumns().get(i).setVisible(false);
                    tblCompras.getColumns().get(i).setVisible(true);
                }

            }
        });
    }

    private ModelDetailPurchase cambios(ModelDetailPurchase value) {
        ModelDetailPurchase modelo = value;
        modelo.setCantidad(Integer.parseInt(input("Cantidad: ", value.getCantidad())));
        modelo.setFecha_vencimiento(inputFecha(value.getFecha_vencimiento()));
        if(modelo.getP_caja() == 0) {
            modelo.setP_unit(Double.parseDouble(input("Precio Unitario: ", value.getP_unit())));
            modelo.setV_total(modelo.getCantidad()*modelo.getP_unit());
        }else {
            modelo.setP_caja(Double.parseDouble(input("Precio Caja: ", value.getP_caja())));
            modelo.setV_total_caja(modelo.getCantidad()*modelo.getP_caja());
        }
        return modelo;
    }

    private String input(String paramTxt, Object antiguo) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(antiguo));
        dialog.setTitle("Cambiar valores");
        dialog.setHeaderText("Ingrese el nuevo valor");
        dialog.setContentText(paramTxt);
        Optional<String> resultado = dialog.showAndWait();
        if(resultado.isPresent())
            return resultado.get();
        return null;
    }



    private String inputFecha(Object antiguo) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(antiguo));
        dialog.setTitle("Cambiar valores");
        dialog.setHeaderText("Ingrese el nuevo valor");
        dialog.setContentText("Fecha compra: ");
        Optional<String> resultado = dialog.showAndWait();
        if(resultado.isPresent())
            return resultado.get();
        return null;
    }

    public void guardarCompra() {
        String value = mapaAutorizacionID.get(txtNumeroFAC.getText());
        if(value != null){
            DBInserts dbInserts = new DBInserts();
            dbInserts.connect();
            dbInserts.insertNotaCompraCabecera(txtAutorizacionFAC.getText(), txtNumeroFAC.getText(), txtNumeroFAC.getText(),
                    getFecha(dateFecha.getValue()), comboFormaPago.getSelectionModel().getSelectedItem(),
                    txtRUC.getText(), txtCantTiempo.getText(), txtPagoInicial.getText(), txtSubtotal12.getText(),
                    txtSubtotal0.getText(), txtIVA.getText(), txtICE.getText(), txtIRBP.getText(), txtTotal.getText());
            guardarNotaDetalle(dbInserts);
        }else
            alertaNumFactura(txtNumeroFAC.getText());
    }

    private void alertaNumFactura(String param){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Numero de Factura");
        alert.setHeaderText("El numero de factura ingresado no existe dentro de las facturas de compra.");
        alert.setContentText("El numero de factura  ||" + param + "|| no existe, por favor ingrese un \n valor valido para la nota de credito.");
        alert.showAndWait();
    }

    private void guardarNotaDetalle(DBInserts param){
        for(ModelDetailPurchase m : tblCompras.getItems())
            param.insertNotaCompraDetalle(m.getCodigo(), txtAutorizacionFAC.getText(),
                    String.valueOf(m.getCantidad()), String.valueOf(m.getP_unit()), String.valueOf(m.getV_total()),
                    String.valueOf(m.getPvp_unit()), String.valueOf(m.getP_caja()), String.valueOf(m.getV_total_caja()),
                    String.valueOf(m.getPvp_caja()), m.getTarifa());
        param.disconnect();
    }

    public void actualizarCompra() {
        System.out.println("Actualizar Compra");

        DBUpdates dbUpdates = new DBUpdates();

        String auto = mapaAutorizacionID.get(txtNumeroFAC.getText());
        String value;
        if(auto != null) {
            value = dbUpdates.updateNotaCabecera(txtNumeroFAC.getText(), txtNumeroFAC.getText(),
                    getFecha(dateFecha.getValue()), comboFormaPago.getSelectionModel().getSelectedItem(),
                    txtRUC.getText(), txtCantTiempo.getText(), txtPagoInicial.getText(), txtSubtotal12.getText(),
                    txtSubtotal0.getText(), txtIVA.getText(), txtICE.getText(), txtIRBP.getText(), txtTotal.getText(),
                    txtAutorizacionFAC.getText());
            if(!value.equals(""))
                alertaActualizacion(value);
        }else
            alertaNumFactura(txtNumeroFAC.getText());
    }

    private void alertaActualizacion(String value){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error al actualizar");
        alert.setTitle("Ha ocurrido un error al momento de actualizar el registro.");
        alert.setContentText("ERROR -->"+value);
        alert.showAndWait();
    }

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private String getFecha(LocalDate localDate){
        if(localDate == null)
            return "";
        else
            return dateTimeFormatter.format(localDate);
    }

    public void eliminarCompra() {
        if(alertaEliminar())
            eliminar();
    }

    private void eliminar(){
        DBDelete dbDelete = new DBDelete();
        if(cabeceraCompra != null) {
            dbDelete.connect();
            for(ModelDetailPurchase m : listaDetalles)
                dbDelete.deleteFacturaCompraDetalles(Integer.parseInt(m.getCodigo()));
            int a = dbDelete.deleteFacturaCompra(cabeceraCompra.getCompra_numero());
            dbDelete.disconnect();
            if(a==1)
                alertaEliminado(cabeceraCompra.getCompra_autorizacion());
        }
    }

    private void alertaEliminado(String codigo){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Factura Eliminada");
        alert.setHeaderText("Se ha eliminado la factura");
        alert.setContentText("La factura con autorizacion: "+ codigo +
                "\n ha sido eliminada correctamente.");
        alert.showAndWait();
    }


    private boolean alertaEliminar(){
        Dialog dialog = new Dialog();

        GridPane grid = new GridPane();
        grid.add(new Label("Password -->"), 0, 0);

        PasswordField txtPass = new PasswordField();
        grid.add(txtPass, 1,0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();

        if(dialog.getResult().equals(ButtonType.OK)){
            if(txtPass.getText().equals("mundoGanadero2019"))
                return true;
            else
                return false;
        }else
            return false;
    }

    private List<ModelDetailPurchase> listaDetalles;

    public void abrirCompra(CabeceraCompra param1, List<ModelDetailPurchase> param2){
        try{
            thread.join();
        }catch (InterruptedException i){
            i.printStackTrace();
        }

        cabeceraCompra = param1;

        txtAutorizacionFAC.setText(param1.getCompra_autorizacion());
        txtNumeroFAC.setText(param1.getCompra_numero());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(param1.getCompra_fecha(), formatter);

        dateFecha.setValue(localDate);

        comboFormaPago.getSelectionModel().select(param1.getCompra_for_pago());

        txtRUC.setText(param1.getProv_ruc());

        txtCantTiempo.setText(String.valueOf(param1.getCompra_dias()));
        txtPagoInicial.setText(String.valueOf(param1.getCompra_pago_inicial()));

        txtSubtotal12.setText(String.valueOf(param1.getCompra_subtotal12()));
        txtSubtotal0.setText(String.valueOf(param1.getCompra_subtotal0()));
        txtIVA.setText(String.valueOf(param1.getCompra_iva()));
        txtICE.setText(String.valueOf(param1.getCompra_ice()));
        txtIRBP.setText(String.valueOf(param1.getCompra_irbp()));
        txtTotal.setText(String.valueOf(param1.getCompra_total()));

        listaDetalles = param2;

        loadCompra(param2);
    }

    public void actualizarNota(){

    }

}
