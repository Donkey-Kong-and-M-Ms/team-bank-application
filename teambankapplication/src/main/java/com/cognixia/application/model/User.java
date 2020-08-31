package com.cognixia.application.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "contact_number")
	private String contactNum;
	
	@Column(name = "user_password")
	private String password;

	@Column(name = "initial_deposit")
	private float initialDeposit;
	
//	@OneToMany(targetEntity = Account.class)
//	private List accountList;
//	
//	@OneToMany(targetEntity = Transaction.class)
//	private List transactionList;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNum() {
		return contactNum;
	}

	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public float getInitialDeposit() {
		return initialDeposit;
	}

	public void setInitialDeposit(float initialDeposit) {
		this.initialDeposit = initialDeposit;
	}

//	public List getAccountList() {
//		return accountList;
//	}
//
//	public void setAccountList(List accountList) {
//		this.accountList = accountList;
//	}
//
//	public List getTransactionList() {
//		return transactionList;
//	}
//
//	public void setTransactionList(List transactionList) {
//		this.transactionList = transactionList;
//	}
	
}