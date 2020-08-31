package com.cognixia.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.application.model.Account;
import com.cognixia.application.model.Transaction;
import com.cognixia.application.model.User;
import com.cognixia.application.repository.AccountRepository;
import com.cognixia.application.repository.TransactionRepository;
import com.cognixia.application.repository.UserRepository;

@RestController
@RequestMapping(path = "/bank")
public class BankController {
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private AccountRepository accountRepo;
	@Autowired
	private TransactionRepository transactionRepo;
	
	@PostMapping(path="/user/add")
	public @ResponseBody String addNewUser (@RequestParam String firstName,
			@RequestParam String lastName, @RequestParam String address,
			@RequestParam String contactNum, @RequestParam String password,
			@RequestParam float initialDeposit) {
		
		User n = new User();
		n.setFirstName(firstName);
		n.setLastName(lastName);
		n.setAddress(address);
		n.setContactNum(contactNum);
		n.setPassword(password);
		n.setInitialDeposit(initialDeposit);
		userRepo.save(n);
		return "Saved";
	}
	
	@GetMapping(path = "/user/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepo.findAll();
	}
	
	@GetMapping(path = "/account/all")
	public @ResponseBody Iterable<Account> getAllAccounts() {
		return accountRepo.findAll();
	}
	
	@GetMapping(path = "/transaction/all")
	public @ResponseBody Iterable<Transaction> getAllTransactions() {
		return transactionRepo.findAll();
	}

}
