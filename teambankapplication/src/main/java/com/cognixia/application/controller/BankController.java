package com.cognixia.application.controller;

/*//<<<<<<< HEAD
	*/
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

@RestController
@RequestMapping(path = "/bank")
public class BankController {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private AccountRepository accountRepo;
	@Autowired
	private TransactionRepository transactionRepo;

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
//>>>>>>> a7e6be9217a15df94f81a75aa73ac9a7040f7561
	}

	// POSTING METHODS

	@PostMapping("/deposit")
	public String depositSuccess(ModelMap model, double amount) {

		// create user instance that we can work with
		// User loggedUser = (User) model.getAttribute("user");

		// call deposit method
		// OPTIONAL consider getting/setting UserBalance

		// push new balance to DB

		// create a timestamp and push to transaction history for user

		// OPTIONAL display success or redirect to main menu
		// return strings in the form of JSX
		return "depositSuccess"; // return front end page if deposit is updated successfully
	}

	@PostMapping("/withdraw")
	public String withdrawSuccess(ModelMap model, double amount) {

		// create user instance
		// User loggedUser = (User) model.getAttribute("user");

		// call withdraw method
		// OPTIONAL consider getting/setting UserBalance

		// push new balance to DB as an UPDATE to user

		// create a timestamp and push to transaction history for user

		// OPTIONAL display success or redirect to main menu
		// return strings in the form of JSX
		return "depositSuccess"; // return front end page if deposit is updated successfully
	}

	@PostMapping()
	public String fundTransferSuccess(ModelMap model, int receiverId, double amount) {

		// 2 WAYS TO DO THIS
		// 1. do a separate deposit and withdraw method for the respective user

		// create 2 objects - 1 loggedUser, 1 receivingUser
		// User loggedUser = (User) model.getAttribute("user");
		// User receivingUser = daoimpl.findByUserId(receiverId)

		// OPTIONAL get loggeduser balance
		// withdraw entered amount from main user
		// set balance to the loggeduser

		// OPTIONAL get recuser balance
		// deposit where userID=?
		// set balance to the recuser

		// save new balances to the DB

		// save to transaction histories

		// return page to end

		// --------------------------------------------------

		// 2. use a fundtransfer method

		// create 2 objects - 1 loggedUser, 1 receivingUser
		// User loggedUser = (User) model.getAttribute("user");
		// User receivingUser = daoimpl.findByUserId(receiverId)

		// OPTIONAL getboth balance

		// use a fundtransfer method (which should have depo and draw method embedded)

		// save new balances to the DB

		// save to transaction histories

		// return page to end

		// return strings in the form of JSX
		return "fundTransferSuccess";
	}
}
