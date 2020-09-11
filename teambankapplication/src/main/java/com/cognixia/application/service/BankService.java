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

		if (InputValidationUtil.validAccountType(accountType)) {

			try {
				Account acc = accountRepo.findByAccountTypeAndUserUserId(accountType, userid).get();

				if (InputValidationUtil.positiveNumber(amount)) {
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
		if (InputValidationUtil.validAccountType(accountType)) {
			try {
				Account acc = accountRepo.findByAccountTypeAndUserUserId(accountType, userid).get();
				if (InputValidationUtil.positiveNumber(amount) && InputValidationUtil.sufficientFunds(amount, acc)) {
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

	public boolean transfer(int giveUserid, int receiveUserid, float amount, String giveAccountType,
			String receiveAccountType) {
		if (InputValidationUtil.userExists(receiveUserid, userRepo)) {
			if (InputValidationUtil.validAccountType(giveAccountType)
					&& InputValidationUtil.validAccountType(receiveAccountType)) {
				try {
					Account giveAcc = accountRepo.findByAccountTypeAndUserUserId(giveAccountType, giveUserid).get();
					Account receiveAcc = accountRepo.findByAccountTypeAndUserUserId(receiveAccountType, receiveUserid)
							.get();
					if (InputValidationUtil.positiveNumber(amount)
							&& InputValidationUtil.sufficientFunds(amount, giveAcc)) {
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

	public boolean register(String firstName, String lastName, String address, String contactNum, String password,
			float initialDeposit, String accountType) {

		if (InputValidationUtil.validAccountType(accountType) && InputValidationUtil.positiveNumber(initialDeposit)) {
			if (InputValidationUtil.validPassword(password) && InputValidationUtil.validPhoneNum(contactNum)) {
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

	public boolean accountValidation(String accountName) {
		return InputValidationUtil.validAccountType(accountName);
	}

	public boolean addNewUser(String firstName, String lastName, String address, String contactNum, String password) {
		if (InputValidationUtil.validPassword(password) && InputValidationUtil.validPhoneNum(contactNum)) {
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
