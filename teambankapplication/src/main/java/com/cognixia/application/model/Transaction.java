package com.cognixia.application.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="DBTransaction")
public class Transaction {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "transaction_id")
	private Integer transactionId;
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "transaction_description")
	private String description;

	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}/*
	 * <<<<<<< HEAD } ======= } >>>>>>> a7e6be9217a15df94f81a75aa73ac9a7040f7561
	 */
