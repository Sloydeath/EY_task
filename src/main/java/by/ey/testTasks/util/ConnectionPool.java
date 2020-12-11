package by.ey.testTasks.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {

    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/EA_DATABASE?useUnicode=true&serverTimezone=UTC";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "1234";

    private ConnectionPool(){

    }

    private static ConnectionPool instance = null;

    public static ConnectionPool getInstance(){
        if (instance == null)
            instance = new ConnectionPool();
        return instance;
    }

    public Connection getConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error");
        }
        return connection;
    }
}