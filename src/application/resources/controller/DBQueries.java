package application.resources.controller;

import application.resources.model.Customers;
import application.resources.model.Employee;
import application.resources.model.FacturaCabecera;
import application.resources.model.FacturaDetalle;
import application.resources.model.Patient;
import application.resources.model.Product;
import application.resources.model.Provider;
import application.resources.model.RegistroMedico;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBQueries {

    public DBQueries(){}

    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private String sql;
    private ResultSet resultSet;

    public void connect(){
        connection = DBConnection.dbConnection();
    }

    public void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexion");
        }

    }

    public FacturaCabecera getFacturaVentaXCodigo(int codigo){
    	FacturaCabecera  objFac = null;
    	try{
    		connect();
    		sql = "select * from factura_cabecera where factura_id = ?";
    		preparedStatement = connection.prepareStatement(sql);
    		preparedStatement.setInt(1,codigo);
    		resultSet = preparedStatement.executeQuery();
    		while(resultSet.next()){
				objFac = new FacturaCabecera();
				objFac.setNumeroFactura(String.valueOf(resultSet.getInt("factura_id")));
				objFac.setRuc(resultSet.getString("cliente_ruc"));
				objFac.setTipoCompra(resultSet.getString("factura_tipo"));
				objFac.setFecha(resultSet.getString("factura_fecha"));
				objFac.setSubtotal(resultSet.getDouble("factura_subtotal"));
				objFac.setIva(resultSet.getDouble("factura_iva"));
				objFac.setDescuento(resultSet.getDouble("factura_descuento"));
				objFac.setTotal(resultSet.getDouble("factura_total"));

				if(resultSet.getString("factura_anulada").equals("TRUE"))
					objFac.anulada = true;
				else if(resultSet.getString("factura_anulada").equals("FALSE"))
					objFac.anulada = false;
			}
    		disconnect();
    		return objFac;
		}catch (SQLException e){
    		disconnect();
    		e.printStackTrace();
    		return objFac;
		}
	}

    public HashMap<Integer, FacturaCabecera> getMapaFacturaVentaXFecha(String fecha1, String fecha2){
		HashMap<Integer, FacturaCabecera> mapaFacturaVentaXFecha = null;
		FacturaCabecera objFac;
		try{
			connect();
			sql = "SELECT * FROM factura_cabecera WHERE factura_fecha BETWEEN ? AND ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, fecha1);
			preparedStatement.setString(2, fecha2);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				objFac = new FacturaCabecera();
				objFac.setNumeroFactura(String.valueOf(resultSet.getInt("factura_id")));
				objFac.setRuc(resultSet.getString("cliente_ruc"));
				objFac.setTipoCompra(resultSet.getString("factura_tipo"));
				objFac.setFecha(resultSet.getString("factura_fecha"));
				objFac.setSubtotal(resultSet.getDouble("factura_subtotal"));
				objFac.setIva(resultSet.getDouble("factura_iva"));
				objFac.setDescuento(resultSet.getDouble("factura_descuento"));
				objFac.setTotal(resultSet.getDouble("factura_total"));

				if(resultSet.getString("factura_anulada").equals("TRUE"))
					objFac.anulada = true;
				else if(resultSet.getString("factura_anulada").equals("FALSE"))
					objFac.anulada = false;

				mapaFacturaVentaXFecha.put(resultSet.getInt("factura_id"), objFac);
			}
			disconnect();
			return mapaFacturaVentaXFecha;
		}catch (SQLException e){
			disconnect();
			e.printStackTrace();
			return mapaFacturaVentaXFecha;
		}
	}
    
    public List<FacturaDetalle> getFacturasDetalles(int idCabecera){
    	List<FacturaDetalle> listaFacturaDetalles = new ArrayList<>();
    	FacturaDetalle objFacDetalle;
    	try {
    		sql = "Select * from factura_detalle where cabecera_id = ?";
    		preparedStatement = connection.prepareStatement(sql);
    		preparedStatement.setInt(1, idCabecera);
    		resultSet = preparedStatement.executeQuery();
    		while(resultSet.next()) {
    			objFacDetalle = new FacturaDetalle();
    			objFacDetalle.setCodigo(String.valueOf(resultSet.getInt("prod_id")));
    			objFacDetalle.setCantidad(resultSet.getDouble("cantidad"));
    			objFacDetalle.setDescripcion(resultSet.getString("descripcion"));
    			objFacDetalle.setPrecioCaja(resultSet.getDouble("precio_caja"));
    			objFacDetalle.setPrecioUnit(resultSet.getDouble("precio_unit"));
    			objFacDetalle.setTarifa(resultSet.getString("tarifa"));
    			objFacDetalle.setTotal(resultSet.getDouble("total"));
    			listaFacturaDetalles.add(objFacDetalle);
    		}
    		disconnect();
    		return listaFacturaDetalles;
    	}catch(SQLException e) {
    		disconnect();
    		e.printStackTrace();
    		return listaFacturaDetalles;
    	}
    }
    
    public HashMap<Integer, FacturaCabecera> getMapaIDFacturaCab(){
    	HashMap<Integer, FacturaCabecera> mapaIDFactura = new HashMap<>();
    	try {
    		connect();
    		sql = "Select * from factura_cabecera";
    		statement = connection.createStatement();
    		resultSet = statement.executeQuery(sql);
    		FacturaCabecera objFac;
    		while(resultSet.next()) {
    			objFac = new FacturaCabecera();
    			objFac.setNumeroFactura(String.valueOf(resultSet.getInt("factura_id")));
    			objFac.setRuc(resultSet.getString("cliente_ruc"));
    			objFac.setTipoCompra(resultSet.getString("factura_tipo"));
    			objFac.setFecha(resultSet.getString("factura_fecha"));
    			objFac.setSubtotal(resultSet.getDouble("factura_subtotal"));
    			objFac.setIva(resultSet.getDouble("factura_iva"));
    			objFac.setDescuento(resultSet.getDouble("factura_descuento"));
    			objFac.setTotal(resultSet.getDouble("factura_total"));
    			
    			if(resultSet.getString("factura_anulada").equals("TRUE"))
    				objFac.anulada = true;
    			else if(resultSet.getString("factura_anulada").equals("FALSE"))
    				objFac.anulada = false;
    			
    			mapaIDFactura.put(resultSet.getInt("factura_id"), objFac);
    		}
    		disconnect();
    		return mapaIDFactura;
    	}catch(SQLException e) {
    		disconnect();
    		e.printStackTrace();
    	}
    	return mapaIDFactura;
    }
    
    public String getFacturaActual() {
    	String factActual = "";
    	try {
    		connect();
    		sql = "SELECT `AUTO_INCREMENT` as incremento FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'mundoganadero' "
    				+ "AND TABLE_NAME   = 'factura_cabecera'";
    		statement = connection.createStatement();
    		resultSet = statement.executeQuery(sql);
    		while(resultSet.next())
    			factActual = String.valueOf(resultSet.getInt("incremento"));
    		disconnect();
    		return factActual;
    	}catch(SQLException e) {
    		return e.getMessage();
    	}
    }
    
    public HashMap<String, String> getVencimientos(){
    	HashMap<String, String> mapaProductoVence = new HashMap<>();
    	try {
    		connect();
    		sql = "SELECT prod_name AS nombre, DATEDIFF(DATE(prod_fecha), CURDATE()) AS diferencia FROM "
    				+ "productos HAVING diferencia < 20 ORDER BY diferencia desc";
    		statement = connection.createStatement();
    		resultSet = statement.executeQuery(sql);
    		while(resultSet.next())
    			mapaProductoVence.put("El producto: " + resultSet.getString("nombre"), " vence en " + resultSet.getString("diferencia") + " dias");
    		disconnect();
    		return mapaProductoVence;
    	}catch(SQLException e) {
    		disconnect();
    		return mapaProductoVence;
    	}
    }
    
    public List<RegistroMedico> getRegistrosMedicos(){
    	RegistroMedico registroMedico;
    	List<RegistroMedico> registroMedicos = new ArrayList<RegistroMedico>();
    	try {
    		connect();
    		sql = "Select * from RegistroMedico";
    		statement = connection.createStatement();
    		resultSet = statement.executeQuery(sql);
    		while(resultSet.next()) {
    			registroMedico = new RegistroMedico();
    			registroMedico.setId(resultSet.getInt("registro_id"));
    			registroMedico.setFecha(resultSet.getString("registro_fecha"));
    			registroMedico.setHora(resultSet.getString("registro_hora"));
    			registroMedico.setNext_fecha(resultSet.getString("registro_next_fecha"));
    			registroMedico.setTipo(resultSet.getString("registro_tipo"));
    			registroMedico.setDescripcion(resultSet.getString("registro_descripcion"));
    			registroMedico.setPaciente_id(resultSet.getInt("registro_paciente_id"));
    			registroMedicos.add(registroMedico);
    		}
    		disconnect();
    		return registroMedicos;
    	}catch(SQLException e) {
    		disconnect();
    		e.printStackTrace();
    		return null;
    	}
    	
    }
    
    public List<Customers> clientes(){
    	List<Customers> listaClientes = new ArrayList<>();
    	Customers cliente;
    	try {
    		connect();
    		
    		sql = "Select * from clientes";
    		statement = connection.createStatement();
    		resultSet = statement.executeQuery(sql);
    		while(resultSet.next()) {
    			cliente = new Customers();
    			cliente.setCi(resultSet.getString("cliente_id"));
    			cliente.setDireccion(resultSet.getString("cliente_direccion"));
    			cliente.setEmail(resultSet.getString("cliente_email"));
    			cliente.setNombre(resultSet.getString("cliente_nombre"));
    			cliente.setTelefono(resultSet.getString("cliente_telefono"));
    			listaClientes.add(cliente);
    		}
    		disconnect();
    		return listaClientes;
    	}catch(SQLException e) {
    		disconnect();
    		System.out.println(e.getMessage());
    		return listaClientes;
    	}
    }
    
    // HashMap de los clientes, devuelve la cedula y nombre de los clientes
    public HashMap<String, String> getClientsCN(){
    	HashMap<String, String> mapaClientesCN = new HashMap<>();
    	try {
    		connect();
    		sql = "SELECT cliente_CI , cliente_Nombre FROM"
    				+ " Clientes";
    		statement = connection.createStatement();
    		resultSet = statement.executeQuery(sql);
    		while(resultSet.next()) {
    			String ci = resultSet.getString("cliente_CI");
    			String nombre = resultSet.getString("cliente_Nombre");
    			mapaClientesCN.put(nombre, ci);
    		}
    		disconnect();
    		return mapaClientesCN;
    	}catch(Exception e) {
    		disconnect();
    		e.printStackTrace();
    		return mapaClientesCN;
    	}
    	
    }
    
    public HashMap<String, Integer> getPatientsNomID(){
    	HashMap<String, Integer> pacientesHashMap = new HashMap<>();
    	try {
    		connect();
    		
    		sql = "Select * from Pacientes";
    		statement = connection.createStatement();
    		resultSet = statement.executeQuery(sql);
    		
    		while(resultSet.next()) 
    			pacientesHashMap.put(resultSet.getString("paciente_nombre"), resultSet.getInt("paciente_ID"));
    		disconnect();
    		return pacientesHashMap;
    	}catch(SQLException e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public HashMap<Integer, Patient> getPatients(){
    	HashMap<Integer, Patient> pacientesHashMap = new HashMap<>();
    	Patient patient;
    	try {
    		connect();
    		
    		sql = "Select * from Pacientes";
    		statement = connection.createStatement();
    		resultSet = statement.executeQuery(sql);

    		while(resultSet.next()) {
    			patient = new Patient();
    			patient.setId(resultSet.getInt("paciente_ID"));
    			patient.setNombre(resultSet.getString("paciente_nombre"));
    			patient.setClase(resultSet.getString("paciente_clase"));
    			patient.setGenero(resultSet.getString("paciente_genero"));
    			patient.setDireccion(resultSet.getString("paciente_direccion"));
    			patient.setFecha_nac(resultSet.getString("paciente_fecha_nac"));
    			patient.setRaza(resultSet.getString("paciente_raza"));
    			patient.setPelaje(resultSet.getString("paciente_pelaje"));
    			patient.setProp_ci(resultSet.getString("paciente_prop_ci"));
    			patient.setCiudad(resultSet.getString("paciente_ciudad"));
    			patient.setEdad(resultSet.getString("paciente_edad"));
    			
    			pacientesHashMap.put(resultSet.getInt("paciente_ID"), patient);
    		}
    		disconnect();
    		return pacientesHashMap;
    	}catch(Exception e) {
    		disconnect();
    		e.printStackTrace();
    	}
    	return null;
    	
    }

    public HashMap<String, List<Product>> productsHashMap(String providerName){
        HashMap<String, List<Product>> productHashMap = new HashMap<>();
        List<Product> products = new ArrayList<>();
        Product product;
        try{
            sql = "Select * from Productos where " +
                    "(Select prov_RUC from Proveedores where prov_nombre=?) = prov_RUC";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, providerName);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String id = resultSet.getString("prod_id");
                String main_id = resultSet.getString("prod_main_id");
                String name = resultSet.getString("prod_name");
                String main_name = resultSet.getString("prod_main_name");
                String fecha = resultSet.getString("prod_fecha");
                String ruc = resultSet.getString("prov_RUC");
                String category = resultSet.getString("prod_category");
                String representation = resultSet.getString("prod_representation");
                int quantity = resultSet.getInt("prod_int_quantity");
                int stock = resultSet.getInt("prod_stock");
                double box_price = resultSet.getDouble("prod_box_price");
                double unit_price = resultSet.getDouble("prod_unit_price");
                String tax = resultSet.getString("prod_tax");
                double box_12 = resultSet.getDouble("prod_box_12");
                double unit_12 = resultSet.getDouble("prod_unit_12");
                double sell_box = resultSet.getDouble("prod_sell_box");
                double sell_unit = resultSet.getDouble("prod_sell_unit");
                product = new Product(id, main_id, name, main_name, fecha, ruc, category, representation, quantity,
                        stock, box_price, unit_price, tax, box_12, unit_12, sell_box, sell_unit);
                products.add(product);
            }
            productHashMap.put(providerName, products);
            //System.out.println(productHashMap);
            return productHashMap;
        }catch (SQLException e) {
        	e.printStackTrace();
            e.getErrorCode();
            return null;
        }
    }
    
    public List<HashMap<String, Product>> productosHashMap(){
    	List<HashMap<String, Product>> listaHashMaps = new ArrayList<>();
    	
    	HashMap<String, Product> idProductoMap = new HashMap<>();
    	HashMap<String, Product> nombreProductoMap = new HashMap<>();
    	
    	Product product;
    	try {
    		connect();
    		statement = connection.createStatement();
    		sql = "select * from productos";
    		resultSet = statement.executeQuery(sql);
    		while(resultSet.next()) {
    			String id = resultSet.getString("prod_id");
                String main_id = resultSet.getString("prod_main_id");
                String name = resultSet.getString("prod_name");
                String main_name = resultSet.getString("prod_main_name");
                String fecha = resultSet.getString("prod_fecha");
                String ruc = resultSet.getString("prov_RUC");
                String category = resultSet.getString("prod_category");
                String representation = resultSet.getString("prod_representation");
                int quantity = resultSet.getInt("prod_int_quantity");
                double stock = resultSet.getDouble("prod_stock");
                double box_price = resultSet.getDouble("prod_box_price");
                double unit_price = resultSet.getDouble("prod_unit_price");
                String tax = resultSet.getString("prod_tax");
                double box_12 = resultSet.getDouble("prod_box_12");
                double unit_12 = resultSet.getDouble("prod_unit_12");
                double sell_box = resultSet.getDouble("prod_sell_box");
                double sell_unit = resultSet.getDouble("prod_sell_unit");

                product = new Product(id, main_id, name, main_name, fecha, ruc, category, representation, quantity,
                        stock, box_price, unit_price, tax, box_12, unit_12, sell_box, sell_unit);
                
                idProductoMap.put(id, product);
                nombreProductoMap.put(name, product);
                
    		}
    		listaHashMaps.add(idProductoMap);
            listaHashMaps.add(nombreProductoMap);
    		disconnect();
    		return listaHashMaps;
    	}catch(SQLException e) {
    		System.out.println(e.getMessage());
    		disconnect();
    		return listaHashMaps;
    	}
    	
    }

    public List<Product> getProducts(){
        List<Product> productList = new ArrayList<>();
        Product product;
        try{
            connect();
            statement = connection.createStatement();
            sql = "select * from Productos";
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String id = resultSet.getString("prod_id");
                String main_id = resultSet.getString("prod_main_id");
                String name = resultSet.getString("prod_name");
                String main_name = resultSet.getString("prod_main_name");
                String fecha = resultSet.getString("prod_fecha");
                String ruc = resultSet.getString("prov_RUC");
                String category = resultSet.getString("prod_category");
                String representation = resultSet.getString("prod_representation");
                int quantity = resultSet.getInt("prod_int_quantity");
                double stock = resultSet.getDouble("prod_stock");
                double box_price = resultSet.getDouble("prod_box_price");
                double unit_price = resultSet.getDouble("prod_unit_price");
                String tax = resultSet.getString("prod_tax");
                double box_12 = resultSet.getDouble("prod_box_12");
                double unit_12 = resultSet.getDouble("prod_unit_12");
                double sell_box = resultSet.getDouble("prod_sell_box");
                double sell_unit = resultSet.getDouble("prod_sell_unit");

                product = new Product(id, main_id, name, main_name, fecha, ruc, category, representation, quantity,
                        stock, box_price, unit_price, tax, box_12, unit_12, sell_box, sell_unit);
                productList.add(product);
            }
            disconnect();
            return productList;
        }catch (Exception e){
            System.out.println("Error al obtener datos del producto");
            return null;
        }
        //return productList;
    }

    public HashMap<Integer, Employee> getEmployees(){
        HashMap<Integer, Employee> empleadosList = new HashMap<>();
        Employee employee;
        try{
            connect();
            statement = connection.createStatement();
            sql = "select * from Empleados";
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                int id = resultSet.getInt("empleado_id");
                String nombre = resultSet.getString("empleado_nombres");
                String titulo = resultSet.getString("empleado_titulo");
                String email = resultSet.getString("empleado_email");
                String telefono = resultSet.getString("empleado_telefono");
                String direccion = resultSet.getString("empleado_direccion");
                employee = new Employee(id, nombre, titulo, email, telefono, direccion);
                empleadosList.put(employee.getId(), employee);
            }
            disconnect();
        }catch (Exception e){
        	disconnect();
            System.out.println("Error al obtener empleados");
        }
        return empleadosList;
    }

    public List<Provider> getProviders(){
        List<Provider> providerList = new ArrayList<>();
        Provider provider;
        try{
            connect();
            statement = connection.createStatement();
            sql = "select * from Proveedores";
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String ruc = resultSet.getString("prov_ruc");
                String nombre = resultSet.getString("prov_nombre");
                String direccion = resultSet.getString("prov_direccion");
                String telefono = resultSet.getString("prov_telefono");
                String email = resultSet.getString("prov_email");
                String observaciones = resultSet.getString("prov_observaciones");
                provider = new Provider(ruc, nombre, direccion, telefono, email, observaciones);
                providerList.add(provider);
            }
            disconnect();
        }catch (Exception e){
            System.out.println("Error al obtener proveedores");
        }
        return providerList;
    }

}
