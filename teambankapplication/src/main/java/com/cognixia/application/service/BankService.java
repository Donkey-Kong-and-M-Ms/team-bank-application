package com.cognixia.application.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.cognixia.application.dao.AccountDaoImpl;
import com.cognixia.application.model.Account;
import com.cognixia.application.model.Transaction;
import com.cognixia.application.model.User;
import com.cognixia.application.repository.AccountRepository;
import com.cognixia.application.repository.TransactionRepository;
import com.cognixia.application.repository.UserRepository;
import com.cognixia.application.utility.ErrorUtil;
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

			try {
				Account acc = accountRepo.findByAccountTypeAndUserUserId(accountType, userid).get();

				if(InputValidationUtil.positiveNumber(amount)) {
					acc.deposit(amount);
					accountRepo.save(acc);

					addNewTransaction(userid, TransactionUtil.deposit(amount, accountType));
					return true;
				} else {
					return false;
				}
			} catch (NoSuchElementException nse) {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean withdraw(int userid, float amount, String accountType) {
		if(InputValidationUtil.validAccountType(accountType)) {
			try {
				Account acc = accountRepo.findByAccountTypeAndUserUserId(accountType, userid).get();
				if(InputValidationUtil.positiveNumber(amount) && InputValidationUtil.sufficientFunds(amount, acc)) {
					acc.withdraw(amount);
					accountRepo.save(acc);

					addNewTransaction(userid, TransactionUtil.withdraw(amount, accountType));
					return true;
				} else {
					return false;
				}
			} catch (NoSuchElementException nse) {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean transfer(int giveUserid, int receiveUserid, float amount, String giveAccountType, String receiveAccountType) {
		if(InputValidationUtil.userExists(receiveUserid, userRepo)) {
			if(InputValidationUtil.validAccountType(giveAccountType) && InputValidationUtil.validAccountType(receiveAccountType)) {
				try {
					Account giveAcc = accountRepo.findByAccountTypeAndUserUserId(giveAccountType, giveUserid).get();
					Account receiveAcc = accountRepo.findByAccountTypeAndUserUserId(receiveAccountType, receiveUserid).get();
					if(InputValidationUtil.positiveNumber(amount) && InputValidationUtil.sufficientFunds(amount, giveAcc)) {
						giveAcc.withdraw(amount);
						accountRepo.save(giveAcc);
						addNewTransaction(giveUserid, TransactionUtil.giveTransfer(amount, receiveAccountType));

						receiveAcc.deposit(amount);
						accountRepo.save(receiveAcc);
						addNewTransaction(receiveUserid, TransactionUtil.receiveTransfer(amount, giveAccountType));

						return true;
					} else {
						return false;
					}
				} catch (NoSuchElementException nse) {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean register( String firstName, String lastName,
			 String address, String contactNum, String password,
			 float initialDeposit, String accountType) {
		
		if(InputValidationUtil.validAccountType(accountType) && InputValidationUtil.positiveNumber(initialDeposit)) {
			if(InputValidationUtil.validPassword(password) && InputValidationUtil.validPhoneNum(contactNum)) {
				addNewUser(firstName, lastName, address, contactNum, password);
				
				Integer userId = userRepo.getOne((int) userRepo.count()).getUserId();

				addNewAccount(userId, accountType, initialDeposit);
				addNewTransaction(userId, TransactionUtil.register(initialDeposit, firstName + " " + lastName));
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
//	// since IDs are auto-generated, dont need as params
//	// full params listed (int userId, String description)
//	public void addHistory(int id, String description) {
//		Date timestamp = new java.util.Date();
//
//		transactionRepo.save(new Transaction(0, id, description + " @ " + sdf.format(timestamp)));
//
//	}

	public boolean accountValidation(String accountName) {
		return InputValidationUtil.validAccountType(accountName);
	}
	
	public boolean addNewUser(String firstName, String lastName, String address, String contactNum, String password) {
		if(InputValidationUtil.validPassword(password) && InputValidationUtil.validPhoneNum(contactNum)) {
			User newUser = new User();
			newUser.setFirstName(firstName);
			newUser.setLastName(lastName);
			newUser.setAddress(address);
			newUser.setContactNum(contactNum);
			newUser.setPassword(password);
			userRepo.save(newUser);
			return true;
		} else {
			return false;
		}
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
