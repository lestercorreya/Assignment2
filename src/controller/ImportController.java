package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import application.DatabaseConnection;
import dao.PostDaoImpl;
import dao.UserDaoImpl;
import dao.PostDao;
import dao.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.Post;
import model.User;

//class that handles all methods related to import posts from csv file page
public class ImportController {
	@FXML
	private TextField IDField;
	
	@FXML
	private Label errorText, successText;
	
	//method to open the import page
	public void openImport(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/Import.fxml"));
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/import.css").toExternalForm());
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
	private void handleImport(ActionEvent event) {
		errorText.setText(null);
		successText.setText(null);
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV files", "*.csv")); //setting filechooser filters to only show csv files
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		File selectedFile = fileChooser.showOpenDialog(stage);
		ArrayList<Post> posts = new ArrayList<>();
		
		if (selectedFile != null) {
            Connection conn = DatabaseConnection.getConnection();
            
            //logic to read contents from the csv file
			try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
				String line;
				boolean firstLine = true; 
	            while ((line = br.readLine()) != null) {
	            	if (firstLine) {
	                    firstLine = false;
	                    continue;
	                }
	            	
	                String[] fields = line.split(",");
	                
	            	String ID = fields[0];
	            	String content = fields[1];
	            	String author = fields[2];
	            	String likes = fields[3];
	            	String shares = fields[4];
	            	String dateTime = fields[5];
	            	
	            	String validNumberRegex = "^[0-9]+$";
	            	
	            	//conducting requried validations
	    			if (!ID.matches(validNumberRegex) || !likes.matches(validNumberRegex) || !shares.matches(validNumberRegex)) {
	    				errorText.setText("Invalid format error on post with ID: " + ID);
	    				conn.close();
	    				return;
	    			}
	    			
	            	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	                dateFormat.setLenient(false);

	                try {
	                    dateFormat.parse(dateTime);
	                } catch (ParseException e) {
	                	errorText.setText("Invalid format error on post with ID: " + ID);
	                	conn.close();
	    				return;
	                }
	                
	    			UserDao userDao = new UserDaoImpl(conn);
	    			
	    			User user = userDao.getUser(author);
	    			
	    			if (user == null) {
	    				errorText.setText("Author doesn't exist on post with ID: " + ID);
	    				conn.close();
	    				return;
	    			}
	                
	                Post post = new Post(Integer.parseInt(ID), user, content, Integer.parseInt(likes), Integer.parseInt(shares), dateTime);
	                
	                posts.add(post); //appeding the post to the posts collection
	            }
	            
    			PostDao postDao = new PostDaoImpl(conn);
    			
	            for (Post post : posts) {
                    postDao.createPost(post); //adding posts to the database
                }
	            
	            conn.close();
	            successText.setText("Posts imported Successfully!");
			} catch (IOException | SQLException e) {
				errorText.setText("An Error occured while trying to import posts!");
	            e.printStackTrace();
	        }
		}
	}
}
