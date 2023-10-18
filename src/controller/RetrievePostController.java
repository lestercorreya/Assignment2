package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.DatabaseConnection;
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

public class RetrievePostController implements Initializable {
	@FXML
	private TableView<Post> postsTable;
	
	@FXML
	private TableColumn<Post, String> IDColumn, authorColumn, contentColumn, dateTimeColumn;
	
	@FXML
	private TableColumn<Post, Integer> likesColumn, sharesColumn;
	
	@FXML
	private TextField IDField;
	
	@FXML
	private Label errorText;
	
	ObservableList<Post> posts = FXCollections.observableArrayList();
	
	public void openRetrievePost(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/RetrievePost.fxml"));
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/retrievePost.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void retrievePost(ActionEvent event) {
		errorText.setText(null);
		try {
			Connection conn = DatabaseConnection.getConnection();
			PostDao postDao = new PostDao(conn);
			
			Post post = postDao.getPost(IDField.getText());
			
			if (post == null) {
				errorText.setText("Post with this ID doesn't exist!");
				conn.close();
				return;
			}
			
			conn.close();
			
			posts.add(post);
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
		IDColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("id"));
		authorColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("author"));
		contentColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("content"));
		likesColumn.setCellValueFactory(new PropertyValueFactory<Post, Integer>("likes"));
		sharesColumn.setCellValueFactory(new PropertyValueFactory<Post, Integer>("shares"));
		dateTimeColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("dateTime"));
		
		postsTable.setItems(posts);
	}
}
