package com.Bazar.doacoes.model.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String URL = "jdbc:mysql://localhost:3306/bazar";
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    private static Connection currentConnection = null;

    public static Connection getCurrentConnection() throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.cj.jdbc.Driver");

        if(currentConnection == null){
            currentConnection = DriverManager.getConnection(URL, USER, PASSWORD);
            currentConnection.setAutoCommit(true);
        }
        return currentConnection;
    }

    public static Connection getConnection() throws SQLException{

        return DriverManager.getConnection(URL, USER, PASSWORD);

    }
}
