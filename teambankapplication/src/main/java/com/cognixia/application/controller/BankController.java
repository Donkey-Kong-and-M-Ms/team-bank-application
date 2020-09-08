package com.cognixia.application.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.cognixia.application.model.Account;
import com.cognixia.application.model.Transaction;
import com.cognixia.application.model.User;
import com.cognixia.application.repository.AccountRepository;
import com.cognixia.application.repository.TransactionRepository;
import com.cognixia.application.repository.UserRepository;
import com.cognixia.application.service.BankService;
import com.cognixia.application.dao.AccountDaoImpl;
import com.cognixia.application.dao.UserDaoImpl;

@RestController
@RequestMapping(path = "/bank")
@SessionAttributes("user")
public class BankController {

	@Autowired
	private UserRepository userRepo;
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

	// POSTING METHODS

	// the service for acc validation is dependant on the spelling of the
	// accountType.
	// it must be "Checking" or "Savings"
	@PostMapping("/account/add")
	public @ResponseBody String addNewAccount(ModelMap model, @RequestParam String accountType) {

		User loggedUser = (User) model.getAttribute("user");

		if (bank.accountValidation(accountType)) {

			accountRepo.save(new Account(0, loggedUser.getUserId(), accountType, loggedUser.getInitialDeposit()));

			return "account added";
		}

		return "account already exists with this user";
	}

	@PostMapping("/deposit")
	public @ResponseBody String depositSuccess(ModelMap model, @RequestParam float amount, @RequestParam String accountType) {

		// create user instance that we can work with
		User loggedUser = (User) model.getAttribute("user");

		// using the user object to get a user id
		int userId = loggedUser.getUserId();

		//grabs the account where userId and accountType match
		Account acc = accDaoImpl.getAccountByUserIdAndAccountType(userId, accountType);
		
		// using the user id to connect to an account id (to be used for transactions)
		int accId = acc.getAccountId();

		// sets up a local var balance to mock the balance in the account
		userBalance = acc.getBalance();

		// deposits amount
		userBalance = bank.deposit(userBalance, amount);

		// push new balance to DB
		accDaoImpl.updateBalance(userBalance, accId);

		// create a timestamp and push to transaction history for user
		// transactionRepo.save(new Transaction(0, userId, "Deposit of " + amount));
		bank.addHistory(userId, "Deposit of " + amount);

		// return strings in the form of JSX
		return "depositSuccess"; // return front end page if deposit is updated successfully
	}

	@PostMapping("/withdraw")
	public @ResponseBody String withdrawSuccess(ModelMap model, @RequestParam float amount, @RequestParam String accountType) {

		// create user instance that we can work with
		User loggedUser = (User) model.getAttribute("user");

		// using the user object to get a user id
		int userId = loggedUser.getUserId();

		//grabs the account where userId and accountType match
		Account acc = accDaoImpl.getAccountByUserIdAndAccountType(userId, accountType);

		// using the user id to connect to an account id (to be used for transactions)
		int accId = acc.getAccountId();

		// sets up a local var balance to mock the balance in the account
		userBalance = acc.getBalance();

		// deposits amount
		userBalance = bank.withdraw(userBalance, amount);

		// push new balance to DB
		accDaoImpl.updateBalance(userBalance, accId);

		// create a timestamp and push to transaction history for user
		transactionRepo.save(new Transaction(0, userId, "Withdraw of " + amount));

		// return strings in the form of JSX
		return "depositSuccess"; // return front end page if deposit is updated successfully
	}

	@PostMapping("/fundTransfer")
	public @ResponseBody String fundTransferSuccess(ModelMap model, @RequestParam int receiverId,
			@RequestParam float amount, @RequestParam String userAccountType, @RequestParam String recAccountType) {

		// create user instance that we can work with
		User loggedUser = (User) model.getAttribute("user");

		// this receiving user is not needed since methods executed below grab the
		// balance directly
		User recUser = userDaoImpl.findUserById(receiverId);

		// using the user object to get a user id
		int userId = loggedUser.getUserId();

		Account acc = accDaoImpl.getAccountByUserIdAndAccountType(userId, userAccountType);
		Account recAcc = accDaoImpl.getAccountByUserIdAndAccountType(receiverId, recAccountType);

		// using the user id to connect to an account id (to be used for transactions)
		int accId = acc.getAccountId();
		int recAccId = recAcc.getAccountId();

		// sets up a local var balance to mock the balance in the account
		userBalance = acc.getBalance();
		float recBalance = recAcc.getBalance();

		// deposits amount to receiver, withdraws from logged user
		userBalance = bank.withdraw(userBalance, amount);
		recBalance = bank.deposit(recBalance, amount);

		// push new balance to DB
		accDaoImpl.updateBalance(userBalance, accId);
		accDaoImpl.updateBalance(recBalance, recAccId);

		// create a transaction object as a psuedo-timestamp to push to the DB
		bank.addHistory(userId,
				"Transfer of " + amount + " to " + recUser.getFirstName() + " " + recUser.getLastName());
		bank.addHistory(receiverId,
				"Transfer of " + amount + " from " + loggedUser.getFirstName() + " " + loggedUser.getLastName());

		// return strings in the form of JSX
		return "fundTransferSuccess";
	}

}
