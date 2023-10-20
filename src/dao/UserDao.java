package dao;

import java.sql.SQLException;

import model.User;

public interface UserDao {
	void createUser(User user) throws SQLException;
	void updateUser(User user, String username) throws SQLException;
	User getUser(String username) throws SQLException;
	User getUser(String username, String password) throws SQLException;
}
