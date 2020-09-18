package com.cognixia.application.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "dbaccount")
public class Account {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "account_id")
	private Integer accountId;
	
	@ManyToOne(targetEntity = User.class)
	private User user;
	
	@Column(name = "account_type")
	private String accountType;

	@Column(name = "account_value")
	private float balance;

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getUserId() {
		return user.getUserId();
	}

	public void setUserId(Integer userId) {
		this.user.setUserId(userId);
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
  }
  
	public Account() {
		super();
	}

	public Account(Integer accountId, Integer userId, String accountType, float balance) {
		super();
		this.accountId = accountId;
		this.user.setUserId(userId);
		this.accountType = accountType;
		this.balance = balance;
	}


	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", user=" + user + ", accountType=" + accountType + ", balance="
				+ balance + "]";
	}

	public void setUser(int userId) {
		User newUser = new User();
		newUser.setUserId(userId);
		this.user = newUser;
	}
	
	public void deposit(float deposit) {
		this.balance += deposit;
	}
	
	public void withdraw(float withdraw) {
		this.balance -= withdraw;
	}

}
