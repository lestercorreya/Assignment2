package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Post;

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
}
