package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnect {

    static final String URL = "jdbc:mysql://localhost:3306/car";
    static final String USER = "root";
    static final String PASSWORD = "123456ja";

    private Connection connection;

    static dbConnect instance;

    private dbConnect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static dbConnect getInstance() {
        if (instance == null)
            instance = new dbConnect();

        return instance;
    }

}