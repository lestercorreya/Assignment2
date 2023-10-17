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
	
	public ArrayList<Post> getPosts() throws SQLException {
		ArrayList<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM posts";

        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        
        while (resultSet.next()) {
        	String ID = resultSet.getString("ID");
        	String username = resultSet.getString("author");
        	String content = resultSet.getString("content");
        	int likes = resultSet.getInt("likes");
        	int shares = resultSet.getInt("shares");
        	String dateTime = resultSet.getString("dateTime");
        	
        	UserDao userDao = new UserDao(connection);
        	User author = userDao.getUser(username);
        	
        	posts.add(new Post(ID, author, content, likes, shares, dateTime));
        }
        
        return posts;
    }
}
