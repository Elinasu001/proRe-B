package com.kh.even.back.payment.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.payment.model.dto.PaymentCancelRequest;
import com.kh.even.back.payment.model.dto.PaymentPrepareRequest;
import com.kh.even.back.payment.model.dto.PaymentVerifyRequest;
import com.kh.even.back.payment.model.vo.PaymentVO;

@Mapper
public interface PaymentMapper {

    /**
     * 견적 번호로 READY 상태 결제 정보 조회
     */
    PaymentVO selectReadyPayment(Long estimateNo);

    /**
     * 결제 사전 등록 (READY 상태로 저장)
     */
    int insertPreparePayment(PaymentPrepareRequest request);
    
    /**
     * merchantUid로 결제 정보 조회
     */
    PaymentVO selectPaymentByMerchantUid(PaymentVerifyRequest request);
    
    /**
     * 결제 상태 업데이트 (READY → PAID)
     */
    int updatePaymentStatus(PaymentVerifyRequest request);
    
    /**
     * impUid로 결제 정보 조회
     */
    PaymentVO selectPaymentByImpUid(PaymentCancelRequest request);
    
    /**
     * 결제 취소 상태 업데이트 (PAID → FAILED)
     */
    int updateCancelStatus(PaymentCancelRequest request);
}