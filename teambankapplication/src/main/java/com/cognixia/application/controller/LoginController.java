package com.cognixia.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	
	//autowired statements
		//services, beans, repos
	
	//global variables if any
		
	//GETTING METHODS
	
	@GetMapping("/index")
	public String showIndex() {
		return "index";
	}
	
	@GetMapping("/login")
	public String showLogin() {
		return "login";
	}
	
	@GetMapping("/register")
	public String showRegister() {
		return "register";
	}
	
	//POSTING METHODS

}
