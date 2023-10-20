package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Post;

public interface PostDao {
	void createPost(Post post) throws SQLException;
	boolean deletePost(int ID, String username) throws SQLException;
	ArrayList<Post> getPosts() throws SQLException;
	Post getPost(int ID) throws SQLException;
	void changeUsername(String oldUsername, String newUsername) throws SQLException;
} 
