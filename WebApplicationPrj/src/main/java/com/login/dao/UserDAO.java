package com.login.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.model.User;

public class UserDAO {
	private String jdbcURL = "jdbc:mysql://localhost:3306/logging?useSSL=false";
	private String jdbcUsername = "root";
	private String jdbcPassword = "123456789";
	private static final String INSERT_USERS_SQL = "INSERT INTO users" + " (name, email, phoneno) VALUES "
			+ " (?, ?, ?);";
	private static final String SELECT_USER_BY_ID = "select SNo,id,name,email,phoneno from users where id =?";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
	private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, phoneno =? where id = ?;";
	private static final int SNo = 0;

	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

//insert
	public void insertUser(User user) throws SQLException {
		System.out.println(INSERT_USERS_SQL);
// try-with-resource statement will auto close the connection.
		try (Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setInt(3, user.getPhoneno());
//System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//update
	public boolean updateUser(User user) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
			System.out.println("updated USer:" + statement);
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setInt(3, user.getPhoneno());
			statement.setInt(4, user.getId());
			rowUpdated = statement.executeUpdate() > 0;
		}
		return rowUpdated;
	}

//select by id
	public User selectUser(int id) {
		User user = null;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
			preparedStatement.setInt(1, id);
			System.out.println("preparedStatement");
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				int phoneno = rs.getInt("phoneno");
				user = new User( id, name, email, phoneno);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

//select all users
	public List<User> selectAllUsers() { // using try-with-resources to avoid closing resources (boiler plate code)
		List<User> users = new ArrayList<>();
// Step 1: Establishing a Connection
		try (Connection connection = getConnection(); // Step 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
			System.out.println(preparedStatement);
// Step 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery(); // Step 4: Process the ResultSet object.
			while (rs.next()) {
				int id = rs.getInt("id");

				String name = rs.getString("name");
				String email = rs.getString("email");
				int phoneno = rs.getInt("phoneno");
				users.add(new User( id, name, email, phoneno));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

//delete
	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}
}
