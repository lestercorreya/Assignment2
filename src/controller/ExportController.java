package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Post;

//this controller deals with all the methods for the export post to csv file page
public class ExportController {
	@FXML
	private TextField IDField;
	
	@FXML
	private Label errorText, successText;
	
	//method to open the export page
	public void openExport(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/Export.fxml"));
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/export.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//method to export the selected post to the csv file
	@FXML
	private void handleExport(ActionEvent event) {
		 errorText.setText(null);
		 successText.setText(null);
		 
		 try {
			 String ID = IDField.getText();
			
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
			 Post post = postDao.getPost(Integer.parseInt(ID));
			 
			 //checking if the post ID entered is invalid
			 if (post == null) {
				 errorText.setText("Entered Post ID is invalid!");
				 conn.close();
				 return;
			 }
			 
			 Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			 FileChooser fileChooser = new FileChooser();
			 File selectedFile = fileChooser.showSaveDialog(stage); //code to open the file saver dialog box
			 
             // returning if not file was entered or the entered file is not a valid csv file
			 if (selectedFile == null) {
				 return; 
			 } else if (!selectedFile.getName().endsWith(".csv")) {
				 errorText.setText("Enter a valid CSV file!");
				 return;
			 }
			
			 //forming the headers and rows 
			FileWriter fileWriter = new FileWriter(selectedFile);
		    StringBuilder csvData = new StringBuilder();
		    List<String> columnHeaders = List.of("ID", "content", "author", "likes", "shares", "dateTime");
		    List<String> columnRow = List.of(Integer.toString(post.getId()), post.getContent(), post.getAuthor().getUsername(), Integer.toString(post.getLikes()), Integer.toString(post.getShares()), post.getDateTime());
		    
		    //appending it the csv file
		    for (String item : columnHeaders) {
                csvData.append(item).append(",");
            }
	    	csvData.deleteCharAt(csvData.length() - 1);
	    	csvData.append("\n");
		    for (String item : columnRow) {
                csvData.append(item).append(",");
            }
	    	csvData.deleteCharAt(csvData.length() - 1);
	        fileWriter.write(csvData.toString());
	        
	        //closing the resources
	        fileWriter.close();
	        
	        successText.setText("Post exported Successfully!");
	        IDField.clear(); //clearing the ID field
			 
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
	
	//Method to go to user dashboard page when the back button is clicked
	@FXML
	private void handleBack(ActionEvent event) {
		UserDashboardController userDashboardController = new UserDashboardController();
		userDashboardController.openUserDashboard(event);
	}
}
