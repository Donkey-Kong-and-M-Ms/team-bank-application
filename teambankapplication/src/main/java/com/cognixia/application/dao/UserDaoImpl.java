package com.cognixia.application.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognixia.application.repository.UserRepository;

import com.cognixia.application.model.User;

@Component
public class UserDaoImpl implements UserDao {

	@Autowired
	UserRepository userRepo;

	Connection conn;
	PreparedStatement stmt;
	ResultSet rs;

// database creds to use with custom(jdbc) methods
	// may need to remove the " jdbc:mysql: " portion
	private static final String URL = "jdbc:mysql:team-bank-database.c7lmsujwlyzy.us-east-2.rds.amazonaws.com";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root"; // Password#1

	// SQL queries to be used below
	static final String FIND_USER_BY_ID = "select * from DBUser where user_id=?";

	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", USERNAME, PASSWORD);

			return conn;

		} catch (Exception e) {

		}
		return conn;
	}

	// find the user by id, grabs all the info from the record,
	// and stores it into user which is returned
	@Override
	public User findUserById(int id) {

		User user = new User();

		try {

			conn = getConnection();
			stmt = conn.prepareStatement(FIND_USER_BY_ID);

			stmt.setInt(1, id);

			rs = stmt.executeQuery();

			while (rs.next()) {
				user = new User(rs.getInt("user_id"), rs.getString("first_name"), rs.getString("last_name"),
						rs.getString("address"), rs.getString("contact_number"), rs.getString("user_password"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}

}
