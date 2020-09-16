package com.cognixia.application.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TransactionUtil {
	
	public static String getTime() {
		Date date = new Date();
		TimeZone.setDefault(TimeZone.getTimeZone("US/Eastern"));
		SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm z");
		String strDate = formatter.format(date);
		return strDate;
	}

	public static String deposit(float amount, String accountType) {
		return "Deposited $" + amount + " to " + accountType + ": " + getTime();
	}
	
	public static String withdraw(float amount, String accountType) {
		return "Withdrew $" + amount + " from " + accountType + ": " + getTime();
	}
	
	public static String giveTransfer(float amount, String otherUser) {
		return "Transferred $" + amount + " to " + otherUser + ": " + getTime();
	}
	
	public static String receiveTransfer(float amount, String otherUser) {
		return "Received $" + amount + " from " + otherUser + ": " + getTime();
	}
	
	public static String register(float amount, String user) {
		return "Registered account for " + user + " with initial deposit of $" + amount + ": " + getTime();
	}
}
