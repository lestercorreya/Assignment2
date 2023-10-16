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

public class LoginController {
	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Label errorText;
	
	@FXML
	private void handleSignupHyperlink(ActionEvent event) {
		SignupController signupController = new SignupController();
		signupController.openSignup(event);
	}
	
	public void openLogin(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void login(ActionEvent event) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			String username = usernameField.getText();
			String password = passwordField.getText();
			
			if (username.trim().length() == 0 || password.trim().length() == 0) {
				errorText.setText("All fields are mandatory!");
				return;
			}
			
			String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, password);

	        // Execute the query to insert the data
	        ResultSet resultSet = statement.executeQuery();
	        
            if (resultSet.next()) {
                UserDashboardController userDashboardController = new UserDashboardController();
                userDashboardController.openUserDashboard(event, username);
            } else {
                errorText.setText("Invalid login credentials.");
            }
	        
            
	        statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
