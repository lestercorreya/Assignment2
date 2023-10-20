package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

import application.DatabaseConnection;
import dao.PostDaoImpl;
import dao.PostDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Post;

//class to handle all methods related to the RetrievePostsController
public class RetrieveNPostsController implements Initializable{
	@FXML
	private TableView<Post> postsTable;
	
	@FXML
	private TableColumn<Post, String> IDColumn, authorColumn, contentColumn, dateTimeColumn;
	
	@FXML
	private TableColumn<Post, Integer> likesColumn, sharesColumn;
	
	@FXML
	private TextField NField;
	
	@FXML
	private Label errorText;
	
	ObservableList<Post> posts = FXCollections.observableArrayList();
	
	public void openRetrieveNPosts(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/RetrieveNPosts.fxml"));
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/retrieveNPosts.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void retrievePosts(ActionEvent event) {
		posts.clear();
		errorText.setText(null);
		try {
			String N = NField.getText();
			
			//conducting field validations
			if (N.trim().length() == 0) {
				errorText.setText("All fields are mandatory");
				return;
			}
			
			String validNumberRegex = "^[0-9]+$";

			if (!N.matches(validNumberRegex)) {
				errorText.setText("N should be a valid number");
				return;
			}
			
			Connection conn = DatabaseConnection.getConnection();
			PostDao postDao = new PostDaoImpl(conn);
			
			ArrayList<Post> resultPosts = postDao.getPosts();
			
			//sorting the posts based on number of likes in the decending order
			Collections.sort(resultPosts, new Comparator<Post>() {
	            @Override
	            public int compare(Post post1, Post post2) {
	                return post2.getLikes() - post1.getLikes();
	            }
	        });
			
			ArrayList<Post> topNPosts = new ArrayList<>();
			
			//filtering out the top N posts with most likes
	        for (int i = 0; i < Integer.parseInt(N) && i < resultPosts.size(); i++) {
	            topNPosts.add(resultPosts.get(i));
	        }
	        
			conn.close();
			
			posts.addAll(topNPosts); //adding it to the observable lists so it can be displayed by the table
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
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//setting all the column attributes of the table
		IDColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("id"));
		authorColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("author"));
		contentColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("content"));
		likesColumn.setCellValueFactory(new PropertyValueFactory<Post, Integer>("likes"));
		sharesColumn.setCellValueFactory(new PropertyValueFactory<Post, Integer>("shares"));
		dateTimeColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("dateTime"));
		
		postsTable.setItems(posts); //setting the observable list to the table
	}
}
