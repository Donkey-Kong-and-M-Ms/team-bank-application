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
	
	// TODO
	// Start with just 10 digit number, maybe expand to (123) 456-7890 or 123-456-7890
	public static boolean validPhoneNum(String phoneNum) {
		return false;
	}
	
	// TODO
	// Criteria is something like: At least 1 Uppercase, 1 Lowercase, 1 Special
	public static boolean validPassword(String password) {
		return false;
	}
	
	// TODO
	// Requires credential lookup - pass userRepo?
	public static boolean validLogin(String userid, String password) {
		return false;
	}

}
