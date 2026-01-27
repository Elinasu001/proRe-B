package com.kh.even.back.exception;

public class EmailAuthFailException extends RuntimeException {
	
	public EmailAuthFailException(String message) {
        super(message);
    }
}
