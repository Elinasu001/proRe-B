package com.kh.even.back.payment.model.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; 

import com.kh.even.back.payment.model.dto.PaymentCancelRequest;
import com.kh.even.back.payment.model.dto.PaymentPrepareRequest;
import com.kh.even.back.payment.model.dto.PaymentVerifyRequest;
import com.kh.even.back.payment.model.vo.PaymentVO;

@Mapper
public interface PaymentMapper {

    /**
     * 견적 번호로 READY 상태 결제 정보 조회
     */
    PaymentVO selectReadyPayment(@Param("estimateNo") Long estimateNo);

    /**
     * 결제 사전 등록 (V2)
     */
    int insertPreparePayment(PaymentPrepareRequest request);
    
    /**
     * merchantUid로 결제 정보 조회 (V2)
     */
    PaymentVO selectPaymentByMerchantUid(@Param("merchantUid") String merchantUid);
    
    /**
     * 결제 상태 업데이트 (READY → PAID) (V2)
     */
    int updatePaymentStatus(PaymentVerifyRequest request);
    
    /**
     * 결제 취소 상태 업데이트 (PAID → CANCELLED) (V2)
     */
    int updateCancelStatus(PaymentCancelRequest request);

    /**
     * 결제 완료 시 TB_ESTIMATE_RESPONSE 상태 업데이트
     */
    int updateEstimateResponseStatus(@Param("estimateNo") Long estimateNo);

    /**
     * 결제 완료 시 TB_ESTIMATE_REQUEST 상태 업데이트
     */
    int updateEstimateRequestStatus(@Param("estimateNo") Long estimateNo);
}