package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import application.DatabaseConnection;
import dao.PostDaoImpl;
import dao.PostDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

//class that deals with all methods related to the remove post page
public class RemovePostController {
	@FXML
	private TextField IDField;
	
	@FXML
	private Label errorText, successText;
	
	public void openRemovePost(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/RemovePost.fxml"));
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/removePost.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void removePost(ActionEvent event) {
		errorText.setText(null);
		successText.setText(null);
		
		try {
			String ID = IDField.getText();
			String username = UserDashboardController.getUsername();
			
			//conducting field validations
			if (ID.trim().length() == 0) {
				errorText.setText("All fields are mandatory");
				return;
			}
			
			String validNumberRegex = "^[0-9]+$";

			if (!ID.matches(validNumberRegex)) {
				errorText.setText("Entered Post ID is invalid!");
				return;
			}
			
			Connection conn = DatabaseConnection.getConnection();
			PostDao postDao = new PostDaoImpl(conn);
			
			boolean deletionSuccess = postDao.deletePost(Integer.parseInt(ID), username); //deleting the post and returning boolean status of deletion
			
			if (!deletionSuccess) {
				errorText.setText("Post with this ID doesn't exist or the post doesn't belong to you!");
				conn.close();
				return;
			}
			
			conn.close();
			
			successText.setText("Post Deleted Successfully!");
			
			IDField.clear();
		} catch (SQLException e) {
			errorText.setText("An Error Occured!");
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleBack(ActionEvent event) {
		UserDashboardController userDashboardController = new UserDashboardController();
		userDashboardController.openUserDashboard(event);
	}
}
