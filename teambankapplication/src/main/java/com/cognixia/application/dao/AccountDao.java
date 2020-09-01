package com.cognixia.application.dao;

public interface AccountDao {
	
	public int updateBalance(float userBal, int accId);
	
	public int getAccountIdByUserId(int userId);
	
	public float getBalanceByAccountId(int accId);

}
