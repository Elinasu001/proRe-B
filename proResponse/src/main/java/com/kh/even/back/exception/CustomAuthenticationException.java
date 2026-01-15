package com.kh.even.back.exception;

public class CustomAuthenticationException extends RuntimeException{

	public CustomAuthenticationException(String message) {
		super(message);
	}
}
