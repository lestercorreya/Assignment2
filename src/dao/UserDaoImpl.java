package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

//implementation of the UserDao interface
public class UserDaoImpl implements UserDao{
	private Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }
    
	public void createUser(User user) throws SQLException {
		//query to creatre the user
        String query = "INSERT INTO users (username, password, firstName, lastName, role) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getFirstName());
        statement.setString(4, user.getLastName());
        statement.setString(5, user.getRole());
        statement.executeUpdate();
        
        statement.close();
    }
	
	public void updateUser(User user, String username) throws SQLException {
		//query to update the user
		String query = "UPDATE users SET username = ?, password = ?, firstName = ?, lastName = ?, role = ? WHERE username = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getFirstName());
        statement.setString(4, user.getLastName());
        statement.setString(5, user.getRole());
        statement.setString(6, username);
        statement.executeUpdate();
        
        statement.close();
	}
	
	public User getUser(String username) throws SQLException {
		//query to get user with username
        String query = "SELECT * FROM users WHERE username = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        
        if (resultSet.next()) {
        	String password = resultSet.getString("password");
        	String firstName = resultSet.getString("firstName");
        	String lastName = resultSet.getString("lastName");
        	String role = resultSet.getString("role");
        	User user = new User(username, password, firstName, lastName, role);
            statement.close();
            return user;
        }
        
        return null;
    }
	
	public User getUser(String username, String password) throws SQLException {
		//query to get user with username and password
        String query = "SELECT * FROM users WHERE username = ? and password = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        
        if (resultSet.next()) {
        	String firstName = resultSet.getString("firstName");
        	String lastName = resultSet.getString("lastName");
        	String role = resultSet.getString("role");
        	User user = new User(username, password, firstName, lastName, role);
            statement.close();
            return user;
        }
        
        return null;
    }
}
