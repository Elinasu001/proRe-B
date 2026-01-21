package com.kh.even.back.mail.model.dto;

public record EmailVerificationResult(boolean verified) {
	
    public static EmailVerificationResult of(boolean verified) {
        return new EmailVerificationResult(verified);
    }
    
}
