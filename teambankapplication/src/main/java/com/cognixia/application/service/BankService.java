package com.cognixia.application.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognixia.application.model.Transaction;
import com.cognixia.application.repository.TransactionRepository;

@Service
public class BankService {

	@Autowired
	TransactionRepository transactionRepo;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd - HH.mm.ss");

	//deposit method used in Bank controller
	public float deposit(float balance, float amount) {
		balance = balance + amount;
		return balance;
	}

	//withdraw method used in Bank controller
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

	// validation for phone number
	public boolean phoneValidation(String contactNum) {

		// checks to see if there is a format of (XXX)XXX-XXXX
		if (contactNum.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) {

			return true;
		} else {
			return false;
		}

	}
}