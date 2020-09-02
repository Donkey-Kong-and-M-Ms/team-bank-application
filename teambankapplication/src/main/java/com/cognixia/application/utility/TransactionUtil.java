package com.cognixia.application.utility;

public class TransactionUtil {

	public static String deposit(float amount, String accountType) {
		return "Deposited $" + amount + " to " + accountType;
	}
	
	public static String withdraw(float amount, String accountType) {
		return "Withdrew $" + amount + " from " + accountType;
	}
	
	public static String giveTransfer(float amount, String otherUser) {
		return "Transferred $" + amount + " to " + otherUser;
	}
	
	public static String receiveTransfer(float amount, String otherUser) {
		return "Received $" + amount + " from " + otherUser;
	}
	
	public static String register(float amount, String user) {
		return "Registered account for " + user + " with initial deposit of $" + amount;
	}
}
