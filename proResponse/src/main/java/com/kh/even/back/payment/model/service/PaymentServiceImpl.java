package com.kh.even.back.payment.model.service;

import org.springframework.stereotype.Service;

import com.kh.even.back.payment.model.dao.PaymentMapper;
import com.kh.even.back.payment.model.dto.PaymentDetailDTO;
import com.kh.even.back.payment.model.dto.PaymentSearchDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;

    @Override
    public PaymentDetailDTO getPayment(Long roomNo, Long userNo) {

        PaymentSearchDTO dto = new PaymentSearchDTO();
        dto.setRoomNo(roomNo);
        dto.setUserNo(userNo);

        return paymentMapper.getByRoom(dto);
    }
}
