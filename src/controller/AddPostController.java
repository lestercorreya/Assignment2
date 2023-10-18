package controller;

import java.util.UUID;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import application.DatabaseConnection;
import dao.PostDao;
import dao.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Post;
import model.User;

public class AddPostController {
	@FXML
	private TextField likesField, sharesField;
	@FXML
	private TextArea contentField; 
	@FXML
	private Label errorText, successText;
	
	public void openAddPost(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/AddPost.fxml"));
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/addPost.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void addPost(ActionEvent event) {
		try {
			errorText.setText(null);
			successText.setText(null);
			
			String content = contentField.getText();
			String likes = likesField.getText();
			String shares = sharesField.getText();
			
			if (content.trim().length() == 0 || likes.trim().length() == 0 || shares.trim().length() == 0) {
				errorText.setText("All fields are mandatory");
				return;
			}
			
			String validNumberRegex = "^[0-9]+$";

			if (!likes.matches(validNumberRegex)) {
				errorText.setText("Likes should be a valid number");
				return;
			}
			
			if (!shares.matches(validNumberRegex)) {
				errorText.setText("Likes should be a valid number");
				return;
			}
			
			LocalDateTime dateTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        String formattedDateTime = dateTime.format(formatter);
	        
			Connection conn = DatabaseConnection.getConnection();
			UserDao userDao = new UserDao(conn);
			User user = userDao.getUser(UserDashboardController.getUsername());
			PostDao postDao = new PostDao(conn);
			
			UUID uniqueID = UUID.randomUUID();
			
		    Post post = new Post(uniqueID.toString(), user, content, Integer.parseInt(likes), Integer.parseInt(shares), formattedDateTime);
		    postDao.createPost(post);
		    
		    conn.close();
	        
	        successText.setText("Post Added Successfully!");
	        emptyFields();
	        
		} catch (SQLException e) {
			errorText.setText("There was an error in Signing Up. Please try again!");
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleBack(ActionEvent event) {
		UserDashboardController userDashboardController = new UserDashboardController();
		userDashboardController.openUserDashboard(event);
	}
	
	private void emptyFields() {
		contentField.clear();
        likesField.clear();
        sharesField.clear();
        
        contentField.requestFocus();
	}
}
