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
	
	@GetMapping(path = "/user/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepo.findAll();
	}
	
	@GetMapping(path = "/user/{userid}")
	public @ResponseBody Optional<User> getUserByID(@PathVariable Integer userid) {
		return userRepo.findById(userid);
	}
	
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
	
	@GetMapping(path = "/account/all")
	public @ResponseBody Iterable<Account> getAllAccounts() {
		return accountRepo.findAll();
	}
	
	@GetMapping(path = "/account/all/{userid}")
	public @ResponseBody List<Account> getAllAccountsByUserID(@PathVariable Integer userid) {
		Iterable<Account> allAccounts = accountRepo.findAll();
		List<Account> accountsByUserId = new ArrayList<Account>();
		for(Account a: allAccounts) {
			if(a.getUserId().equals(userid)) {
				accountsByUserId.add(a);
			}
		}
		
		return accountsByUserId;
	}
	
	@GetMapping(path = "/account/{accountid}")
	public @ResponseBody Optional<Account> getAccountByID(@PathVariable Integer accountid) {
		return accountRepo.findById(accountid);
	}
	
	@PostMapping(path="/transaction/add")
	public @ResponseBody String addNewTransaction (@RequestParam int userId, 
			@RequestParam String description) {
		
		Transaction newTransaction = new Transaction();
		newTransaction.setUser(userId);
		newTransaction.setDescription(description);
		transactionRepo.save(newTransaction);
		return "Saved Transaction";
	}
	
	@GetMapping(path = "/transaction/all")
	public @ResponseBody Iterable<Transaction> getAllTransactions() {
		return transactionRepo.findAll();
	}
	
	@GetMapping(path = "/transaction/all/{userid}")
	public @ResponseBody List<Transaction> getAllTransactionsByUserID(@PathVariable Integer userid) {
		Iterable<Transaction> allTransactions = transactionRepo.findAll();
		List<Transaction> transactionsByUserId = new ArrayList<Transaction>();
		for(Transaction t: allTransactions) {
			if(t.getUserId().equals(userid)) {
				transactionsByUserId.add(t);
			}
		}
		
		return transactionsByUserId;
	}
	
	@GetMapping(path = "/transaction/{transactionid}")
	public @ResponseBody Optional<Transaction> getTransactionByID(@PathVariable Integer transactionid) {
		return transactionRepo.findById(transactionid);
	}
	
}