package com.cognixia.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

	/*
	 * @GetMapping("/user/add") public String showRegister() { return "/user/add"; }
	 */

	// POSTING METHODS

<<<<<<< HEAD
	/*
	 * @PostMapping(path = "/user/add") public @ResponseBody String
	 * addNewUser(@RequestParam String firstName, @RequestParam String lastName,
	 * 
	 * @RequestParam String address, @RequestParam String contactNum, @RequestParam
	 * String password,
	 * 
	 * @RequestParam float initialDeposit) {
	 * 
	 * 
	 * User n = new User();
	 * 
	 * n.setFirstName(firstName); n.setLastName(lastName); n.setAddress(address);
	 * n.setContactNum(contactNum); n.setPassword(password);
	 * n.setInitialDeposit(initialDeposit);
	 * 
	 * 
	 * 
	 * if(lService.passWordValidation(password) &&
	 * lService.phoneValidation(contactNum)) { userRepo.save(new User(0, firstName,
	 * lastName, address, contactNum, password)); return "User added"; }
	 * 
	 * //userRepo.save(n); return "User already exists"; }
	 */
=======
	@PostMapping(path = "/user/add")
	public @ResponseBody String addNewUser(@RequestParam String firstName, @RequestParam String lastName,
			@RequestParam String address, @RequestParam String contactNum, @RequestParam String password,
			@RequestParam float initialDeposit) {
		 
		
		if(lService.passWordValidation(password) && lService.phoneValidation(contactNum)) {
			userRepo.save(new User(0, firstName, lastName, address, contactNum, password, initialDeposit));
			return "User added";
		}
		
		return "User already exists";
	}
>>>>>>> a0ae22962a001d0ccfcf94d792ce5bf6215cdd3e

	// redirectView used to redirect to the bank controller
	// redirectAttributes is used to redirect the model to the bank controller
	@PostMapping("/login")
	public @ResponseBody RedirectView loginSuccess(@ModelAttribute ModelMap model, @RequestParam int userId,
			@RequestParam String userPass, RedirectAttributes red) {

		// model is updated in the login service
		if (lService.loginVerify(model, userId, userPass)) {
			red.addFlashAttribute("user", model.getAttribute("user"));
			return new RedirectView("/bank/mainPage", true);
		}
		return new RedirectView("/login/login", false);
	}

}
