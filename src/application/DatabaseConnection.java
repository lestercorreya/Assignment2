package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//this class sets up the database connection using JDBC with a sqlite database
public class DatabaseConnection {
	public static Connection getConnection() {
        try {
            String url = "jdbc:sqlite:Assignment2.db"; //db name is Assignment2.db
            Connection conn = DriverManager.getConnection(url);
            return conn;
        } catch (SQLException e) {
            return null;
        }
    }
}
