package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.stage.Stage;
import model.Post;

//controller to handle all the methods related to the visualize page
public class VisualizeController implements Initializable{
	@FXML
	private PieChart pieChart;
	
	public ObservableList<Data> sharesData = FXCollections.observableArrayList();
	
	public void openVisualize(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/Visualize.fxml"));
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/visualize.css").toExternalForm());
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
	
	//initializing the page with a pie chart that displays the distribution of shares for all the posts into 3 categories
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			
			PostDao postDao = new PostDaoImpl(conn);
			
			ArrayList<Post> postsResult = postDao.getPosts();
			
			int postsWithShares0to99 = 0;
			int postsWithShares100to999 = 0;
			int postsWithShares999andAbove = 0;

			for (Post post : postsResult) {
			    int shares = post.getShares();
			    
			    if (shares >= 0 && shares <= 99) {
			        postsWithShares0to99++;
			    } else if (shares >= 100 && shares <= 999) {
			        postsWithShares100to999++;
			    } else if (shares >= 1000) {
			        postsWithShares999andAbove++;
			    }
			}
			
			sharesData.add(new PieChart.Data("Posts having shares from 0-99", postsWithShares0to99));
			sharesData.add(new PieChart.Data("Posts having shares from 100-999", postsWithShares100to999));
			sharesData.add(new PieChart.Data("Posts having shares 999 and above", postsWithShares999andAbove));
			
			pieChart.setData(sharesData);
			pieChart.setLabelsVisible(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
