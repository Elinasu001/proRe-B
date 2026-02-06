package com.kh.even.back.payment.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.payment.model.dto.PaymentCancelRequest;
import com.kh.even.back.payment.model.dto.PaymentDetailRequest;
import com.kh.even.back.payment.model.dto.PaymentPrepareRequest;
import com.kh.even.back.payment.model.dto.PaymentVerifyRequest;
import com.kh.even.back.payment.model.vo.PaymentVO;

@Mapper
public interface PaymentMapper {
    
    // 결제 준비 정보 저장 (READY)
    int insertPreparePayment(PaymentPrepareRequest request);
    
    // merchant_uid로 결제 정보 조회
    PaymentVO selectPaymentByMerchantUid(PaymentVerifyRequest request);
    
    // imp_uid로 결제 정보 조회
    PaymentVO selectPaymentByImpUid(PaymentCancelRequest request);
    
    // 결제 완료 상태 업데이트 (READY → PAID)
    int updatePaymentStatus(PaymentVerifyRequest request);
    
    // 결제 취소 상태 업데이트 (PAID → FAILED)
    int updateCancelStatus(PaymentCancelRequest request);
    
    // 결제 상세 조회
    PaymentVO selectPaymentDetail(PaymentDetailRequest request);
}