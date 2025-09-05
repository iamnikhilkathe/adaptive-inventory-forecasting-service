package com.nikhil.inventory.adaptiveinventoryforecastingservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class UserAuthenticationException extends AuthenticationException {
 
	private HttpStatus httpStatus;
	
	
	 public UserAuthenticationException(String message) {
	        super(message);
	    }
	 
	    public UserAuthenticationException(String message, Throwable cause) {
	        super(message, cause);
	    }
	
	 
	 public UserAuthenticationException(String message,HttpStatus statusCode) {
	        super(message);
	        this.httpStatus=statusCode;
	    }

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	
	 
	 
}
