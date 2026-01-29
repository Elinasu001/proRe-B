package com.kh.even.back.payment.model.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kh.even.back.exception.PaymentException;
import com.kh.even.back.payment.model.dao.PaymentMapper;
import com.kh.even.back.payment.model.dto.PaymentDetailDTO;
import com.kh.even.back.payment.model.dto.PreparePaymentRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final RestTemplate restTemplate;

    @Value("${imp.api.key}")
    private String impApiKey;

    @Value("${imp.api.secret}")
    private String impApiSecret;

    @Override
    public PaymentDetailDTO getPayment(Long estimateNo, Long userNo) {

        PaymentDetailDTO paymentDetailDto= paymentMapper.getByEstimateNo(estimateNo);
        if (paymentDetailDto == null) {
            throw new PaymentException("해당 견적에 대한 결제 정보가 존재하지 않습니다");
        }

        return paymentDetailDto;
    }

    @Override
    public PaymentDetailDTO preparePayment(PreparePaymentRequest request, Long userNo) {
        
        log.info("결제 준비 시작 - roomNo: {}, estimateNo: {}, amount: {}", request.getRoomNo(), request.getEstimateNo(), request.getAmount());
        
        // 1. 기존 결제 내역 조회
        PaymentDetailDTO existingPayment = paymentMapper.getByRoomNo(request.getRoomNo());
        
        // 2. 기존 결제가 있는 경우
        if (existingPayment != null) {

            log.info("기존 결제 내역 발견 - paymentNo: {}, status: {}", existingPayment.getPaymentNo(), existingPayment.getStatus());
            
            // 이미 결제 완료된 경우    
            if ("PAID".equals(existingPayment.getStatus()) || "DONE".equals(existingPayment.getStatus())) {
                throw new PaymentException("이미 결제가 완료되었습니다");
            }
            
            // READY 또는 FAILED 상태면 재사용
            if ("READY".equals(existingPayment.getStatus()) || "FAILED".equals(existingPayment.getStatus())) {

                log.info("기존 결제 정보 재사용 - merchantUid: {}", existingPayment.getMerchantUid());

                return existingPayment;
            }
        }
        
        // 3. 새로운 결제 정보 생성
        PaymentDetailDTO newPayment = new PaymentDetailDTO();
        newPayment.setRoomNo(request.getRoomNo());
        newPayment.setAmount(request.getAmount());
        newPayment.setStatus("READY");
        newPayment.setEstimateNo(request.getEstimateNo());
        
        // 주문 테이블 저장
        String merchantUid = generateMerchantUid();
        newPayment.setMerchantUid(merchantUid);
        
        // 4. DB에 저장
        int result = paymentMapper.savePayment(newPayment);
        if (result <= 0) {
            throw new PaymentException("결제 정보 생성에 실패했습니다");
        }
        
        log.info("결제 준비 완료 - paymentNo: {}, merchantUid: {}", newPayment.getPaymentNo(), newPayment.getMerchantUid());
        
        return newPayment;
    }


    /**
     * 주문번호 생성 메서드
     */
    private String generateMerchantUid(){
        // 현재 날짜와 시간 포함하여 고유 문자열 생성
        String uniqueString = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDay = today.format(formatter).replace("-", "");

        // 랜덤 문자열  + 현재 날짜 및 시간 조합한 주문번호
        return formattedDay + "-" + uniqueString;
    }

	@Override
	public void verifyAndCompletePayment(String impUid, String merchantUid) {
		// TODO Auto-generated method stub
		
	}

        

}
