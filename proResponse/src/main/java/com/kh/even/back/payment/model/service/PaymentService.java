package com.kh.even.back.payment.model.service;

import com.kh.even.back.payment.model.dto.PaymentDetailDTO;
import com.kh.even.back.payment.model.dto.PreparePaymentRequest;

public interface PaymentService {

    /**
     * 채팅방 기준 결제 정보 조회
     */
    PaymentDetailDTO getPayment(Long estimateNo, Long userNo);

    /**
     * 결제 준비
     */
    PaymentDetailDTO preparePayment(PreparePaymentRequest request, Long userNo);

    /**
     * 결제 완료 검증 및 처리
     */
    void verifyAndCompletePayment(String impUid, String merchantUid);
}
