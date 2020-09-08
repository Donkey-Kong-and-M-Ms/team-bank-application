package com.cognixia.application.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import com.cognixia.application.utility.ErrorUtil;
import com.cognixia.application.utility.InputValidationUtil;
import com.cognixia.application.utility.MessageUtil;
import com.cognixia.application.utility.SuccessUtil;
import com.cognixia.application.utility.TransactionUtil;
import com.cognixia.application.service.BankService;
import com.cognixia.application.dao.AccountDaoImpl;
import com.cognixia.application.dao.UserDaoImpl;

@RestController
@RequestMapping(path = "/bank")
@SessionAttributes("user")
public class BankController {
	
	//autowired statements
		//services, beans, repos
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
		
		return SuccessUtil.successRegister();
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
			addNewTransaction(accountToUpdate.getUserId(), TransactionUtil.deposit(deposit, accountToUpdate.getAccountType()));
			return SuccessUtil.successDeposit() + " " + MessageUtil.newBalance(deposit);
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
				addNewTransaction(accountToUpdate.getUserId(), TransactionUtil.withdraw(withdraw, accountToUpdate.getAccountType()));
				return SuccessUtil.successWithdraw() + " " + MessageUtil.newBalance(withdraw);
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
			accountRepo.save(otherAccount);
			addNewTransaction(accountToUpdate.getUserId(), TransactionUtil.giveTransfer(transfer, accountToUpdate.getAccountType()));
			addNewTransaction(otherAccount.getUserId(), TransactionUtil.receiveTransfer(transfer, otherAccount.getAccountType()));
			return SuccessUtil.successTransfer() + " " + MessageUtil.newBalance(transfer);
		} else {
			return errorMessage;
		}
	}
	
	// Get all accounts
	@GetMapping(path = "/account/all")
	public @ResponseBody Iterable<Account> getAllAccounts() {
		return accountRepo.findAll();
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
	
	// Get transaction by transaction ID
	@GetMapping(path = "/transaction/{transactionid}")
	public @ResponseBody Optional<Transaction> getTransactionByID(@PathVariable Integer transactionid) {
		return transactionRepo.findById(transactionid);
	}
	
	// Get all accounts of certain user ID
	@GetMapping(path = "/{userid}/account")
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
	
	// Get all accounts of certain user ID, made readable
	@GetMapping(path = "/{userid}/account/pretty")
	public @ResponseBody List<String> getAccounTypeNadBalanceByUserID(@PathVariable Integer userid) {
		List<String> accountTypeAndBalance = new ArrayList<String>();
		// Loop through all accounts
		// Add accounts with matching user id to list
		for(Account a: accountRepo.findAll()) {
			if(a.getUserId().equals(userid)) {
				accountTypeAndBalance.add("Account " + a.getAccountId() + ": " + a.getAccountType() + ", $" + a.getBalance());
			}
		}
		
		return accountTypeAndBalance;
	}
	
	// Get all transactions of a certain user ID
	@GetMapping(path = "/{userid}/transaction")
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
	
	// Get all transactions of a certain user ID, just the descriptions
	@GetMapping(path = "/{userid}/transaction/description")
	public @ResponseBody List<String> getAllTransactionDescriptionsByUserID(@PathVariable Integer userid) {
		List<String> transactionDescriptions = new ArrayList<String>();
		// Loop through all transactions
		// Add transactions with matching user id to list
		for(Transaction t: transactionRepo.findAll()) {
			if(t.getUserId().equals(userid)) {
				transactionDescriptions.add(t.getDescription());
			}
		}

		return transactionDescriptions;
	}
	
	// Deposit to account
		@PostMapping(path = "/{userid}/deposit")
		public @ResponseBody String depositToUser(@PathVariable Integer userid,
				@RequestParam Integer accountid, @RequestParam float deposit) {
			Account accountToUpdate = accountRepo.getOne(accountid);
			if(InputValidationUtil.positiveNumber(deposit)) {
				accountToUpdate.deposit(deposit);
				accountRepo.save(accountToUpdate);
				addNewTransaction(userid, TransactionUtil.deposit(deposit, accountToUpdate.getAccountType()));
				return SuccessUtil.successDeposit() + " " + MessageUtil.newBalance(deposit);
			} else {
				return ErrorUtil.errorNotPositive();
			}
		}
		
		// Withdraw from account
		@PostMapping(path = "/{userid}/withdraw")
		public @ResponseBody String withdrawFromUser(@PathVariable Integer userid,
				@RequestParam Integer accountid, @RequestParam float withdraw) {
			Account accountToUpdate = accountRepo.getOne(accountid);
			if(InputValidationUtil.positiveNumber(withdraw)) {
				if(InputValidationUtil.sufficientFunds(withdraw, accountToUpdate)) {
					accountToUpdate.withdraw(withdraw);
					accountRepo.save(accountToUpdate);
					addNewTransaction(userid, TransactionUtil.withdraw(withdraw, accountToUpdate.getAccountType()));
					return SuccessUtil.successWithdraw() + " " + MessageUtil.newBalance(withdraw);
				} else {
					return ErrorUtil.errorNotEnough();
				}
			} else {
				return ErrorUtil.errorNotPositive();
			}
		}
		
		// Transfer to other account
		@PostMapping(path = "/{userid}/transfer")
		public @ResponseBody String transferBetweenUserAccounts(@PathVariable Integer userid,
				@RequestParam Integer accountid, @RequestParam Integer transferAccountid, @RequestParam float transfer) {
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
				accountRepo.save(otherAccount);
				addNewTransaction(userid, TransactionUtil.giveTransfer(transfer, accountToUpdate.getAccountType()));
				addNewTransaction(otherAccount.getUserId(), TransactionUtil.receiveTransfer(transfer, otherAccount.getAccountType()));
				return SuccessUtil.successTransfer() + " " + MessageUtil.newBalance(transfer);
			} else {
				return errorMessage;
			}
		}
	
}