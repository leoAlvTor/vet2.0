package application.resources.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.CallableStatement;

@SuppressWarnings("ALL")
public class DBUpdates {

    public DBUpdates(){}

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
    
    public String devolverProducto(String ...params) {
    	try {
    		
    		java.sql.CallableStatement cStatement = connection.prepareCall("{call procedure_return_productos(?,?)}");
    		cStatement.setString(1, params[0]);
    		cStatement.setDouble(2, Double.parseDouble(params[1]));
    		cStatement.execute();
    		return "";
    	}catch(SQLException e) {
    		 System.out.println(e.getMessage());
    		 return e.getMessage();
    	}
    }
    
    public String anularFactura(Object param) {
    	sql = "update factura_cabecera set factura_anulada = 'TRUE' where factura_id = ?";
    	try {
    		connect();
    		preparedStatement = connection.prepareStatement(sql);
    		
    		preparedStatement.setInt(1, Integer.parseInt(String.valueOf(param)));
    		
    		preparedStatement.executeUpdate();
    		

    		return "";
    	}catch(SQLException e) {
    		disconnect();
    		return e.getMessage();
    	}
    }
    
    public String updateFacturaCabecera(Object ...params) {
    	sql = "update factura_cabecera set cliente_ruc = ?, factura_tipo = ?, factura_subtotal = ?,"
    			+ " factura_iva = ?, factura_descuento = ?, factura_total = ? where factura_id = ?";
    	try {
    		connect();
    		preparedStatement = connection.prepareStatement(sql);
    		
    		preparedStatement.setString(1, String.valueOf(params[0]));
    		preparedStatement.setString(2, String.valueOf(params[1]));
    		
    		preparedStatement.setDouble(3, Double.parseDouble(String.valueOf(params[2])));
    		preparedStatement.setDouble(4, Double.parseDouble(String.valueOf(params[3])));
    		preparedStatement.setDouble(5, Double.parseDouble(String.valueOf(params[4])));
    		preparedStatement.setDouble(6, Double.parseDouble(String.valueOf(params[5])));
    		
    		preparedStatement.setInt(7, Integer.parseInt(String.valueOf(params[6])));
    		
    		preparedStatement.executeUpdate();
    		disconnect();
    		return "";
    	}catch(SQLException e) {
    		return e.getMessage();
    	}
    }
    
    public void updateEmpleado(int id, String nombre, String titulo, String email, String telefono, String direccion) {
    	try {
    		connect();
    		statement = connection.createStatement();
    		sql = "update Empleados set empleado_nombres = ?, empleado_titulo = ?, empleado_email = ?, empleado_telefono = ?, empleado_direccion = ? where empleado_id = ?";
    		preparedStatement = connection.prepareStatement(sql);
    		preparedStatement.setString(1, nombre);
    		preparedStatement.setString(2, titulo);
    		preparedStatement.setString(3, email);
    		preparedStatement.setString(4, telefono);
    		preparedStatement.setString(5, direccion);
    		preparedStatement.setInt(6, id);
    		preparedStatement.executeUpdate();
    		disconnect();
    	}catch(SQLException e){
    		e.printStackTrace();
    		disconnect();
    		
    	}
    }
    
    public void updateRegistro(int id, String fecha, String hora, String next_fecha, String tipo, String descripcion, int paciente_id) {
    	try {
    		connect();
    		statement = connection.createStatement();
    		sql = "update RegistroMedico SET registro_fecha = ?, registro_hora = ?, registro_next_fecha = ?, "
    				+ "registro_tipo = ?, registro_descripcion = ?, registro_paciente_id = ? where registro_id = ?";
    		preparedStatement = connection.prepareStatement(sql);
    		preparedStatement.setString(1, fecha);
    		preparedStatement.setString(2, hora);
    		preparedStatement.setString(3, next_fecha);
    		preparedStatement.setString(4, tipo);
    		preparedStatement.setString(5, descripcion);
    		preparedStatement.setInt(6, paciente_id);
    		preparedStatement.setInt(7, id);
    		preparedStatement.executeUpdate();
    		disconnect();
    	}catch(SQLException e) {
    		disconnect();
    		e.printStackTrace();
    	}
    }
    
    public void updatePatient(int id, String nombre, String clase, String genero, String direccion, String fecha_nac,
    		String raza, String pelaje, String prop_ci, String ciudad, String edad) {
    	
    	try {
    		connect();
    		
    		statement = connection.createStatement();
    		sql = "update Pacientes set paciente_nombre = ?, paciente_clase = ?, paciente_genero = ?, paciente_direccion = ?,"
    				+ " paciente_fecha_nac = ?, paciente_raza = ?, paciente_pelaje = ?, paciente_prop_ci = ?, paciente_ciudad = ?, paciente_edad = ? where paciente_ID = ?";
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
    		preparedStatement.setInt(11, id);
    		preparedStatement.executeUpdate();
    		disconnect();
    	}catch(SQLException e) {
    		disconnect();
    		e.printStackTrace();
    	}
    	
    }

    public void updateProduct(String old_id, String id, String main_id, String name, String main_name, String fecha, String prov_RUC,
                              String category, String representation, int internal_quantity, double stock, double box_price,
                              double unit_price, String tax, double box_12, double unit_12, double sell_box, double sell_unit){
        try{
            connect();
            statement = connection.createStatement();
            sql = "update Productos set prod_id=?, prod_main_id=?, prod_name=?, prod_main_name=?, prod_fecha=?, " +
                    "prov_RUC=?, prod_category=?, prod_representation=?, prod_int_quantity=?, prod_stock=?," +
                    " prod_box_price=?, prod_unit_price=?, prod_tax=?, prod_box_12=?, prod_unit_12=?, prod_sell_box =?," +
                    " prod_sell_unit=?  where prod_id = ?";
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
            preparedStatement.setString(18, old_id);
            preparedStatement.executeUpdate();
            disconnect();
        }catch (SQLException e){
        	disconnect();
            System.out.println("Error al actualizar el producto...");
        }
    }

    public void updateProvider(String oldRUC, String ruc, String nombre, String direccion, String telefono, String email, String observaciones){
        try{
            connect();
            statement = connection.createStatement();
            sql = "update Proveedores set prov_ruc=?, prov_nombre=?, prov_direccion=?, prov_telefono=?, prov_email=?," +
                    " prov_observaciones=? where prov_ruc = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, ruc);
            preparedStatement.setString(2, nombre);
            preparedStatement.setString(3, direccion);
            preparedStatement.setString(4, telefono);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, observaciones);
            preparedStatement.setString(7, oldRUC);
            preparedStatement.executeUpdate();
            disconnect();
        }catch (Exception e){
        	disconnect();
            System.out.println("Error al actualizar el proveedor...");
        }

    }

}
