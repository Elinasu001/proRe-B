package com.kh.even.back.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.common.ResponseData;
import com.kh.even.back.payment.model.dto.PaymentDetailDTO;
import com.kh.even.back.payment.model.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{roomNo}")
    public ResponseEntity<ResponseData<PaymentDetailDTO>> getPayment(
            @PathVariable Long roomNo,
            @AuthenticationPrincipal CustomUserDetails user) {

        PaymentDetailDTO payment = paymentService.getPayment(roomNo, user.getUserNo());
        return ResponseData.ok(payment, "결제 정보 조회 성공");
    }
}
