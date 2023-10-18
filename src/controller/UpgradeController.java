package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import application.DatabaseConnection;
import dao.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.User;

public class UpgradeController {
	@FXML
	private Label errorText;
	
	public void openUpgrade(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/Upgrade.fxml"));
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/upgrade.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleBack(ActionEvent event) {
		UserDashboardController userDashboardController = new UserDashboardController();
		userDashboardController.openUserDashboard(event);
	}

	@FXML
	private void handleUpgrade(ActionEvent event) {
		try {
			String username = UserDashboardController.getUsername();
			
			Connection conn = DatabaseConnection.getConnection();
			
			UserDao userDao = new UserDao(conn);
			User user = userDao.getUser(username);
			
			User updatedUser = new User(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), "vip");
			userDao.updateUser(updatedUser, username);
			
			UserDashboardController.setUsername(null);
			LoginController loginController = new LoginController();
			loginController.openLogin(event);
		} catch (SQLException e) {
			errorText.setText("There was an error in Upgrading!");
		}
	}

}
