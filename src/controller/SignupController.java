package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignupController {
    //	claiming access to the fxml variables
	@FXML
	private TextField usernameField, firstNameField, lastNameField;
	@FXML
	private PasswordField passwordField, confirmPasswordField;
	@FXML
	private Label errorText;
	
	private LoginController loginController;
	
	// functions
	
	public void openSignup(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/Signup.fxml"));
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/signup.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void signup(ActionEvent event) {
		try {
			//establishing connection to the database
			Connection conn = DatabaseConnection.getConnection();
			String username = usernameField.getText();
			String firstName = firstNameField.getText();
			String lastName = lastNameField.getText();
			String password = passwordField.getText();
			String confirmPassword = confirmPasswordField.getText();
			
			if (username.trim().length() == 0 || firstName.trim().length() == 0 || lastName.trim().length() == 0 || password.trim().length() == 0 || confirmPassword.trim().length() == 0) {
				errorText.setText("All fields are mandatory");
				return;
			}
			
			// check if the 2 passwords entered are the same
			if (!password.equals(confirmPassword)) {
				errorText.setText("Passwords don't match!");
				return;
			}
			
			if (password.length() < 5) {
				errorText.setText("Password should be atleast 5 characters");
				return;
			}
			
			String sql = "SELECT * FROM users WHERE username = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, username);

	        // Execute the query to insert the data
	        ResultSet resultSet = statement.executeQuery();

	        if (resultSet.next()) {
                errorText.setText("username already exists!");
                statement.close();
                return;
            }
	        
			sql = "INSERT INTO users (username, password, firstName, lastName, role) VALUES (?, ?, ?, ?, ?)";
			statement = conn.prepareStatement(sql);
			statement.setString(1, username);
	        statement.setString(2, password);
	        statement.setString(3, firstName);
	        statement.setString(4, lastName);
	        statement.setString(5, "user");

	        // Execute the query to insert the data
	        statement.executeUpdate();

	        // Close the resources
	        statement.close();
	        
	        loginController.openLogin(event);
	        
		} catch (SQLException e) {
			errorText.setText("There was an error in Signing Up. Please try again!");
			e.printStackTrace();
		}
	}
	
	
	@FXML
	private void handleLoginHyperlink(ActionEvent event) {
		loginController = new LoginController();
		loginController.openLogin(event);
	}
}
