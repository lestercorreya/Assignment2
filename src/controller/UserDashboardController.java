package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UserDashboardController {
	@FXML
	private Label usernameGreetingLabel;
	
	public void openUserDashboard(ActionEvent event, String username) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserDashboard.fxml"));
			Parent root = loader.load();
			UserDashboardController userDashboardController = loader.getController();
			userDashboardController.displayUsername(username);
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/userDashboard.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void displayUsername(String username) {
		usernameGreetingLabel.setText("Hello, " + username);
	}
}
