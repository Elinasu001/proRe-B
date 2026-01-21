package com.kh.even.back.payment.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.payment.model.dto.PaymentDetailDTO;
import com.kh.even.back.payment.model.dto.PaymentSearchDTO;

@Mapper
public interface PaymentMapper {

    /**
     * 채팅방 기준 결제 정보 조회
     */
    PaymentDetailDTO getByRoom(PaymentSearchDTO dto);

}
