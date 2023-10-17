package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Post;
import model.User;

public class PostDao {
	private Connection connection;

    public PostDao(Connection connection) {
        this.connection = connection;
    }
    
	public void createPost(Post post) throws SQLException {
        String query = "INSERT INTO posts (id, author, content, likes, shares, dateTime) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, post.getId());
        statement.setString(2, post.getAuthor().getUsername());
        statement.setString(3, post.getContent());
        statement.setInt(4, post.getLikes());
        statement.setInt(5, post.getShares());
        statement.setString(6, post.getDateTime());
        statement.executeUpdate();
        
        statement.close();
    }
	
	public ArrayList<String> getPosts() throws SQLException {
		ArrayList<String> posts = new ArrayList<>();
        String query = "SELECT * FROM posts";

        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        
        while (resultSet.next()) {
        	String author = resultSet.getString("author");
        	posts.add(author);
//        	String firstName = resultSet.getString("firstName");
//        	String lastName = resultSet.getString("lastName");
//        	String role = resultSet.getString("role");
//        	User user = new User(username, password, firstName, lastName, role);
//            statement.close();
//            return user;
        }
        
        return posts;
    }
}
