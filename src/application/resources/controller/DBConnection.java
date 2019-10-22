package application.resources.controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/MundoGanaderoMercy";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";

    public static Connection dbConnection(){
        Connection conn;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            return conn;
        } catch (Exception se) {
            se.printStackTrace();
            return null;
        }
    }
}
