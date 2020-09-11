package com.cognixia.application.dao;

import com.cognixia.application.model.Account;

public interface AccountDao {

	public int updateBalance(float userBal, int accId);

	public float getBalanceByAccountId(int accId);

	public Account getAccountByUserIdAndAccountType(int userId, String accountType);

}
