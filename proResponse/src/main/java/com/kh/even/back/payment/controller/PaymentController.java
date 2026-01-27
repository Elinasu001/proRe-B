package com.kh.even.back.payment.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.common.ResponseData;
import com.kh.even.back.payment.model.dto.PaymentDetailDTO;
import com.kh.even.back.payment.model.dto.PreparePaymentRequest;
import com.kh.even.back.payment.model.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{estimateNo}")
    public ResponseEntity<ResponseData<PaymentDetailDTO>> getPayment(
            @PathVariable("estimateNo") Long estimateNo,
            @AuthenticationPrincipal CustomUserDetails user) {

        PaymentDetailDTO payment = paymentService.getPayment(estimateNo, user.getUserNo());
        return ResponseData.ok(payment, "결제 정보 조회에 성공했습니다.");
    }


    /**
     * 주문서에 나타낼 정보
     */
    @PostMapping(value = "/prepare")
    public ResponseEntity<ResponseData<PaymentDetailDTO>> preparePayment(
            @Valid @RequestBody PreparePaymentRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {

        PaymentDetailDTO payment = paymentService.preparePayment(
            request, user.getUserNo()
        );
        return ResponseData.ok(payment, "결제 준비가 완료되었습니다.");
    }

}
