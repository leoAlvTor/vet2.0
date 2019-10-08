package application.resources.controller;

import application.resources.model.*;

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

	public HashMap<String, Customers> getIdCliente(){
		HashMap<String, Customers> mapaIdCliente = new HashMap<>();
		try{
			connect();
			sql = "Select * from clientes";
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			Customers cliente;
			while(resultSet.next()){
				cliente = new Customers();
				cliente.setCi(resultSet.getString("cliente_id"));
				cliente.setNombre(resultSet.getString("cliente_nombre"));
				cliente.setTelefono(resultSet.getString("cliente_telefono"));
				cliente.setDireccion(resultSet.getString("cliente_direccion"));
				cliente.setEmail(resultSet.getString("cliente_email"));
				mapaIdCliente.put(cliente.getCi(), cliente);
			}
			disconnect();
			return mapaIdCliente;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return mapaIdCliente;
	}

	public Customers buscarCustomer(String nombre){
		Customers customer = new Customers();
		try{
			connect();
			statement = connection.createStatement();
			sql = "select * from clientes where cliente_nombre='"+nombre+"'";
			resultSet = statement.executeQuery(sql);
			while(resultSet.next()){
				String ci=String.valueOf(resultSet.getInt("cliente_id"));
				customer.setCi(ci);
				customer.setDireccion(resultSet.getString("cliente_direccion"));
				customer.setNombre(resultSet.getString("cliente_nombre"));
				customer.setTelefono(resultSet.getString("cliente_telefono"));
				customer.setEmail(resultSet.getString("cliente_email"));
				System.out.println("ci");
			}
			disconnect();
			return customer;

		}catch (Exception e){
			System.out.println("Error al obtener datos del producto");
			return null;
		}

	}

    public HashMap<Integer, FacturaCabecera> getMapaFacturaVentaXFecha(String fecha1, String fecha2){
		HashMap<Integer, FacturaCabecera> mapaFacturaVentaXFecha = new HashMap<>();
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

	public List<ModelDetailPurchase> getDetalleCompras(String autorizacion){
        List<ModelDetailPurchase> listaDetalleCompras = new ArrayList<>();
        ModelDetailPurchase modelDetailPurchase;
        try{
            sql = "SELECT c.id, c.prod_id, c.cabecera_id, c.cantidad, c.p_unit, c.v_total, c.pvp_unit, c.p_caja," +
                    " c.v_total_caja, c.pvp_caja, c.tarifa, c.fecha_vencimiento, p.prod_name FROM compra_detalle c," +
                    " productos p WHERE cabecera_id = ? AND p.prod_id = c.prod_id;";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, autorizacion);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                modelDetailPurchase = new ModelDetailPurchase();
                modelDetailPurchase.setCodigo(String.valueOf(resultSet.getInt("id")));
                modelDetailPurchase.setDescripcion(resultSet.getString("prod_name"));

                modelDetailPurchase.setCantidad(resultSet.getInt("cantidad"));

                modelDetailPurchase.setP_unit(resultSet.getDouble("p_unit"));
                modelDetailPurchase.setV_total(resultSet.getDouble("v_total"));
                modelDetailPurchase.setPvp_unit(resultSet.getDouble("pvp_unit"));

                modelDetailPurchase.setTarifa(resultSet.getString("tarifa"));

                modelDetailPurchase.setP_caja(resultSet.getDouble("p_caja"));
                modelDetailPurchase.setV_total_caja(resultSet.getDouble("v_total_caja"));
                modelDetailPurchase.setPvp_caja(resultSet.getDouble("pvp_caja"));

                modelDetailPurchase.setFecha_vencimiento(resultSet.getString("fecha_vencimiento"));

                listaDetalleCompras.add(modelDetailPurchase);
            }
            return listaDetalleCompras;
        }catch (SQLException e){
            disconnect();
            System.out.println(e.getMessage());
            return listaDetalleCompras;
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

    public CabeceraCompra getCabeceraCompraProveedor(String ruc){
        CabeceraCompra objCabecera = null;
        try{
            connect();
            sql = "select * from compra_cabecera where proveedor_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,ruc);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                objCabecera = new CabeceraCompra();
                objCabecera.setCompra_autorizacion(resultSet.getString("factura_id"));
                objCabecera.setCompra_numero(resultSet.getString("autorizacion"));
                objCabecera.setCompra_fecha(resultSet.getString("fecha"));
                objCabecera.setCompra_for_pago(resultSet.getString("forma_pago"));
                objCabecera.setProv_ruc(resultSet.getString("proveedor_id"));

                objCabecera.setCompra_dias(resultSet.getInt("plazo"));

                objCabecera.setCompra_pago_inicial(resultSet.getDouble("abono"));
                objCabecera.setCompra_subtotal12(resultSet.getDouble("subtotal12"));
                objCabecera.setCompra_subtotal0(resultSet.getDouble("subtotal0"));
                objCabecera.setCompra_iva(resultSet.getDouble("totalIVA"));
                objCabecera.setCompra_ice(resultSet.getDouble("ice"));
                objCabecera.setCompra_irbp(resultSet.getDouble("irbp"));
                objCabecera.setCompra_total(resultSet.getDouble("total"));
            }

            disconnect();
            return objCabecera;
        }catch (SQLException e){
            disconnect();
            System.out.println(e.getMessage());
            return objCabecera;
        }
    }

    public HashMap<String, CabeceraCompra> getMapaIDFacturaCompraFecha(String fecha1, String fecha2){
        HashMap<String, CabeceraCompra> mapaIDFactura = new HashMap<>();
        try{
            connect();
            sql = "SELECT * FROM compra_cabecera WHERE fecha BETWEEN ? AND ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, fecha1);
            preparedStatement.setString(2, fecha2);
            resultSet = preparedStatement.executeQuery();
            CabeceraCompra objCabecera;

            if(!resultSet.next())
                return null;
            resultSet.beforeFirst();
            while(resultSet.next()){
                objCabecera = new CabeceraCompra();
                objCabecera.setCompra_autorizacion(resultSet.getString("factura_id"));
                objCabecera.setCompra_numero(resultSet.getString("autorizacion"));
                objCabecera.setCompra_fecha(resultSet.getString("fecha"));
                objCabecera.setCompra_for_pago(resultSet.getString("forma_pago"));
                objCabecera.setProv_ruc(resultSet.getString("proveedor_id"));

                objCabecera.setCompra_dias(resultSet.getInt("plazo"));

                objCabecera.setCompra_pago_inicial(resultSet.getDouble("abono"));
                objCabecera.setCompra_subtotal12(resultSet.getDouble("subtotal12"));
                objCabecera.setCompra_subtotal0(resultSet.getDouble("subtotal0"));
                objCabecera.setCompra_iva(resultSet.getDouble("totalIVA"));
                objCabecera.setCompra_ice(resultSet.getDouble("ice"));
                objCabecera.setCompra_irbp(resultSet.getDouble("irbp"));
                objCabecera.setCompra_total(resultSet.getDouble("total"));
                mapaIDFactura.put(objCabecera.getCompra_numero(), objCabecera);
            }
            disconnect();
            return mapaIDFactura;

        }catch (SQLException e){
            disconnect();
            System.out.println(e.getMessage());
            return mapaIDFactura;
        }
    }

    public HashMap<String, CabeceraCompra> getMapaIDFacturaDet(){
        HashMap<String, CabeceraCompra> mapaIDFactura = new HashMap<>();
        try{
            connect();

            sql = "Select * from compra_cabecera";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            CabeceraCompra objCabecera;
            while(resultSet.next()){
                objCabecera = new CabeceraCompra();
                objCabecera.setCompra_autorizacion(resultSet.getString("factura_id"));
                objCabecera.setCompra_numero(resultSet.getString("autorizacion"));
                objCabecera.setCompra_fecha(resultSet.getString("fecha"));
                objCabecera.setCompra_for_pago(resultSet.getString("forma_pago"));
                objCabecera.setProv_ruc(resultSet.getString("proveedor_id"));

                objCabecera.setCompra_dias(resultSet.getInt("plazo"));

                objCabecera.setCompra_pago_inicial(resultSet.getDouble("abono"));
                objCabecera.setCompra_subtotal12(resultSet.getDouble("subtotal12"));
                objCabecera.setCompra_subtotal0(resultSet.getDouble("subtotal0"));
                objCabecera.setCompra_iva(resultSet.getDouble("totalIVA"));
                objCabecera.setCompra_ice(resultSet.getDouble("ice"));
                objCabecera.setCompra_irbp(resultSet.getDouble("irbp"));
                objCabecera.setCompra_total(resultSet.getDouble("total"));
                mapaIDFactura.put(objCabecera.getCompra_numero(), objCabecera);
            }

            disconnect();
            return mapaIDFactura;

        }catch (SQLException e){
            disconnect();
            System.out.println(e.getMessage());
            return mapaIDFactura;
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
