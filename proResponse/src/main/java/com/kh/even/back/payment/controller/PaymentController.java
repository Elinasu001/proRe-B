package com.kh.even.back.payment.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.payment.model.dto.PaymentCancelRequest;
import com.kh.even.back.payment.model.dto.PaymentPrepareRequest;
import com.kh.even.back.payment.model.dto.PaymentVerifyRequest;
import com.kh.even.back.payment.model.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 결제 사전 등록
     */
    @PostMapping("/prepare")
    public ResponseEntity<Map<String, Object>> prepare(
            @RequestBody PaymentPrepareRequest request) {
        Map<String, Object> response = paymentService.preparePayment(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 결제 검증
     */
    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify(
            @RequestBody PaymentVerifyRequest request) {
        Map<String, Object> response = paymentService.verifyPayment(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 결제 취소
     */
    @PostMapping("/cancel")
    public ResponseEntity<Map<String, Object>> cancel(
            @RequestBody PaymentCancelRequest request) {
        Map<String, Object> response = paymentService.cancelPayment(request);
        return ResponseEntity.ok(response);
    }
}