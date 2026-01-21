package com.kh.even.back.exception;

public class CustomServerException extends RuntimeException {
	
	public CustomServerException(String message) {
        super(message);
    }
}
