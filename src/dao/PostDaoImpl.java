package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Post;
import model.User;

//implementation of the PostDao interface
public class PostDaoImpl implements PostDao{
	private Connection connection;

    public PostDaoImpl(Connection connection) {
        this.connection = connection; //setting the database connection
    }
    
	public void createPost(Post post) throws SQLException {
		//query to add post to the database
        String query = "INSERT INTO posts (id, author, content, likes, shares, dateTime) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(query);
        //setting the values of the query
        statement.setInt(1, post.getId());
        statement.setString(2, post.getAuthor().getUsername());
        statement.setString(3, post.getContent());
        statement.setInt(4, post.getLikes());
        statement.setInt(5, post.getShares());
        statement.setString(6, post.getDateTime());
        statement.executeUpdate();
        
        statement.close();
    }
	
	public boolean deletePost(int ID, String username) throws SQLException {
        String query = "DELETE FROM posts WHERE ID = ? and author = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, ID);
        statement.setString(2, username);
        
        int rowsAffected = statement.executeUpdate();
        
        statement.close();
        if (rowsAffected > 0) {
        	return true;
        } else {
        	return false;        	
        }
    }
	
	public ArrayList<Post> getPosts() throws SQLException {
		ArrayList<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM posts";

        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        
        //looping through all the posts that were returned
        while (resultSet.next()) {
        	int ID = resultSet.getInt("ID");
        	String username = resultSet.getString("author");
        	String content = resultSet.getString("content");
        	int likes = resultSet.getInt("likes");
        	int shares = resultSet.getInt("shares");
        	String dateTime = resultSet.getString("dateTime");
        	
        	UserDaoImpl userDao = new UserDaoImpl(connection);
        	User author = userDao.getUser(username);
        	
        	posts.add(new Post(ID, author, content, likes, shares, dateTime));
        }
        
        statement.close();
        
        return posts;
    }
	
	public Post getPost(int ID) throws SQLException {
		String query = "SELECT * FROM posts WHERE ID = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, ID);
        ResultSet resultSet = statement.executeQuery();
        
        if (resultSet.next()) {
        	String username = resultSet.getString("author");
        	String content = resultSet.getString("content");
        	int likes = resultSet.getInt("likes");
        	int shares = resultSet.getInt("shares");
        	String dateTime = resultSet.getString("dateTime");
        	
        	UserDaoImpl userDao = new UserDaoImpl(connection);
        	User author = userDao.getUser(username);
        	
        	Post post = new Post(ID, author, content, likes, shares, dateTime);
        	
            statement.close();
            
            return post;
        }
        
        statement.close();
        
        return null;
	}
	
	//changing the usernames of the posts if the user updated the profile and changed the username
	public void changeUsername(String oldUsername, String newUsername) throws SQLException {
		String query = "UPDATE posts SET author = ? WHERE author = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, newUsername);
        statement.setString(2, oldUsername);
        statement.executeUpdate();
        
        statement.close();
	}
}
