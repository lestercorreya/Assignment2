package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import application.DatabaseConnection;
import dao.UserDaoImpl;
import dao.UserDao;
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
import model.User;

//controller that deals with all methods related to the signup page
public class SignupController {
    //	claiming access to the fxml variables
	@FXML
	private TextField usernameField, firstNameField, lastNameField;
	@FXML
	private PasswordField passwordField, confirmPasswordField;
	@FXML
	private Label errorText;
	
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
			String username = usernameField.getText();
			String firstName = firstNameField.getText();
			String lastName = lastNameField.getText();
			String password = passwordField.getText();
			String confirmPassword = confirmPasswordField.getText();
			
			//conducting field validations
			if (username.trim().length() == 0 || firstName.trim().length() == 0 || lastName.trim().length() == 0 || password.trim().length() == 0 || confirmPassword.trim().length() == 0) {
				errorText.setText("All fields are mandatory");
				return;
			}
			
			if (!password.equals(confirmPassword)) {
				errorText.setText("Passwords don't match!");
				return;
			}
			
			if (password.length() < 5) {
				errorText.setText("Password should be atleast 5 characters");
				return;
			}
			
			Connection conn = DatabaseConnection.getConnection();
		    UserDao userDao = new UserDaoImpl(conn);
		    
		    if (userDao.getUser(username) != null) {
		    	errorText.setText("A user with this username already exists!");
		    	conn.close();
				return;
		    }
			
		    //creating the user and adding the user to the database
		    User user = new User(username, password, firstName, lastName, "user");
		    userDao.createUser(user);
		    
		    conn.close();
	        
		    //opening the login page after signup
	        LoginController loginController = new LoginController();
			loginController.openLogin(event);
	        
		} catch (SQLException e) {
			errorText.setText("There was an error in Signing Up. Please try again!");
			e.printStackTrace();
		}
	}
	
	//opening the login page with the login hyperlink is clicked
	@FXML
	private void handleLoginHyperlink(ActionEvent event) {
		LoginController loginController = new LoginController();
		loginController.openLogin(event);
	}
}
