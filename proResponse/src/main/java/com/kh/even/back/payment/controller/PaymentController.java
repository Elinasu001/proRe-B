package com.kh.even.back.payment.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
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
            @ModelAttribute PaymentPrepareRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        // userNo 설정
        request.setUserNo(user.getUserNo());
        Map<String, Object> response = paymentService.preparePayment(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 결제 검증
     */
    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify(
            @ModelAttribute PaymentVerifyRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        
        // userNo 설정
        request.setUserNo(user.getUserNo());
        
        Map<String, Object> response = paymentService.verifyPayment(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 결제 취소
     */
    @PostMapping("/cancel")
    public ResponseEntity<Map<String, Object>> cancel(
            @ModelAttribute PaymentCancelRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        
        // userNo 설정
        request.setUserNo(user.getUserNo());
        
        Map<String, Object> response = paymentService.cancelPayment(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 결제 상세 조회
     */
    // @GetMapping("/detail")
    // public ResponseEntity<Map<String, Object>> getPaymentDetail(
    //         @RequestParam String impUid,
    //         @AuthenticationPrincipal CustomUserDetails user) {
        
    //     PaymentDetailRequest request = new PaymentDetailRequest();
    //     request.setUserNo(user.getUserNo());
    //     request.setImpUid(impUid);
        
    //     Map<String, Object> response = paymentService.getPaymentDetail(request);
    //     return ResponseEntity.ok(response);
    // }
}