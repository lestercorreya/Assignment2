package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.DatabaseConnection;
import dao.PostDao;
import dao.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

public class EditProfileController implements Initializable{
	@FXML
	private TextField usernameField, firstNameField, lastNameField;
	
	@FXML
	private PasswordField passwordField, confirmPasswordField;
	
	@FXML
	private Label errorText, successText;
	
	public void openEditProfile(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/EditProfile.fxml"));
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/editProfile.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void editProfile(ActionEvent event) {
		try {
			String username = usernameField.getText();
			String oldUsername = UserDashboardController.getUsername();
			String firstName = firstNameField.getText();
			String lastName = lastNameField.getText();
			String password = passwordField.getText();
			String confirmPassword = confirmPasswordField.getText();
			
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
		    UserDao userDao = new UserDao(conn);
		    PostDao postDao = new PostDao(conn);
			
			if (!username.equals(oldUsername)) {
			    if (userDao.getUser(username) != null) {
			    	errorText.setText("A user with this username already exists!");
			    	conn.close();
					return;
			    }
			    
			    postDao.changeUsername(oldUsername, username);
			}
			
		    User oldUser = userDao.getUser(oldUsername);
		    User user = new User(username, password, firstName, lastName, oldUser.getRole());
		    userDao.updateUser(user, oldUsername);
		    
		    conn.close();
	        
		    UserDashboardController.setUsername(username);
	        successText.setText("Profile details are in sync!");
	        
		} catch (SQLException e) {
			errorText.setText("There was an error in Editing the profile. Please try again!");
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleBack(ActionEvent event) {
		UserDashboardController userDashboardController = new UserDashboardController();
		userDashboardController.openUserDashboard(event);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		String username = UserDashboardController.getUsername();
		try {
			Connection conn = DatabaseConnection.getConnection();
			
			UserDao userDao = new UserDao(conn);
			User user = userDao.getUser(username);
			
			usernameField.setText(username);
			firstNameField.setText(user.getFirstName());
			lastNameField.setText(user.getLastName());
			passwordField.setText(user.getPassword());
			confirmPasswordField.setText(user.getPassword());
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
