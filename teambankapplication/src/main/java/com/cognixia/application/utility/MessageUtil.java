package com.cognixia.application.utility;

public class MessageUtil {
	
	public static String newBalance(float amount) {
		return "New balance: " + amount;
	}
	
	public static String passwordCriteria() {
		return "At least 1 Uppercase, 1 Lowercase, 1 Special character, and between 8-30 characters long";
	}
	
	public static String phoneNumForm() {
		return "Must be like the following number: (555)123-4567";
	}

}
