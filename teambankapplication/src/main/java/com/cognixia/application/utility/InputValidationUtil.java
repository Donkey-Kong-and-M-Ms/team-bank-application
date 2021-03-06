package com.cognixia.application.utility;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cognixia.application.model.Account;
import com.cognixia.application.model.User;
import com.cognixia.application.repository.UserRepository;

public class InputValidationUtil {

	@Autowired
	UserRepository userRepo;

	// Check that the account has enough funds
	public static boolean sufficientFunds(float amount, Account account) {
		if (amount < account.getBalance()) {
			return true;
		} else {
			return false;
		}
	}

	// Check if the given number is positive
	public static boolean positiveNumber(float amount) {
		if (amount > 0) {
			return true;
		} else {
			return false;
		}
	}

	// Check if the given user exists in the repository
	public static boolean userExists(int userid, UserRepository userRepo) {
		if (userRepo.existsById(userid)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean validPhoneNum(String phoneNum) {
		// checks to see if there is a format of (XXX)XXX-XXXX
		if (phoneNum.matches("\\(\\d{3}\\)\\d{3}-\\d{4}")) {
			return true;
		} else {
			return false;
		}
	}

	// password validation
	// criteria - at least: 1 number, 1 lower case, 1 upper case, 1 special
	// character, 8-30 characters long
	public static boolean validPassword(String password) {
		if (password.matches("(?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!*]).{8,30}")) {
			return true;
		} else
			return true;
	}

	// iterates through all users and checks whether userName and Password entered
	// match
	// an existing user
	public static boolean validLogin(int userid, String password, UserRepository userRepo) {
		List<User> uList = (List<User>) userRepo.findAll();

		for (User user : uList) {
			if (user.getUserId().equals(userid) && user.getPassword().equals(password)) {
				return true;
			}

		}
		return false;
	}

	// Checks the account names are only "checkings" or "savings"
	// functionality can be added later for more accounts to be created
	public static boolean validAccountType(String accountType) {
		if (accountType.equalsIgnoreCase("Checkings") | accountType.equalsIgnoreCase("Savings")) {
			return true;
		} else {
			return false;
		}
	}
}
