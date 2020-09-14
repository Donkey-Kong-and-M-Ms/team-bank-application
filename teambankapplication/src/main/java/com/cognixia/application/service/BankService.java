package com.cognixia.application.service;

import java.util.NoSuchElementException;

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

	// All inputs must be correct to deposit
	public boolean deposit(int userid, float amount, String accountType) {
		
		// Check if the account type is a valid one
		if (InputValidationUtil.validAccountType(accountType)) {

			// Search for the account with the user id and account type
			try {
				Account acc = accountRepo.findByAccountTypeAndUserUserId(accountType, userid).get();

				// Check if the amount given is positive
				if (InputValidationUtil.positiveNumber(amount)) {
					// Change the account's balance, save to the repository, and add transaction
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

	// All inputs must be correct to withdraw
	public boolean withdraw(int userid, float amount, String accountType) {
		
		// Check if the account type is a valid one
		if (InputValidationUtil.validAccountType(accountType)) {
			
			// Search for the account with the user id and account type
			try {
				Account acc = accountRepo.findByAccountTypeAndUserUserId(accountType, userid).get();
				
				// Check if the amount is positive and that the account has enough in the balance
				if (InputValidationUtil.positiveNumber(amount) && InputValidationUtil.sufficientFunds(amount, acc)) {
					// Change the account's balance, save to the repository, and add transaction
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

	// All inputs must be correct to transfer
	public boolean transfer(int giveUserid, int receiveUserid, float amount, String giveAccountType,
			String receiveAccountType) {
		
		// Check that the other user exists in the repository
		if (InputValidationUtil.userExists(receiveUserid, userRepo)) {
			
			// Check if both account types are valid
			if (InputValidationUtil.validAccountType(giveAccountType) && InputValidationUtil.validAccountType(receiveAccountType)) {
				
				// Search for the giving and receiving accounts
				try {
					Account giveAcc = accountRepo.findByAccountTypeAndUserUserId(giveAccountType, giveUserid).get();
					Account receiveAcc = accountRepo.findByAccountTypeAndUserUserId(receiveAccountType, receiveUserid).get();
					
					// If the amount is positive and the giving account has enough in the balance
					if (InputValidationUtil.positiveNumber(amount) && InputValidationUtil.sufficientFunds(amount, giveAcc)) {
						// Change the account balances, save to the repository, and add transactions to both users
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

	// Register with the bank, creating a user, account, and transaction
	public boolean register(String firstName, String lastName, String address, String contactNum, String password,
			float initialDeposit, String accountType) {

		// Check if the account type is valid and the initial deposit is positive
		if (InputValidationUtil.validAccountType(accountType) && InputValidationUtil.positiveNumber(initialDeposit)) {
			// Check if a new user can be added with the information
			if (addNewUser(firstName, lastName, address, contactNum, password)) {
				// Retrieve the user id that was just added
				Integer userId = userRepo.getOne((int) userRepo.count()).getUserId();

				// Add new account and transaction related to user id
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

	// Check that the account type is valid
	public boolean accountValidation(String accountName) {
		return InputValidationUtil.validAccountType(accountName);
	}

	// Add a new user to the repository
	public boolean addNewUser(String firstName, String lastName, String address, String contactNum, String password) {
		// Check that the password fits the criteria and the phone number is in the accepted form
		if (InputValidationUtil.validPassword(password) && InputValidationUtil.validPhoneNum(contactNum)) {
			// Create a new user and save to the repository
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

	// Add a new account to the repository
	public void addNewAccount(int userid, String accountType, float balance) {
		Account newAccount = new Account();
		newAccount.setUser(userid);
		newAccount.setAccountType(accountType);
		newAccount.setBalance(balance);
		accountRepo.save(newAccount);
	}

	// Add a new transaction to the repository
	public void addNewTransaction(int userid, String description) {
		Transaction newTransaction = new Transaction();
		newTransaction.setUser(userid);
		newTransaction.setDescription(description);
		transactionRepo.save(newTransaction);
	}

}
