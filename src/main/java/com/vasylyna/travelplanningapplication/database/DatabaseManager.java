package com.vasylyna.travelplanningapplication.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL = "jdbc:postgresql://localhost:5432/travel_planner";
    private static final String USER = "postgres";
    private static final String PASSWORD = "WDJWhAqGPIFN2m";

    private static DatabaseManager instance;
    private static Connection connection;

    private DatabaseManager() throws SQLException{
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static DatabaseManager getInstance() throws SQLException{
        if (instance == null || instance.getConnection().isClosed()){
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection(){
        return connection;
    }
}
