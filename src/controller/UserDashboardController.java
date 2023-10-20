package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.DatabaseConnection;
import dao.PostDaoImpl;
import dao.UserDaoImpl;
import dao.UserDao;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Post;
import model.User;

//controller that deals with all the methods related to the user dashboard page
public class UserDashboardController implements Initializable {
	@FXML
	private Label usernameGreetingLabel;
	
	@FXML
	private TableView<Post> postsTable;
	
	@FXML
	private TableColumn<Post, String> IDColumn, authorColumn, contentColumn, dateTimeColumn;
	
	@FXML
	private TableColumn<Post, Integer> likesColumn, sharesColumn;
	
	@FXML
	private Button visualizeButton, upgradeButton, importButton;
	
	ObservableList<Post> posts = FXCollections.observableArrayList();
	private static String username;
	
	public static String getUsername() {
		return username;
	}
	
	//method to set the username as a static variable so the entire application can use it
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
	
	//handlers for all the funtionalities
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
	
	@FXML
	private void handleRetrieveNPosts(ActionEvent event) {
		RetrieveNPostsController retrieveNPostsController = new RetrieveNPostsController();
		retrieveNPostsController.openRetrieveNPosts(event);
	}
	
	@FXML
	private void handleEditProfile(ActionEvent event) {
		EditProfileController editProfileController = new EditProfileController();
		editProfileController.openEditProfile(event);
	}
	
	@FXML
	private void handleLogout(ActionEvent event) {
		UserDashboardController.setUsername(null);
		LoginController loginController = new LoginController();
		loginController.openLogin(event);
	}
	
	@FXML
	private void handleUpgrade(ActionEvent event) {
		UpgradeController upgradeController = new UpgradeController();
		upgradeController.openUpgrade(event);
	}
	
	@FXML
	private void handleExport(ActionEvent event) {
		ExportController exportController = new ExportController();
		exportController.openExport(event);
	}
	
	@FXML
	private void handleVisualize(ActionEvent event) {
		VisualizeController visualizeController = new VisualizeController();
		visualizeController.openVisualize(event);
	}
	
	@FXML
	private void handleImport(ActionEvent event) {
		ImportController importController = new ImportController();
		importController.openImport(event);
	}
	
	//initializing the page by populating the tableview with all the available posts
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			
			UserDao userDao = new UserDaoImpl(conn);
			User user = userDao.getUser(username);
			
			if (user.getRole().equals("user")) {
				visualizeButton.setVisible(false);
				importButton.setVisible(false);
				
			} else {
				upgradeButton.setVisible(false);
			}
			IDColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("id"));
			authorColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("authorUsername"));
			contentColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("content"));
			likesColumn.setCellValueFactory(new PropertyValueFactory<Post, Integer>("likes"));
			sharesColumn.setCellValueFactory(new PropertyValueFactory<Post, Integer>("shares"));
			dateTimeColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("dateTime"));
			
			PostDao postDao = new PostDaoImpl(conn);
			
			ArrayList<Post> postsResult = postDao.getPosts();
			
			posts.addAll(postsResult);
			
			postsTable.setItems(posts);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
