package com.kh.even.back.payment.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kh.even.back.payment.model.dao.PaymentMapper;
import com.kh.even.back.payment.model.dto.PaymentCancelRequest;
import com.kh.even.back.payment.model.dto.PaymentDetailRequest;
import com.kh.even.back.payment.model.dto.PaymentPrepareRequest;
import com.kh.even.back.payment.model.dto.PaymentVerifyRequest;
import com.kh.even.back.payment.model.vo.PaymentVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${imp.api.key}")
    private String apiKey;

    @Value("${imp.api.secret}")
    private String apiSecret;

    private final RestTemplate restTemplate;
    private final PaymentMapper paymentMapper;
    
    private static final String API_URL = "https://api.iamport.kr";

    /**
     * 포트원 API 토큰 발급
     */
    private String getPortoneToken() {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("imp_key", apiKey);
            body.put("imp_secret", apiSecret);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                API_URL + "/users/getToken", body, Map.class);
            
            Map<String, Object> data = (Map<String, Object>) response.getBody().get("response");
            return (String) data.get("access_token");
        } catch (Exception e) {
            log.error("포트원 토큰 발급 실패", e);
            throw new RuntimeException("토큰 발급 실패");
        }
    }

    /**
     * 결제 사전 등록
     */
    @Override
    public Map<String, Object> preparePayment(PaymentPrepareRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 입력값 검증
            if (request.getAmount() == null || request.getAmount() <= 0) {
                result.put("success", false);
                result.put("message", "결제 금액이 올바르지 않습니다.");
                return result;
            }

            if (request.getRoomNo() == null || request.getEstimateNo() == null) {
                result.put("success", false);
                result.put("message", "채팅방 번호 또는 견적 번호가 없습니다.");
                return result;
            }

            // merchant_uid 생성
            String merchantUid = "ORDER_" + request.getUserNo() + "_" + System.currentTimeMillis();
            request.setMerchantUid(merchantUid);
            
            String token = getPortoneToken();

            // 포트원 API 호출
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("merchant_uid", merchantUid);
            body.put("amount", request.getAmount());

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(API_URL + "/payments/prepare", entity, Map.class);

            // DB에 결제 준비 정보 저장 (READY 상태)
            paymentMapper.insertPreparePayment(request);

            log.info("결제 사전 등록 성공 - userNo: {}, merchantUid: {}, roomNo: {}, estimateNo: {}", 
                    request.getUserNo(), merchantUid, request.getRoomNo(), request.getEstimateNo());

            result.put("success", true);
            result.put("merchantUid", merchantUid);
        } catch (Exception e) {
            log.error("결제 준비 실패 - userNo: {}", request.getUserNo(), e);
            result.put("success", false);
            result.put("message", "결제 준비 중 오류가 발생했습니다.");
        }
        
        return result;
    }

    /**
     * 결제 검증
     */
    @Override
    public Map<String, Object> verifyPayment(PaymentVerifyRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 입력값 검증
            if (request.getImpUid() == null || request.getImpUid().isEmpty()) {
                result.put("success", false);
                result.put("message", "결제 고유번호가 없습니다.");
                return result;
            }

            if (request.getMerchantUid() == null || request.getMerchantUid().isEmpty()) {
                result.put("success", false);
                result.put("message", "주문번호가 없습니다.");
                return result;
            }

            String token = getPortoneToken();
            
            // 포트원에서 결제 정보 조회
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                API_URL + "/payments/" + request.getImpUid(),
                HttpMethod.GET, entity, Map.class);

            Map<String, Object> data = (Map<String, Object>) response.getBody().get("response");
            String status = (String) data.get("status");
            Integer amount = (Integer) data.get("amount");
            String merchantUid = (String) data.get("merchant_uid");

            // merchant_uid 일치 검증
            if (!merchantUid.equals(request.getMerchantUid())) {
                log.error("주문번호 불일치 - 요청: {}, 응답: {}", 
                        request.getMerchantUid(), merchantUid);
                result.put("success", false);
                result.put("message", "주문번호가 일치하지 않습니다.");
                return result;
            }

            // DB에서 사전 등록된 결제 정보 조회
            PaymentVO savedPayment = paymentMapper.selectPaymentByMerchantUid(request);
            
            if (savedPayment == null) {
                result.put("success", false);
                result.put("message", "등록되지 않은 결제입니다.");
                return result;
            }

            // 금액 검증
            if (!savedPayment.getAmount().equals(amount)) {
                log.error("결제 금액 불일치 - DB: {}, 포트원: {}", 
                        savedPayment.getAmount(), amount);
                result.put("success", false);
                result.put("message", "결제 금액이 일치하지 않습니다.");
                return result;
            }

            // 결제 상태 확인
            if ("paid".equals(status)) {
                // DTO에 값 설정
                request.setAmount(amount);
                request.setStatus("PAID");
                
                // DB 업데이트 (READY → PAID)
                paymentMapper.updatePaymentStatus(request);

                log.info("결제 완료 - userNo: {}, impUid: {}, amount: {}, roomNo: {}, estimateNo: {}", 
                        request.getUserNo(), request.getImpUid(), amount, 
                        savedPayment.getRoomNo(), savedPayment.getEstimateNo());
                
                result.put("success", true);
                result.put("impUid", request.getImpUid());
                result.put("merchantUid", merchantUid);
                result.put("amount", amount);
                result.put("roomNo", savedPayment.getRoomNo());
                result.put("estimateNo", savedPayment.getEstimateNo());
                result.put("message", "결제가 완료되었습니다.");
            } else {
                log.warn("결제 미완료 - status: {}", status);
                result.put("success", false);
                result.put("message", "결제가 완료되지 않았습니다.");
            }
        } catch (Exception e) {
            log.error("결제 검증 실패 - userNo: {}", request.getUserNo(), e);
            result.put("success", false);
            result.put("message", "결제 검증 중 오류가 발생했습니다.");
        }
        
        return result;
    }

    /**
     * 결제 취소
     */
    @Override
    public Map<String, Object> cancelPayment(PaymentCancelRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 입력값 검증
            if (request.getImpUid() == null || request.getImpUid().isEmpty()) {
                result.put("success", false);
                result.put("message", "결제 고유번호가 없습니다.");
                return result;
            }

            // DB에서 결제 정보 조회
            PaymentVO payment = paymentMapper.selectPaymentByImpUid(request);
            
            if (payment == null) {
                result.put("success", false);
                result.put("message", "결제 정보를 찾을 수 없습니다.");
                return result;
            }

            // 결제 상태 확인 (PAID만 취소 가능)
            if (!"PAID".equals(payment.getStatus())) {
                result.put("success", false);
                result.put("message", "취소할 수 없는 결제 상태입니다.");
                return result;
            }

            String token = getPortoneToken();
            
            // 포트원 API 호출
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = new HashMap<>();
            body.put("imp_uid", request.getImpUid());
            if (request.getReason() != null && !request.getReason().isEmpty()) {
                body.put("reason", request.getReason());
            }

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(API_URL + "/payments/cancel", entity, Map.class);

            // DB 업데이트 (PAID → FAILED)
            request.setStatus("FAILED");
            paymentMapper.updateCancelStatus(request);

            log.info("결제 취소 - userNo: {}, impUid: {}, roomNo: {}, estimateNo: {}", 
                    request.getUserNo(), request.getImpUid(), 
                    payment.getRoomNo(), payment.getEstimateNo());
            
            result.put("success", true);
            result.put("message", "결제가 취소되었습니다.");
        } catch (Exception e) {
            log.error("결제 취소 실패 - userNo: {}", request.getUserNo(), e);
            result.put("success", false);
            result.put("message", "결제 취소 중 오류가 발생했습니다.");
        }
        
        return result;
    }

    /**
     * 결제 상세 조회
     */
    @Override
    public Map<String, Object> getPaymentDetail(PaymentDetailRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 입력값 검증
            if (request.getImpUid() == null || request.getImpUid().isEmpty()) {
                result.put("success", false);
                result.put("message", "결제 고유번호가 없습니다.");
                return result;
            }

            // DB에서 결제 정보 조회
            PaymentVO payment = paymentMapper.selectPaymentDetail(request);
            
            if (payment == null) {
                result.put("success", false);
                result.put("message", "결제 정보를 찾을 수 없습니다.");
                return result;
            }

            log.info("결제 상세 조회 - userNo: {}, impUid: {}", 
                    request.getUserNo(), request.getImpUid());
            
            result.put("success", true);
            result.put("payment", payment);
            
        } catch (Exception e) {
            log.error("결제 상세 조회 실패 - userNo: {}", request.getUserNo(), e);
            result.put("success", false);
            result.put("message", "결제 정보 조회 중 오류가 발생했습니다.");
        }
        
        return result;
    }
}