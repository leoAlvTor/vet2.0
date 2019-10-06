package application.resources.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBDelete {

    public DBDelete(){}
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
    
    public void deleteEmpleado(int empleado_id) {
    	try {
    		connect();
    		statement = connection.createStatement();
    		sql = "delete from Empleados where empleado_id = ?";
    		preparedStatement = connection.prepareStatement(sql);
    		preparedStatement.setInt(1, empleado_id);
    		preparedStatement.executeUpdate();
    		disconnect();
    	}catch(SQLException e) {
    		e.printStackTrace();
    		disconnect();
    	}
    }
    
    public void deleteRegistroMedico(int paciente_id) {
    	try {
    		connect();
    		statement = connection.createStatement();
    		sql = "delete from RegistroMedico where registro_id=?";
    		preparedStatement = connection.prepareStatement(sql);
    		preparedStatement.setInt(1, paciente_id);
    		preparedStatement.executeUpdate();
    		disconnect();
    	}catch(SQLException e) {
    		e.printStackTrace();
    		disconnect();
    	}
    }
    
    public void deletePatient(int paciente_id) {
    	try {
    		connect();
    		statement = connection.createStatement();
    		sql = "delete from Pacientes where paciente_id=?";
    		preparedStatement = connection.prepareStatement(sql);
    		preparedStatement.setInt(1, paciente_id);
    		preparedStatement.executeUpdate();
    		disconnect();
    	}catch(SQLException e) {
    		disconnect();
    		e.printStackTrace();
    	}
    }

    public boolean deleteProduct(String prod_id){
        try {
            connect();
            statement = connection.createStatement();
            sql = "delete from Productos where prod_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, prod_id);
            preparedStatement.executeUpdate();
            disconnect();
            return true;
        }catch (Exception e){
        	disconnect();
            System.out.println("Error al eliminar el producto...");
            return false;
        }

    }

    public boolean deleteProvider(String ruc){
        try{
            connect();
            statement = connection.createStatement();
            sql = "delete from Proveedores where prov_ruc=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, ruc);
            preparedStatement.executeUpdate();
            disconnect();
            return true;
        }catch (Exception e){
            System.out.println("Error al eliminar el proveedor...");
            return false;
        }
    }

}
