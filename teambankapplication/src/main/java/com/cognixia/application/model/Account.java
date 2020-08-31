package com.cognixia.application.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Account {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "account_id")
	private Integer accountId;
	
//	@Column(name = "user_id")
//	private Integer userId;
	
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

}
