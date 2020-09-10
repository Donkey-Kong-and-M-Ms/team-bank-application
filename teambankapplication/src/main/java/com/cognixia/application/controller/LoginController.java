package com.cognixia.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import com.cognixia.application.model.User;
import com.cognixia.application.repository.UserRepository;
import com.cognixia.application.service.LoginService;

@RestController
@RequestMapping(path = "/login") // may need to rename this or the login functions below
public class LoginController {
	@Autowired
	LoginService lService;

	@Autowired
	UserRepository userRepo;

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

	@GetMapping("/user/add")
	public String showRegister() {
		return "/user/add";
	}

	// POSTING METHODS
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path = "/user/add")
	public @ResponseBody String addNewUser(/*@RequestParam String firstName, @RequestParam String lastName,
			@RequestParam String address, @RequestParam String contactNum, @RequestParam String password,
			@RequestParam float initialDeposit*/ @RequestBody User user) {
		System.out.println(user);
		/*
		 * User n = new User();
		 * 
		 * n.setFirstName(firstName); n.setLastName(lastName); n.setAddress(address);
		 * n.setContactNum(contactNum); n.setPassword(password);
		 * n.setInitialDeposit(initialDeposit);
		 */
		
		if(/*lService.passWordValidation(user.getPassword())&&*/ lService.phoneValidation(user.getContactNum())) {
			System.out.println("Run");
			userRepo.save(new User(0, user.getFirstName(), user.getLastName(), user.getAddress(), user.getContactNum(), user.getPassword(), 0));
			//Inform user of ID num
			return "User added";
		}
		
		//userRepo.save(n);
		return "User already exists";
	}

	// redirectView used to redirect to the bank controller
	//redirectAttributes is used to redirect the model to the bank controller
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/login")
	public @ResponseBody /*RedirectView*/ String loginSuccess(@ModelAttribute ModelMap model,/* @RequestParam int userId,
			@RequestParam String userPass,*/ @RequestBody User user, RedirectAttributes red) {

		// model is updated in the login service
		if (lService.loginVerify(model, user.getUserId(), user.getPassword())) {
			System.out.println("model inside controller is " + model);
			red.addFlashAttribute("user", model.getAttribute("user"));
			System.out.println("red is " + red);
			//return new RedirectView("/bank/mainPage", true);
			return "Login Success";
		}
		//return new RedirectView("/login/login", false);
		return "Login Failed";
	}

}
