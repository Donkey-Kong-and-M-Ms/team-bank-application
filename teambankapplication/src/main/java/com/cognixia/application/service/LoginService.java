package com.cognixia.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;

import com.cognixia.application.model.User;
import com.cognixia.application.repository.UserRepository;

@Service
public class LoginService {

	@Autowired
	UserRepository userRepo;

	// code to validate userId (from User) and password - pull from controller
	public boolean loginVerify(ModelMap model, int userId, String userPass) {

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

	// password validation
	// criteria - at least: 1 number, 1 lower case, 1 upper case, 1 special
	// character, 8-30 characters long
	public boolean passWordValidation(String password) {

		if (password.matches("?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!*]).{8,30}")) {
			return true;
		} else
			return false;
	}

	// model removal/replacement for user

}
