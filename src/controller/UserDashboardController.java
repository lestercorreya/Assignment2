package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Post;

public class UserDashboardController implements Initializable {
	@FXML
	private Label usernameGreetingLabel;
	
	@FXML
	private TableView<Post> postsTable;
	
	@FXML
	private TableColumn<Post, String> IDColumn, authorColumn, contentColumn, dateTimeColumn;
	
	@FXML
	private TableColumn<Post, Integer> likesColumn, sharesColumn;
	
	ObservableList<Post> posts = FXCollections.observableArrayList();
	private static String username;
	
	public static String getUsername() {
		return username;
	}
	
	public static void setUsername(String username) {
		UserDashboardController.username = username;
	}
	
	public void openUserDashboard(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserDashboard.fxml"));
			Parent root = loader.load();
			UserDashboardController userDashboardController = loader.getController();
			userDashboardController.usernameGreetingLabel.setText("Hello, " + username);
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/userDashboard.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleAddPost(ActionEvent event) {
		AddPostController addPostController = new AddPostController();
		addPostController.openAddPost(event);
	}
	
	@FXML
	private void handleRetrievePost(ActionEvent event) {
		RetrievePostController retrievePostController = new RetrievePostController();
		retrievePostController.openRetrievePost(event);
	}
	
	@FXML
	private void handleRemovePost(ActionEvent event) {
		RemovePostController removePostController = new RemovePostController();
		removePostController.openRemovePost(event);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		IDColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("id"));
		authorColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("author"));
		contentColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("content"));
		likesColumn.setCellValueFactory(new PropertyValueFactory<Post, Integer>("likes"));
		sharesColumn.setCellValueFactory(new PropertyValueFactory<Post, Integer>("shares"));
		dateTimeColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("dateTime"));
		
		Connection conn = DatabaseConnection.getConnection();
		PostDao postDao = new PostDao(conn);
		
		try {
			ArrayList<Post> postsResult = postDao.getPosts();
			
			posts.addAll(postsResult);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		postsTable.setItems(posts);
	}
}
