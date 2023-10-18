package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import application.DatabaseConnection;
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
			
			if (ID.trim().length() == 0) {
				errorText.setText("All fields are mandatory");
				return;
			}
			
			Connection conn = DatabaseConnection.getConnection();
			PostDao postDao = new PostDao(conn);
			
			boolean deletionSuccess = postDao.deletePost(ID);
			
			if (!deletionSuccess) {
				errorText.setText("Post with this ID doesn't exist!");
				conn.close();
				return;
			}
			
			conn.close();
			
			successText.setText("Post Deleted Successfully!");
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
