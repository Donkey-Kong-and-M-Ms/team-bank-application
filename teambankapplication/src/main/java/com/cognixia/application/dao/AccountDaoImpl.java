package com.cognixia.application.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import org.springframework.stereotype.Component;

import com.cognixia.application.model.Account;
import com.cognixia.application.model.User;

@Component
public class AccountDaoImpl implements AccountDao {

	// name of DB table may change
	private static final String UPDATE_BALANCE = "UPDATE DBAccount SET account_value = ? where account_id = ?";

	private static final String ACCOUNT_BY_USER_ID = "SELECT * FROM DBAccount WHERE user_id = ?";

	private static final String ACCOUNT_BALANCE_BY_ACCOUNT_ID = "SELECT account_value FROM DBAccount WHERE account_id = ?";

	// database creds to use with custom(jdbc) methods
	// may need to remove the " jdbc:mysql: " portion
	private static final String URL = "jdbc:mysql:team-bank-database.c7lmsujwlyzy.us-east-2.rds.amazonaws.com";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root"; //Password#1

	Connection conn;
	PreparedStatement stmt;
	ResultSet rs;

	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			// LOCALHOST URL IS FOR TESTING ONLY
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", USERNAME, PASSWORD);

			return conn;

		} catch (Exception e) {

		}
		return conn;
	}

	@Override
	public int updateBalance(float userBalance, int accId) {

		try {
			// connect to DB through JDBC
			conn = getConnection();

			// primes update balance statement
			stmt = conn.prepareStatement(UPDATE_BALANCE);

			// sets userBal as the new number
			stmt.setFloat(1, userBalance);
			// sets new bal according to Account ID
			stmt.setInt(2, accId);

			// executes statement
			stmt.executeUpdate();

			// closes connection to DB. used for best practice
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public Account getAccountByUserId(int userId) {
		
		Account acc = new Account();

		try {
			conn = getConnection();

			stmt = conn.prepareStatement(ACCOUNT_BY_USER_ID);

			stmt.setInt(1, userId);
			
			rs = stmt.executeQuery();

			while (rs.next()) {
				acc = new Account(rs.getInt("account_id"), rs.getInt("user_id"), rs.getString("account_type"),
						rs.getFloat("account_value"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return acc;
	}

	@Override
	public float getBalanceByAccountId(int accId) {
		try {

			conn = getConnection();

			stmt = conn.prepareStatement(ACCOUNT_BALANCE_BY_ACCOUNT_ID);

			stmt.setInt(1, accId);
			
			stmt.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

}
