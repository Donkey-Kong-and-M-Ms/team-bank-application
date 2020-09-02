package com.cognixia.application.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.cognixia.application.utility.ErrorUtil;
import com.cognixia.application.utility.InputValidationUtil;
import com.cognixia.application.utility.TransactionUtil;

@RestController
@RequestMapping(path = "/bank")
public class BankController {
	
	//autowired statements
		//services, beans, repos
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private AccountRepository accountRepo;
	@Autowired
	private TransactionRepository transactionRepo;
	
	//global variables if any
	
	//GETTING METHODS
	
	@GetMapping("/mainPage")
	public String showMainPage() {
		
		//page to display
		return "mainPage";
	}
	
	@GetMapping("/deposit")
	public String showDeposit() {
		
		//page to display
		return "deposit";
	}
	
	@GetMapping("/withdraw")
	public String showWithdraw() {
		
		//page to display
		return "withdraw";
	}
	
	@GetMapping("/fundTransfer")
	public String showFundTransfer() {
		
		//page to display
		return "fundTransfer";
	}
	
	@GetMapping("/transactionHistory")
	public String showHistory() {
		
		//page to display
		return "transactionHistory";
	}
	
	@GetMapping("/myAccount")
	public String showMyAccount() {
		
		//page to display
		return "myAccount";
	}
	
	//POSTING METHODS
	
	@PostMapping("/deposit")
	public String depositSuccess( /* ModelMap model, double amount */ ) {
		
		//create user instance
			//User loggedUser = (User) model.getAttribute("user");
		
		//call deposit method
			//OPTIONAL consider getting/setting UserBalance
		
		//push new balance to DB
		
		//create a timestamp and push to transaction history for user
		
		//OPTIONAL display success or redirect to main menu
		//return strings in the form of JSX
		return "depositSuccess"; //return front end page if deposit is updated successfully
	}
	
	@PostMapping("/withdraw")
	public String withdrawSuccess(/* ModelMap model, double amount */) {
		
		//create user instance
			//User loggedUser = (User) model.getAttribute("user");
		
		//call withdraw method
			//OPTIONAL consider getting/setting UserBalance
		
		//push new balance to DB as an UPDATE to user
		
		//create a timestamp and push to transaction history for user
		
		//OPTIONAL display success or redirect to main menu
		//return strings in the form of JSX
		return "depositSuccess"; //return front end page if deposit is updated successfully
	}
	
	@PostMapping
	public String fundTransferSuccess( /* ModelMap model, int receiverId, double amount */ ) {
		
		//2 WAYS TO DO THIS
		//1. do a separate deposit and withdraw method for the respective user
		
			//create 2 objects - 1 loggedUser, 1 receivingUser
				//User loggedUser = (User) model.getAttribute("user");
				//User receivingUser = daoimpl.findByUserId(receiverId)
		
			//OPTIONAL get loggeduser balance
				//withdraw entered amount from main user
				//set balance to the loggeduser
			
			//OPTIONAL get recuser balance
				//deposit where userID=?
				//set balance to the recuser
		
			//save new balances to the DB
			
			//save to transaction histories
			
			//return page to end
		
		//--------------------------------------------------
		
		//2. use a fundtransfer method
		
		//create 2 objects - 1 loggedUser, 1 receivingUser
		//User loggedUser = (User) model.getAttribute("user");
		//User receivingUser = daoimpl.findByUserId(receiverId)
			
			//OPTIONAL getboth balance
		
			//use a fundtransfer method (which should have depo and draw method embedded)
	
			//save new balances to the DB
			
			//save to transaction histories
		
			//return page to end
		
		//return strings in the form of JSX
		return "fundTransferSuccess";
	}
	
	// Add new user
	@PostMapping(path="/register")
	public @ResponseBody String registerUser (@RequestParam String firstName,
			@RequestParam String lastName, @RequestParam String address,
			@RequestParam String contactNum, @RequestParam String password,
			@RequestParam float initialDeposit, @RequestParam String accountType) {
		addNewUser(firstName, lastName, address, contactNum, password, initialDeposit);
		
		Integer userId = userRepo.getOne((int) userRepo.count()).getUserId();
		
		addNewAccount(userId, accountType, initialDeposit);
		addNewTransaction(userId, TransactionUtil.register(initialDeposit, firstName + " " + lastName));
		
		return "Registered User";
	}
	
	// Add new user
	@PostMapping(path="/user/add")
	public @ResponseBody String addNewUser (@RequestParam String firstName,
			@RequestParam String lastName, @RequestParam String address,
			@RequestParam String contactNum, @RequestParam String password,
			@RequestParam float initialDeposit) {
		
		User newUser = new User();
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setAddress(address);
		newUser.setContactNum(contactNum);
		newUser.setPassword(password);
		newUser.setInitialDeposit(initialDeposit);
		userRepo.save(newUser);
		return "Saved User";
	}
	
	// Get all users
	@GetMapping(path = "/user/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepo.findAll();
	}
	
	// Get user by user ID
	@GetMapping(path = "/user/{userid}")
	public @ResponseBody Optional<User> getUserByID(@PathVariable Integer userid) {
		return userRepo.findById(userid);
	}
	
	// Add new account
	@PostMapping(path="/account/add")
	public @ResponseBody String addNewAccount (@RequestParam int userId, 
			@RequestParam String accountType, @RequestParam float balance) {
		
		Account newAccount = new Account();
		newAccount.setUser(userId);
		newAccount.setAccountType(accountType);
		newAccount.setBalance(balance);
		accountRepo.save(newAccount);
		return "Saved Account";
	}
	
	// Update account balance
	@PostMapping(path = "/account/{accountid}/update/balance")
	public @ResponseBody String updateAccountBalance(@PathVariable Integer accountid, @RequestParam float balance) {
		Account accountToUpdate = accountRepo.getOne(accountid);
		accountToUpdate.setBalance(balance);
		accountRepo.save(accountToUpdate);
		return "Balance updated";
	}
	
	// Deposit to account
	@PostMapping(path = "/account/{accountid}/deposit")
	public @ResponseBody String depositToAccount(@PathVariable Integer accountid, @RequestParam float deposit) {
		Account accountToUpdate = accountRepo.getOne(accountid);
		if(InputValidationUtil.positiveNumber(deposit)) {
			accountToUpdate.deposit(deposit);
			accountRepo.save(accountToUpdate);
			return "Deposit successful, new balance: " + accountToUpdate.getBalance();
		} else {
			return ErrorUtil.errorNotPositive();
		}
	}
	
	// Withdraw from account
	@PostMapping(path = "/account/{accountid}/withdraw")
	public @ResponseBody String withdrawFromAccount(@PathVariable Integer accountid, @RequestParam float withdraw) {
		Account accountToUpdate = accountRepo.getOne(accountid);
		if(InputValidationUtil.positiveNumber(withdraw)) {
			if(InputValidationUtil.sufficientFunds(withdraw, accountToUpdate)) {
				accountToUpdate.withdraw(withdraw);
				accountRepo.save(accountToUpdate);
				return "Withdraw successful, new balance: " + accountToUpdate.getBalance();
			} else {
				return ErrorUtil.errorNotEnough();
			}
		} else {
			return ErrorUtil.errorNotPositive();
		}
	}
	
	// Transfer to other account
	@PostMapping(path = "/account/{accountid}/transfer")
	public @ResponseBody String transferBetweenAccounts(@PathVariable Integer accountid, 
			@RequestParam Integer transferAccountid, @RequestParam float transfer) {
		Account accountToUpdate = accountRepo.getOne(accountid);
		Account otherAccount = accountRepo.getOne(transferAccountid);
		
		boolean noErrorNumber = false;
		boolean noErrorUser = false;
		
		String errorMessage = "";
		
		if(InputValidationUtil.userExists(transferAccountid, userRepo)) {
			noErrorUser = true;
		} else {
			errorMessage += ErrorUtil.errorUserNotFound() + ", ";
		}
		
		if(InputValidationUtil.positiveNumber(transfer)) {
			if(InputValidationUtil.sufficientFunds(transfer, accountToUpdate)) {
				noErrorNumber = true;
			} else {
				errorMessage += ErrorUtil.errorNotEnough();
			}
		} else {
			errorMessage += ErrorUtil.errorNotPositive();
		}
		
		if(noErrorUser && noErrorNumber) {
			accountToUpdate.withdraw(transfer);
			otherAccount.deposit(transfer);
			accountRepo.save(accountToUpdate);
			return "Transfer successful, new balance: " + accountToUpdate.getBalance();
		} else {
			return errorMessage;
		}
	}
	
	// Get all accounts
	@GetMapping(path = "/account/all")
	public @ResponseBody Iterable<Account> getAllAccounts() {
		return accountRepo.findAll();
	}
	
	// Get all accounts of certain user ID
	@GetMapping(path = "/account/all/{userid}")
	public @ResponseBody List<Account> getAllAccountsByUserID(@PathVariable Integer userid) {
		List<Account> accountsByUserId = new ArrayList<Account>();
		// Loop through all accounts
		// Add accounts with matching user id to list
		for(Account a: accountRepo.findAll()) {
			if(a.getUserId().equals(userid)) {
				accountsByUserId.add(a);
			}
		}
		
		return accountsByUserId;
	}
	
	// Get account by account ID
	@GetMapping(path = "/account/{accountid}")
	public @ResponseBody Optional<Account> getAccountByID(@PathVariable Integer accountid) {
		return accountRepo.findById(accountid);
	}
	
	// Add new transaction
	@PostMapping(path="/transaction/add")
	public @ResponseBody String addNewTransaction (@RequestParam int userId, 
			@RequestParam String description) {
		
		Transaction newTransaction = new Transaction();
		newTransaction.setUser(userId);
		newTransaction.setDescription(description);
		transactionRepo.save(newTransaction);
		return "Saved Transaction";
	}
	
	// Get all transactions
	@GetMapping(path = "/transaction/all")
	public @ResponseBody Iterable<Transaction> getAllTransactions() {
		return transactionRepo.findAll();
	}
	
	// Get all transactions of a certain user ID
	@GetMapping(path = "/transaction/all/{userid}")
	public @ResponseBody List<Transaction> getAllTransactionsByUserID(@PathVariable Integer userid) {
		List<Transaction> transactionsByUserId = new ArrayList<Transaction>();
		// Loop through all transactions
		// Add transactions with matching user id to list
		for(Transaction t: transactionRepo.findAll()) {
			if(t.getUserId().equals(userid)) {
				transactionsByUserId.add(t);
			}
		}
		
		return transactionsByUserId;
	}
	
	// Get transaction by transaction ID
	@GetMapping(path = "/transaction/{transactionid}")
	public @ResponseBody Optional<Transaction> getTransactionByID(@PathVariable Integer transactionid) {
		return transactionRepo.findById(transactionid);
	}
	
}