package com.kh.even.back.payment.model.service;

import com.kh.even.back.payment.model.dto.PaymentDetailDTO;

public interface PaymentService {

    /**
     * 채팅방 기준 결제 정보 조회
     */
    PaymentDetailDTO getPayment(Long roomNo, Long userNo);

}
