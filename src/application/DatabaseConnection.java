package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	public static Connection getConnection() {
        try {
            String url = "jdbc:sqlite:Assignment2.db";
            Connection conn = DriverManager.getConnection(url);
            return conn;
        } catch (SQLException e) {
            return null;
        }
    }
}
