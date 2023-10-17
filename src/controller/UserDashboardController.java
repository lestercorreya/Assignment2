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
import model.User;

public class UserDashboardController implements Initializable {
	@FXML
	private Label usernameGreetingLabel;
	
	@FXML
	private TableView<User> table;
	
	@FXML
	private TableColumn<User, String> firstName;
	
	@FXML
	private TableColumn<User, String> lastName;
	
	ObservableList<User> list = FXCollections.observableArrayList(
				new User("sdjfkds", "sdkjf", "ksdjf", "skdjf", "sdjf")
			);
	
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		firstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
		lastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
		
		Connection conn = DatabaseConnection.getConnection();
		PostDao postDao = new PostDao(conn);
		try {
			ArrayList<String> posts = postDao.getPosts();
			System.out.println(posts.size());
			list.add(new User("jsdkf", "sdf", "skdjf", "jdsf", "sdf"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		table.setItems(list);
	}
}
