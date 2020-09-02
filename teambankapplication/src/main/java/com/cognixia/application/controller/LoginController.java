package com.cognixia.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.application.service.LoginService;

@RestController
@RequestMapping(path = "/login") // may need to rename this or the login functions below
public class LoginController {
	@Autowired
	LoginService lService;

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

	// may not use models for logging but httpSessions?
	@PostMapping("/login")
	public @ResponseBody String loginSuccess(ModelMap model, @RequestParam int userId, @RequestParam String userPass) {

		// THIS IS A WARNING MESSAGE IF THE LOGIN FAILED
		// model is updated in the login service
		if (!lService.loginVerify(model, userId, userPass)) {
			return "loginFail";
		}

		return "loginSuccess";
	}

}
