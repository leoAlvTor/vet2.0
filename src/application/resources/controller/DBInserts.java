package application.resources.controller;

import java.sql.*;

public class DBInserts {

    public DBInserts(){}

    private Connection connection;
    private Statement statement;
    private String sql;
    private PreparedStatement preparedStatement = null;

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
    

    
    public String insertFacturaCabecera(Object ...params) {
    	try {
    		sql = "insert into factura_cabecera set cliente_ruc = ?, factura_tipo = ?, factura_fecha = ?, factura_subtotal = ?, "
    				+ " factura_iva = ?, factura_descuento = ?, factura_total = ?";
    		
    		preparedStatement = connection.prepareStatement(sql);
    		
    		preparedStatement.setString(1, String.valueOf(params[0]));
    		preparedStatement.setString(2, String.valueOf(params[1]));
    		preparedStatement.setString(3, String.valueOf(params[2]));
    		
    		preparedStatement.setDouble(4, Double.parseDouble(String.valueOf(params[3])));
    		preparedStatement.setDouble(5, Double.parseDouble(String.valueOf(params[4])));
    		preparedStatement.setDouble(6, Double.parseDouble(String.valueOf(params[5])));
    		preparedStatement.setDouble(7, Double.parseDouble(String.valueOf(params[6])));
    		
    		preparedStatement.executeUpdate();
    		return "";
    	}catch(SQLException e) {
    		return e.getMessage();
    	}
    }
    
    public String insertFacturaDetalle(Object ...params) {
    	try {
    		sql = "insert into factura_detalle set cabecera_id = ?, prod_id = ?, descripcion = ?, tarifa = ?, cantidad = ?, precio_unit = ?, precio_caja = ?, total = ?";
    		preparedStatement = null;
    		preparedStatement = connection.prepareStatement(sql);
    		
    		preparedStatement.setInt(1, Integer.parseInt(String.valueOf(params[0])));
    		
    		preparedStatement.setString(2, String.valueOf(params[1]));
    		preparedStatement.setString(3, String.valueOf(params[2]));
    		preparedStatement.setString(4, String.valueOf(params[3]));
    		
    		
    		preparedStatement.setDouble(5, Double.parseDouble(String.valueOf(params[4])));
    		preparedStatement.setDouble(6, Double.parseDouble(String.valueOf(params[5])));
    		preparedStatement.setDouble(7, Double.parseDouble(String.valueOf(params[6])));
    		preparedStatement.setDouble(8, Double.parseDouble(String.valueOf(params[7])));
    		
    		preparedStatement.executeUpdate();
    		
    		return "";
    	}catch(SQLException e) {
    		e.printStackTrace();
    		return e.getMessage();
    	}
    }

    public String insertNotaDetalle(String ... params){
    	try{
    		sql = "INSERT INTO nota_detalle SET id = ?, prod_id = ?, autorizacion = ?, cantidad = ?, p_unit = ?," +
					" v_total = ?, pvp_unit = ?, p_caja = ?, v_total_caja = ?, pvp_caja = ?, tarifa = ?";
    		preparedStatement = connection.prepareStatement(sql);

    		preparedStatement.setInt(1, Integer.parseInt(params[0]));

    		preparedStatement.setString(2, params[1]);
    		preparedStatement.setString(3, params[2]);

    		preparedStatement.setInt(4, Integer.parseInt(params[3]));

    		preparedStatement.setDouble(5, Double.parseDouble(params[4]));
			preparedStatement.setDouble(6, Double.parseDouble(params[5]));
			preparedStatement.setDouble(7, Double.parseDouble(params[6]));
			preparedStatement.setDouble(8, Double.parseDouble(params[7]));
			preparedStatement.setDouble(9, Double.parseDouble(params[8]));
			preparedStatement.setDouble(10, Double.parseDouble(params[9]));

			preparedStatement.setString(11, params[10]);
			preparedStatement.executeUpdate();
			return "";
		}catch (SQLException e){
    		disconnect();
			System.out.println(e.getMessage());
			return e.getMessage();
		}
	}
    
    public String insertCompraDetalle(Object ... params) {
    	try {
    		
    		sql = "insert into compra_detalle set prod_id = ?, cabecera_id = ?, cantidad = ?, p_unit = ?, v_total = ?," +
					" pvp_unit = ?, p_caja = ?, v_total_caja = ?, pvp_caja = ?, tarifa = ?, fecha_vencimiento = ?";
    		preparedStatement = null;
    		preparedStatement = connection.prepareStatement(sql);
    		
    		preparedStatement.setString(1, String.valueOf(params[0]));
    		preparedStatement.setString(2, String.valueOf(params[1]));
    		
    		preparedStatement.setInt(3, Integer.valueOf(String.valueOf(params[2])));
    		
    		preparedStatement.setDouble(4, Double.parseDouble(String.valueOf(params[3])));
    		preparedStatement.setDouble(5, Double.parseDouble(String.valueOf(params[4])));
    		preparedStatement.setDouble(6, Double.parseDouble(String.valueOf(params[5])));
    		preparedStatement.setDouble(7, Double.parseDouble(String.valueOf(params[6])));
    		preparedStatement.setDouble(8, Double.parseDouble(String.valueOf(params[7])));
    		preparedStatement.setDouble(9, Double.parseDouble(String.valueOf(params[8])));
    		
    		preparedStatement.setString(10, String.valueOf(params[9]));
    		preparedStatement.setString(11, String.valueOf(params[10]));
    		
    		preparedStatement.executeUpdate();

    	}catch(SQLException e) {
    		System.out.println(e.getMessage());
    		return e.getMessage();
    	}
    	return "";
    }

    public String insertNotaCabecera(String ... params){
    	try{
    		sql = "INSERT INTO nota_cabecera SET autorizacion = ?, idFactura = ?, numeroNota = ?, fechaNota = ?, " +
					"formaPago = ?, proveedorRuc = ?, plazo = ?, abono = ?, subtotal12 = ?, subtotal0 = ?, iva = ?," +
					" ice = ?, irbp = ?, total = ?";
    		preparedStatement = connection.prepareStatement(sql);

    		preparedStatement.setString(1, params[0]);
			preparedStatement.setString(2, params[1]);
			preparedStatement.setString(3, params[2]);
			preparedStatement.setString(4, params[3]);
			preparedStatement.setString(5, params[4]);
			preparedStatement.setString(6, params[5]);
			preparedStatement.setString(7, params[6]);

			preparedStatement.setDouble(8, Double.parseDouble(params[7]));
			preparedStatement.setDouble(9, Double.parseDouble(params[8]));
			preparedStatement.setDouble(10, Double.parseDouble(params[9]));
			preparedStatement.setDouble(11, Double.parseDouble(params[10]));
			preparedStatement.setDouble(12, Double.parseDouble(params[11]));
			preparedStatement.setDouble(13, Double.parseDouble(params[12]));
			preparedStatement.setDouble(14, Double.parseDouble(params[13]));

			preparedStatement.executeUpdate();
			return "";
		}catch (SQLException e){
    		disconnect();
			System.out.println("NOTA CABECERA --> \n" + e.getMessage());
    		return e.getMessage();
		}
	}
    
    public String insertCompraCabecera(Object ... params) {
    	try {
    		sql = "insert into compra_cabecera set factura_id = ?, autorizacion = ?, fecha = ?, forma_pago = ?," +
					" proveedor_id = ?, plazo = ?, abono = ?, subtotal12 = ?, subtotal0 = ?, totalIVA = ?, ice = ?, irbp = ?, total = ?";
    		preparedStatement = null;
    		preparedStatement = connection.prepareStatement(sql);
    		
    		preparedStatement.setString(1, String.valueOf(params[0]));
    		preparedStatement.setString(2, String.valueOf(params[1]));
    		preparedStatement.setString(3, String.valueOf(params[2]));
    		preparedStatement.setString(4, String.valueOf(params[3]));
    		preparedStatement.setString(5, String.valueOf(params[4]));
    		preparedStatement.setString(6, String.valueOf(params[5]));
    		
    		preparedStatement.setDouble(7, Double.parseDouble(String.valueOf(params[6])));
    		preparedStatement.setDouble(8, Double.parseDouble(String.valueOf(params[7])));
    		preparedStatement.setDouble(9, Double.parseDouble(String.valueOf(params[8])));
    		preparedStatement.setDouble(10, Double.parseDouble(String.valueOf(params[9])));
    		preparedStatement.setDouble(11, Double.parseDouble(String.valueOf(params[10])));
    		preparedStatement.setDouble(12, Double.parseDouble(String.valueOf(params[11])));
    		preparedStatement.setDouble(13, Double.parseDouble(String.valueOf(params[12])));
    		
    		preparedStatement.executeUpdate();

    	}catch(SQLException e) {
    		System.out.println(e.getMessage());
    		return e.getMessage();
    	}
    	return "";
    }
    
    
    public String insertCliente(String ... params) {
    	try {
    		connect();
    		sql = "insert into Clientes set cliente_CI = ?, cliente_Nombre = ?, cliente_Telefono = ?, cliente_Direccion = ?, cliente_Email = ?";
    		preparedStatement = connection.prepareStatement(sql);
    		preparedStatement.setString(1, params[0]);
    		preparedStatement.setString(2, params[1]);
    		preparedStatement.setString(3, params[2]);
    		preparedStatement.setString(4, params[3]);
    		preparedStatement.setString(5, params[4]);
    		preparedStatement.executeUpdate();
    		disconnect();
    		return "";
    	}catch(SQLException e) {
    		disconnect();
    		return e.getMessage();
    	}

    }

    public int insertProduct(String id, String main_id, String name, String main_name, String fecha, String prov_RUC, String category,
                              String representation, int internal_quantity, double stock, double box_price, double unit_price,
                              String tax, double box_12, double unit_12, double sell_box, double sell_unit){
        try{
            connect();
            statement = connection.createStatement();
            sql = "insert into Productos values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, main_id);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, main_name);
            preparedStatement.setString(5, fecha);
            preparedStatement.setString(6, prov_RUC);
            preparedStatement.setString(7, category);
            preparedStatement.setString(8, representation);
            preparedStatement.setInt(9, internal_quantity);
            preparedStatement.setDouble(10, stock);
            preparedStatement.setDouble(11, box_price);
            preparedStatement.setDouble(12, unit_price);
            preparedStatement.setString(13, tax);
            preparedStatement.setDouble(14, box_12);
            preparedStatement.setDouble(15, unit_12);
            preparedStatement.setDouble(16, sell_box);
            preparedStatement.setDouble(17, sell_unit);
            preparedStatement.executeUpdate();
            disconnect();
            return 0;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            disconnect();
            if(e.getErrorCode() == 1062)
                return 1;
            else
                return 2;
        }
    }

    public int insertProvider(String ruc, String nombre, String direccion, String telefono, String email, String observaciones){
        try{
            connect();
            statement = connection.createStatement();
            sql = "insert into Proveedores values(?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, ruc);
            preparedStatement.setString(2, nombre);
            preparedStatement.setString(3, direccion);
            preparedStatement.setString(4, telefono);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, observaciones);
            preparedStatement.executeUpdate();
            disconnect();
            return 0;
        }catch (SQLException e){
        	disconnect();
            if(e.getErrorCode() == 1062)
                return 1;
            else
                return 2;
        }
    }
    
    public String insertEmpleado(String nombre, String titulo, String email, String telefono, String direccion) {
    	try {
    		connect();
    		statement = connection.createStatement();
    		sql = "insert into Empleados set empleado_nombres = ?, empleado_titulo = ?, empleado_email = ?," +
					" empleado_telefono = ?, empleado_direccion = ?";
    		preparedStatement = connection.prepareStatement(sql);
    		preparedStatement.setString(1, nombre);
    		preparedStatement.setString(2, titulo);
    		preparedStatement.setString(3, email);
    		preparedStatement.setString(4, telefono);
    		preparedStatement.setString(5, direccion);
    		preparedStatement.executeUpdate();
    		disconnect();
    		return "";
    	}catch(SQLException e) {
    		disconnect();
    		e.printStackTrace();
    		return e.getMessage();
    	}
    }
    
    public String insertRegistroMedico(String fecha, String hora, String next_fecha, String tipo, String descripcion, int paciente_id) {
    	try {
    		connect();
    		statement = connection.createStatement();
    		sql = "INSERT INTO RegistroMedico SET registro_fecha = ?, registro_hora = ?,"
    				+ " registro_next_fecha = ?, registro_tipo = ?, registro_descripcion = ?, registro_paciente_id = ?";
    		preparedStatement = connection.prepareStatement(sql);
    		preparedStatement.setString(1, fecha);
    		preparedStatement.setString(2, hora);
    		preparedStatement.setString(3, next_fecha);
    		preparedStatement.setString(4, tipo);
    		preparedStatement.setString(5, descripcion);
    		preparedStatement.setInt(6, paciente_id);
    		preparedStatement.executeUpdate();
    		disconnect();
    		return "";
    	}catch(SQLException e) {
    		disconnect();
    		e.printStackTrace();
    		return e.getMessage();
    	}
    }
    
    public int insertPatient(String nombre, String clase, String genero, String direccion, String fecha_nac,
    		String raza, String pelaje, String prop_ci, String ciudad, String edad) {
    	try {
    		connect();
    		statement = connection.createStatement();
    		sql = "INSERT INTO Pacientes set paciente_nombre = ?, paciente_clase = ?, paciente_genero = ?, paciente_direccion = ?,"
    				+ " paciente_fecha_nac = ?, paciente_raza = ?, paciente_pelaje = ?, paciente_prop_ci = ?, paciente_ciudad = ?, paciente_edad = ?";
    		preparedStatement = connection.prepareStatement(sql);
    		preparedStatement.setString(1, nombre);
    		preparedStatement.setString(2, clase);
    		preparedStatement.setString(3, genero);
    		preparedStatement.setString(4, direccion);
    		preparedStatement.setString(5, fecha_nac);
    		preparedStatement.setString(6, raza);
    		preparedStatement.setString(7, pelaje);
    		preparedStatement.setString(8, prop_ci);
    		preparedStatement.setString(9, ciudad);
    		preparedStatement.setString(10, edad);
    		preparedStatement.executeUpdate();
    		disconnect();
    		return 0;
    	}catch (SQLException e){
    		disconnect();
    		e.printStackTrace();
    	}
    	
    	return 0;
    }

	//----------ISRAEL CHUCHUCA ------CODE

	public int insertAppointment( String cit_cedula,String cit_empleado,String cit_tipo,Date cit_fecha,
								  int cit_importancia, String observaciones){
		DBInserts dbInserts = new DBInserts();
		try {
			connect();
			System.out.println("conectado");
			statement = connection.createStatement();
			sql = "INSERT INTO citas_paciente set  cit_cedula = ?, cit_empleado = ?, cit_tipo = ?,"
					+ " cit_fecha = ?, cit_importancia = ?, observaciones = ?";
			preparedStatement = connection.prepareStatement(sql);
			System.out.println("ejecuto la sentencia");

			preparedStatement.setString(1, cit_cedula);
			preparedStatement.setString(2, cit_empleado);
			preparedStatement.setString(3, cit_tipo);
			preparedStatement.setDate(4, cit_fecha);
			preparedStatement.setInt(5, cit_importancia);
			preparedStatement.setString(6, observaciones);
			preparedStatement.executeUpdate();
			disconnect();
			return 0;
		}catch (SQLException e){
			disconnect();
			e.printStackTrace();
		}

		return 0;

	}

}
