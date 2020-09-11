package com.cognixia.application.utility;

public class ErrorUtil {
	
	public static String errorNotEnough() {
		return "Not enough in balance";
	}
	
	public static String errorNotPositive() {
		return "Number input was not positive number";
	}
	
	public static String errorNotPhone() {
		return "Contact number is not a valid phone number";
	}
	
	public static String errorPassword() {
		return "Password does not match criteria";
	}
	
	public static String errorLogin() {
		return "Invalid credentials";
	}
	
	public static String errorUserNotFound() {
		return "No user found with that ID";
	}
	
	public static String errorAccountNotFound() {
		return "No account found";
	}
	
	public static String errorDepositFailed() {
		return "Deposit failed";
	}
	
	public static String errorWithdrawFailed() {
		return "Withdraw failed";
	}
	
	public static String errorTransferFailed() {
		return "Transfer failed";
	}
	
	public static String errorRegisterFailed() {
		return "Register failed";
	}
	
	public static String errorAccountCreationFailed() {
		return "Account creation failed";
	}
	
	public static String errorLogoutFailed() {
		return "Logout failed";
	}

}
