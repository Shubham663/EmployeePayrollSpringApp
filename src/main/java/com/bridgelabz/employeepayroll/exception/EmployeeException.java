package com.bridgelabz.employeepayroll.exception;

/**
 * @author Shubham
 * Exception class whose object is thrown when Database Operation is unsuccessfull
 */
@SuppressWarnings("serial")
public class EmployeeException extends RuntimeException{
	public EmployeeException(String message) {
		super(message);
	}
}
