package com.kh.even.back.payment.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.payment.model.dto.PaymentDetailDTO;

@Mapper
public interface PaymentMapper {

    /* 견적 번호 기준 거래 상세 조회
     * @param estimateNo
     * @return 거래 상세 DTO(PaymentDetailDTO)
     */
    PaymentDetailDTO getByEstimateNo(Long estimateNo);

    /**
     * 결제 정보 생성
     */
    int savePayment(PaymentDetailDTO newPayment);

    /**
     * 채팅방 번호로 결제 조회
     */
    PaymentDetailDTO getByRoomNo(Long roomNo);

}
