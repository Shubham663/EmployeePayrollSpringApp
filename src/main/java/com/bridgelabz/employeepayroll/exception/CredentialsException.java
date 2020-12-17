package com.bridgelabz.employeepayroll.exception;

/**
 * @author Shubham
 * Exception class whose object is thrown when login credentials are invalid.
 */
@SuppressWarnings("serial")
public class CredentialsException extends RuntimeException{
	public CredentialsException(String message) {
		super(message);
	}
}
