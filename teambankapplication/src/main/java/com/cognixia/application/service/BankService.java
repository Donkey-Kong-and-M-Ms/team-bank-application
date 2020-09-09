package com.cognixia.application.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognixia.application.dao.AccountDaoImpl;
import com.cognixia.application.model.Transaction;
import com.cognixia.application.repository.TransactionRepository;
import com.cognixia.application.utility.InputValidationUtil;

@Service
public class BankService {

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

	// since IDs are auto-generated, dont need as params
	// full params listed (int userId, String description)
	public void addHistory(int id, String description) {
		Date timestamp = new java.util.Date();

		transactionRepo.save(new Transaction(0, id, description + " @ " + sdf.format(timestamp)));

	}

	public boolean accountValidation(String accountName) {
		return InputValidationUtil.validAccountType(accountName);
	}

}
