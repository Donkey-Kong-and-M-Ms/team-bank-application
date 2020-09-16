package com.cognixia.application.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.cognixia.application.model.Account;
import com.cognixia.application.model.User;
import com.cognixia.application.model.Transaction;
import com.cognixia.application.repository.AccountRepository;
import com.cognixia.application.repository.TransactionRepository;
import com.cognixia.application.utility.ErrorUtil;
import com.cognixia.application.utility.SuccessUtil;
import com.cognixia.application.service.BankService;
import com.cognixia.application.dao.AccountDaoImpl;
import com.cognixia.application.dao.UserDaoImpl;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/bank")
@SessionAttributes("user")
public class BankController {

	// autowired statements
	// services, beans, repos
	@Autowired
	private AccountRepository accountRepo;
	
	@Autowired
	private TransactionRepository transactionRepo;
	

	@Autowired
	BankService bank;
	@Autowired
	AccountDaoImpl accDaoImpl;
	@Autowired
	UserDaoImpl userDaoImpl;

	// GLOBAL VARIABLES
	float userBalance;

	// GETTING METHODS

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/mainPage")
	public String showMainPage(ModelMap model, HttpServletRequest request) {
		Map<String, ?> previousUser = RequestContextUtils.getInputFlashMap(request);

		if (previousUser != null) {
			previousUser.get("user");
		}

		// page to display
		return "mainPage";
	}

	@GetMapping("/account/add")
	public String showAccountCreation() {
		return "account/add";
	}

	@GetMapping("/deposit")
	public String showDeposit(ModelMap model) {

		// page to display
		return "deposit";
	}

	@GetMapping("/withdraw")
	public String showWithdraw(ModelMap model) {

		// page to display
		return "withdraw";
	}

	@GetMapping("/fundTransfer")
	public String showFundTransfer(ModelMap model) {

		// page to display
		return "fundTransfer";
	}

	@GetMapping("/transactionHistory")
	public String showHistory(ModelMap model) {

		// page to display
		return "transactionHistory";
	}

	@GetMapping("/myAccount")
	public String showMyAccount(ModelMap model) {

		// page to display
		return "myAccount";
	}
	
	@GetMapping(path = "/transactions")
	public @ResponseBody List<Transaction> getAllTransactionsByUserID(@RequestParam int userid) {
	List<Transaction> transactionsByUserId = new ArrayList<Transaction>();
	// Loop through all transactions
	// Add transactions with matching user id to list
	List<Transaction> list = transactionRepo.findAllByUserUserId(userid);
	for (int i = list.size(); i > 1 && i > list.size()-5; i--) {
		transactionsByUserId.add(list.get(i-1));
	}
	
	/*for (Transaction t : transactionRepo.findAllByUserUserId(userid)) {
	if (t.getUserId().equals(userid)) {
	transactionsByUserId.add(t); }
	}*/
	 return transactionsByUserId;
	}

	// POST METHODS

	// Add new user
	// when creating a user, an account will be created by default
	// An automatic transaction will be entered to reflect the initial amount
	// deposited
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path = "/register")
	public @ResponseBody String registerUser(@RequestParam String firstName, @RequestParam String lastName,
			@RequestParam String address, @RequestParam String contactNum, @RequestParam String password,
			@RequestParam float initialDeposit, @RequestParam String accountType) {

		if (bank.register(firstName, lastName, address, contactNum, password, initialDeposit, accountType)) {
			return SuccessUtil.successRegister();
		} else {
			return ErrorUtil.errorRegisterFailed();
		}
	}

	// User will get to add another account
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/account/addNew")
	public @ResponseBody String addAnotherAccount(ModelMap model, @RequestParam String accountType,
			@RequestParam float initialDeposit, @RequestParam int userId) {

		//User loggedUser = (User) model.getAttribute("user");

		if (bank.accountValidation(accountType)) {

			bank.addNewAccount(userId, accountType, initialDeposit);

			return "account added";
		}

		return "account already exists with this user";
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/deposit")
	public @ResponseBody String depositSuccess( ModelMap model, @RequestParam float amount,
			@RequestParam String accountType, @RequestParam int userId) {
		System.out.println(model);
		System.out.println("test");
		// create user instance that we can work with
		User loggedUser = (User) model.getAttribute("user");

		// using the user object to get a user id
		//int userid = loggedUser.getUserId();

		if (bank.deposit(userId, amount, accountType)) {

			return SuccessUtil.successDeposit();
		} else {
			return ErrorUtil.errorDepositFailed();
		}
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/withdraw")
	public @ResponseBody String withdrawSuccess(ModelMap model, @RequestParam float amount,
			@RequestParam String accountType, @RequestParam int userId) {

		// create user instance that we can work with
		User loggedUser = (User) model.getAttribute("user");

		// using the user object to get a user id
		//int userid = loggedUser.getUserId();

		if (bank.withdraw(userId, amount, accountType)) {

			return SuccessUtil.successWithdraw();
		} else {
			return ErrorUtil.errorWithdrawFailed();
		}
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/fundTransfer")
	public @ResponseBody String fundTransferSuccess(ModelMap model, @RequestParam int receiverId,
			@RequestParam float amount, @RequestParam String userAccountType, @RequestParam String recAccountType, @RequestParam int userId) {

		// create user instance that we can work with
		User loggedUser = (User) model.getAttribute("user");

		// using the user object to get a user id
		//int userId = loggedUser.getUserId();

		// Attempt to transfer
		if (bank.transfer(userId, receiverId, amount, userAccountType, recAccountType)) {
			return SuccessUtil.successTransfer();
		} else {
			return ErrorUtil.errorTransferFailed();
		}
	}

}