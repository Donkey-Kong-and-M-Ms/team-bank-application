package com.cognixia.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognixia.application.model.Transaction;
import com.cognixia.application.repository.TransactionRepository;

@Service
public class BankService {

	@Autowired
	TransactionRepository transactionRepo;

	public float deposit(float balance, float amount) {

		// implementation is in controller
		// addHistory(transactionId, userId, description)
		balance = balance + amount;
		return balance;
	}

	public float withdraw(float balance, float amount) {
		// implementation is in controller
		// addHistory(transactionId, userId, description)

		balance = balance - amount;
		return balance;
	}

	//since IDs are auto-generated, dont need as params 
	//full params listed (int transactionId, int userId, String description)
	public Transaction addHistory(String description) {

		Transaction trans = new Transaction();

		// transId and userId is not needed since it is auto generated
		/*
		 * trans.setTransactionId(transactionId); trans.setUserId(userId);
		 */
		trans.setDescription(description);

		transactionRepo.save(trans);

		return trans;
	}
	
	//validation for phone number
	
	//validation in fund transfer, withdraw, deposit, init deposit - make sure not negative - DANIEL
	
	//validation for sufficient funds - DANIEL

}
