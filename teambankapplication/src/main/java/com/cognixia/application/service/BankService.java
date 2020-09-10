package com.cognixia.application.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognixia.application.dao.AccountDaoImpl;
import com.cognixia.application.model.Account;
import com.cognixia.application.model.Transaction;
import com.cognixia.application.model.User;
import com.cognixia.application.repository.AccountRepository;
import com.cognixia.application.repository.TransactionRepository;
import com.cognixia.application.repository.UserRepository;
import com.cognixia.application.utility.InputValidationUtil;
import com.cognixia.application.utility.SuccessUtil;
import com.cognixia.application.utility.TransactionUtil;

@Service
public class BankService {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	TransactionRepository transactionRepo;

	@Autowired
	AccountDaoImpl accDaoImpl;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd - HH.mm.ss");

	// deposit method used in Bank controller
	public float deposit(float balance, float amount) {
		balance = balance + amount;
		return balance;
	}

	// withdraw method used in Bank controller
	public float withdraw(float balance, float amount) {
		balance = balance - amount;
		return balance;
	}
	
	public boolean deposit(int userid, float amount, String accountType) {

		if(InputValidationUtil.validAccountType(accountType)) {
			// grabs the account where userId and accountType match
//			Account acc = accDaoImpl.getAccountByUserIdAndAccountType(userid, accountType);
//			System.out.println(acc.getUserId());

			//		// using the user id to connect to an account id (to be used for transactions)
			//		int accId = acc.getAccountId();
			//
			//		// sets up a local var balance to mock the balance in the account
			//		float userBalance = acc.getBalance();
			//
			//		// deposits amount
			//		userBalance += amount;
			//
			//		// push new balance to DB
			//		accDaoImpl.updateBalance(userBalance, accId);

			// create a timestamp and push to transaction history for user
			// transactionRepo.save(new Transaction(0, userId, "Deposit of " + amount));

			Account acc = accountRepo.findByAccountTypeAndUserUserId(accountType, userid).get();

			if(InputValidationUtil.positiveNumber(amount)) {
				acc.deposit(amount);
				accountRepo.save(acc);

				addNewTransaction(userid, TransactionUtil.deposit(amount, accountType));
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// since IDs are auto-generated, dont need as params
	// full params listed (int userId, String description)
	public void addHistory(int id, String description) {
		Date timestamp = new java.util.Date();

		transactionRepo.save(new Transaction(0, id, description + " @ " + sdf.format(timestamp)));

	}

	public boolean accountValidation(String accountName) {
		return InputValidationUtil.validAccountType(accountName);
	}
	
	public void addNewUser(String firstName, String lastName, String address, String contactNum, String password) {
		User newUser = new User();
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setAddress(address);
		newUser.setContactNum(contactNum);
		newUser.setPassword(password);
		userRepo.save(newUser);
	}
	
	public void addNewAccount(int userid, String accountType, float balance) {
		Account newAccount = new Account();
		newAccount.setUser(userid);
		newAccount.setAccountType(accountType);
		newAccount.setBalance(balance);
		accountRepo.save(newAccount);
	}
	
	public void addNewTransaction(int userid, String description) {
		Transaction newTransaction = new Transaction();
		newTransaction.setUser(userid);
		newTransaction.setDescription(description);
		transactionRepo.save(newTransaction);
	}

}
