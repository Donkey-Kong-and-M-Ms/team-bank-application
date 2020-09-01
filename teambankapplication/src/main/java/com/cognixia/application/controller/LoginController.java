package com.cognixia.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.application.repository.AccountRepository;
import com.cognixia.application.repository.UserRepository;
import com.cognixia.application.model.*;

@RestController
@RequestMapping(path = "/login") //may need to rename this or the login functions below
public class LoginController {

	// autowired statements

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AccountRepository accountRepo;

	// global variables if any

	// GETTING METHODS

	@GetMapping("/index")
	public String showIndex() {
		return "index";
	}

	@GetMapping("/login")
	public String showLogin() {
		return "login";
	}

	// THE POST IS DONE IN BANK CONTROLLER
	@GetMapping("/user/add")
	public String showRegister() {
		return "/user/add";
	}

	// POSTING METHODS

	//may not use models for logging but httpSessions?
	@PostMapping("/login")
	public @ResponseBody String loginSuccess(ModelMap model, @RequestParam int userId, @RequestParam String userPass) {

		List<User> uList = (List<User>) userRepo.findAll();

		// the user id can/should be changed with username
		for (User user : uList) {
			if (user.getPassword().equals(userPass) && user.getUserId().equals(userId)) {
				// places the correct user in the model
				model.put("user", user);
			}
		}

		return "loginSuccess";
	}

}
