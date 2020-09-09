package com.cognixia.application.utility;

import java.util.List;

import com.cognixia.application.model.Account;
import com.cognixia.application.model.User;
import com.cognixia.application.repository.UserRepository;

public class InputValidationUtil {
	
	public static boolean sufficientFunds(float amount, Account account) {
		if(amount < account.getBalance()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean positiveNumber(float amount) {
		if(amount > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	// TODO
	// Requires user lookup - pass UserRepo
	public static boolean userExists(int userid, UserRepository userRepo) {
		if(userRepo.existsById(userid)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean validPhoneNum(String phoneNum) {
		// checks to see if there is a format of (XXX)XXX-XXXX
		if (phoneNum.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) {
			return true;
		} else {
			return false;
		}
	}
	
	// password validation
	// criteria - at least: 1 number, 1 lower case, 1 upper case, 1 special
	// character, 8-30 characters long
	public static boolean validPassword(String password) {
		
		if (password.matches("?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!*]).{8,30}")) {
			return true;
		} else
			return false;
	}
	
	// TODO
	// Requires credential lookup - pass userRepo?
	public static boolean validLogin(int userid, String password, UserRepository userRepo) {
		List<User> uList = (List<User>) userRepo.findAll();

		for (User user : uList) {
			if (user.getUserId().equals(userid) && user.getPassword().equals(password)) {
				return true;
			}

		}
		return false;
	}

	
	public static boolean validAccountType(String accountType) {
		if (accountType.equalsIgnoreCase("Checking") | accountType.equalsIgnoreCase("Savings")) {
			return true;
		} else {
			return false;
		}
	}
}
