package com.kh.even.back.payment.model.service;

import java.util.Map;

import com.kh.even.back.payment.model.dto.PaymentCancelRequest;
import com.kh.even.back.payment.model.dto.PaymentDetailRequest;
import com.kh.even.back.payment.model.dto.PaymentPrepareRequest;
import com.kh.even.back.payment.model.dto.PaymentVerifyRequest;

public interface PaymentService {
    
    /**
     * 결제 사전 등록
     */
    Map<String, Object> preparePayment(PaymentPrepareRequest request);
    
    /**
     * 결제 검증
     */
    Map<String, Object> verifyPayment(PaymentVerifyRequest request);
    
    /**
     * 결제 취소
     */
    Map<String, Object> cancelPayment(PaymentCancelRequest request);
    
    /**
     * 결제 상세 조회
     */
    Map<String, Object> getPaymentDetail(PaymentDetailRequest request);
}