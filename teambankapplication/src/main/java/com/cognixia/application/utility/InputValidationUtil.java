package com.cognixia.application.utility;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import com.cognixia.application.model.Account;
import com.cognixia.application.model.User;
import com.cognixia.application.repository.UserRepository;

public class InputValidationUtil {
	
	@Autowired
	UserRepository userRepo;

	public static boolean sufficientFunds(float amount, Account account) {
		if (amount < account.getBalance()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean positiveNumber(float amount) {
		if (amount > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean userExists(int userid, UserRepository userRepo) {
		if (userRepo.existsById(userid)) {
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

	public static boolean validPassword(String password) {
		
		// criteria - at least: 1 number, 1 lower case, 1 upper case, 1 special
		// character, 8-30 characters long
		if (password.matches("?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!*]).{8,30}")) {
			return true;
		} else
			return false;
	}

	//checks to see if the user exists
	public boolean validLogin(ModelMap model, int userId, String userPass) {
		List<User> uList = (List<User>) userRepo.findAll();

		for (User user : uList) {

			if (user.getUserId().equals(userId) && user.getPassword().equals(userPass)) {
				System.out.println("model prior to putting is " + model);
				model.addAttribute("user", user);
				System.out.println("model after put is " + model);
				return true;
			}

		}
		return false;
	}

}
