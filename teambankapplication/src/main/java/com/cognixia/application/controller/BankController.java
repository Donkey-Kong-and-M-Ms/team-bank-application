package com.cognixia.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
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
import com.cognixia.application.service.BankService;
import com.cognixia.application.dao.AccountDaoImpl;
import com.cognixia.application.dao.UserDaoImpl;

@RestController
@RequestMapping(path = "/bank")
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
	Account account;
	@Autowired
	UserDaoImpl userDaoImpl;

	// GLOBAL VARIABLES
	float userBalance;

	// GETTING METHODS

	@GetMapping("/mainPage")
	public String showMainPage() {

		// page to display
		return "mainPage";
	}

	@GetMapping("/deposit")
	public String showDeposit() {

		// page to display
		return "deposit";
	}

	@GetMapping("/withdraw")
	public String showWithdraw() {

		// page to display
		return "withdraw";
	}

	@GetMapping("/fundTransfer")
	public String showFundTransfer() {

		// page to display
		return "fundTransfer";
	}

	@GetMapping("/transactionHistory")
	public String showHistory() {

		// page to display
		return "transactionHistory";
	}

	@GetMapping("/myAccount")
	public String showMyAccount() {

		// page to display
		return "myAccount";
	}

	@PostMapping(path = "/user/add")
	public @ResponseBody String addNewUser(@RequestParam String firstName, @RequestParam String lastName,
			@RequestParam String address, @RequestParam String contactNum, @RequestParam String password,
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

	// POSTING METHODS

	@PostMapping("/deposit")
	public String depositSuccess(ModelMap model, float amount) {

		// create user instance that we can work with
		User loggedUser = (User) model.getAttribute("user");

		// using the user object to get a user id
		int userId = loggedUser.getUserId();

		// using the user id to connect to an account id (to be used for transactions)
		int accId = accDaoImpl.getAccountIdByUserId(userId);

		// sets up a local var balance to mock the balance in the account
		userBalance = accDaoImpl.getBalanceByAccountId(accId);

		// deposits amount
		userBalance = bank.deposit(userBalance, amount);

		// push new balance to DB
		accDaoImpl.updateBalance(userBalance, accId);

		// create a timestamp and push to transaction history for user
		transactionRepo.save(new Transaction(0, userId, "Deposit of " + amount));

		// return strings in the form of JSX
		return "depositSuccess"; // return front end page if deposit is updated successfully
	}

	@PostMapping("/withdraw")
	public String withdrawSuccess(ModelMap model, float amount) {

		// create user instance that we can work with
		User loggedUser = (User) model.getAttribute("user");

		// using the user object to get a user id
		int userId = loggedUser.getUserId();

		// using the user id to connect to an account id (to be used for transactions)
		int accId = accDaoImpl.getAccountIdByUserId(userId);

		// sets up a local var balance to mock the balance in the account
		userBalance = accDaoImpl.getBalanceByAccountId(accId);

		// deposits amount
		userBalance = bank.withdraw(userBalance, amount);

		// push new balance to DB
		accDaoImpl.updateBalance(userBalance, accId);

		// create a timestamp and push to transaction history for user
		transactionRepo.save(new Transaction(0, userId, "Withdraw of " + amount));

		// return strings in the form of JSX
		return "depositSuccess"; // return front end page if deposit is updated successfully
	}

	@PostMapping()
	public String fundTransferSuccess(ModelMap model, int receiverId, float amount) {

		// create user instance that we can work with
		User loggedUser = (User) model.getAttribute("user");
		
		//this receiving user is not needed since methods executed below grab the balance directly
		//User recUser = userDaoImpl.findUserById(receiverId);

		// using the user object to get a user id
		int userId = loggedUser.getUserId();

		// using the user id to connect to an account id (to be used for transactions)
		int accId = accDaoImpl.getAccountIdByUserId(userId);
		int recAccId = accDaoImpl.getAccountIdByUserId(receiverId);

		// sets up a local var balance to mock the balance in the account
		userBalance = accDaoImpl.getBalanceByAccountId(accId);
		float recBalance = accDaoImpl.getBalanceByAccountId(recAccId);

		// deposits amount
		recBalance = bank.deposit(recBalance, amount);
		userBalance = bank.withdraw(userBalance, amount);

		// push new balance to DB
		accDaoImpl.updateBalance(userBalance, accId);
		accDaoImpl.updateBalance(recBalance, recAccId);

		// create a transaction object as a psuedo-timestamp to push to the DB
		bank.addHistory(userId, "Transferred to " + receiverId);
		bank.addHistory(receiverId, "Transferred from " + userId);

		// return strings in the form of JSX
		return "fundTransferSuccess";
	}
}
