package com.kh.even.back.exception;

public class CustomAuthorizationException extends RuntimeException {

    public CustomAuthorizationException(String message) {
        super(message);
    }
}