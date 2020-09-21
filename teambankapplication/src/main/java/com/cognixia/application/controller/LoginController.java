package com.cognixia.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import com.cognixia.application.repository.UserRepository;
import com.cognixia.application.service.LoginService;
import com.cognixia.application.utility.ErrorUtil;
import com.cognixia.application.utility.SuccessUtil;

@RestController
@RequestMapping(path = "/login") // may need to rename this or the login functions below
public class LoginController {
	@Autowired
	LoginService lService;

	@Autowired
	UserRepository userRepo;

	// GETTING METHODS

	@GetMapping("/index")
	public String showIndex() {
		return "index";
	}

	@CrossOrigin(origins = "https://dk-bank.herokuapp.com/")
	@GetMapping("/login")
	public String showLogin() {
		return "Login Failed";
	}
	
	//POST METHODS

	// redirectView used to redirect to the bank controller
	// redirectAttributes is used to redirect the model to the bank controller
	@CrossOrigin(origins = "https://dk-bank.herokuapp.com/")
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
	
	@PostMapping("/logout")
	public @ResponseBody String logoutSuccess(@ModelAttribute ModelMap model) {
		
		//this effectively logs the user out
		if(lService.isUserLoggedOut(model)) {
			model.clear();
			return SuccessUtil.successLogout();
		}
		else {
			return ErrorUtil.errorLogoutFailed();
		}
		
		
	}

}
