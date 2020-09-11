package com.cognixia.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.Optional;

import com.cognixia.application.model.User;
import com.cognixia.application.repository.UserRepository;
import com.cognixia.application.utility.InputValidationUtil;

@Service
public class LoginService {

	@Autowired
	UserRepository userRepo;

	// code to validate userId (from User) and password - pull from controller
	public boolean loginVerify(ModelMap model, int userId, String userPass) {
		if (InputValidationUtil.validLogin(userId, userPass, userRepo)) {
			Optional<User> user = userRepo.findById(userId);
			System.out.println("model prior to putting is " + model);
			model.addAttribute("user", user.get());
			System.out.println("model after put is " + model);
			return true;
		} else {
			return false;
		}
	}

	// password validation
	// criteria - at least: 1 number, 1 lower case, 1 upper case, 1 special
	// character, 8-30 characters long
	public boolean passWordValidation(String password) {
		return InputValidationUtil.validPassword(password);
	}

	// validation for phone number
	public boolean phoneValidation(String contactNum) {
		return InputValidationUtil.validPhoneNum(contactNum);
	}

	//checks if user is logged out or not
	public boolean isUserLoggedOut(ModelMap model) {

		if (model.isEmpty()) {
			return true;
		}
		return false;
	}

}
