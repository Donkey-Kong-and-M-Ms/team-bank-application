package com.cognixia.application.dao;

import com.cognixia.application.model.Account;

public interface AccountDao {
	
	public int updateBalance(float userBal, int accId);
	
	public Account getAccountByUserId(int userId);
	
	public float getBalanceByAccountId(int accId);

}
