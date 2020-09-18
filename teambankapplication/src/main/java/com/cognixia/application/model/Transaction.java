package com.cognixia.application.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "dbtransaction")
public class Transaction {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "transaction_id")
	private Integer transactionId;
	
	@ManyToOne(targetEntity = User.class)
	private User user;
  
	@Column(name = "transaction_description")
	private String description;

	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getUserId() {
		return user.getUserId();
	}

	public void setUserId(Integer userId) {
		this.user.setUserId(userId);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public void setUser(int userId) {
		User newUser = new User();
		newUser.setUserId(userId);
		this.user = newUser;
	}

	public Transaction(Integer transactionId, Integer userId, String description) {
		super();
		this.transactionId = transactionId;
		this.user.setUserId(userId);
		this.description = description;
	}

	public Transaction() {
		super();
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", user=" + user + ", description=" + description + "]";
	}

	
}
