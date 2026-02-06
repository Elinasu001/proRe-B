package com.kh.even.back.payment.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PortOneApiClient {

    @Value("${portone.v2.api-secret}")
    private String apiSecret;

    @Value("${portone.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    /**
     * V2: 결제 정보 조회
     * @param paymentId merchantUid (V2에서는 paymentId)
     * @return 결제 정보
     */
    public Map<String, Object> getPaymentInfo(String paymentId) {
        HttpHeaders headers = createHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                apiUrl + "/payments/" + paymentId,
                HttpMethod.GET, 
                entity, 
                Map.class
            );

            log.info("[포트원 V2] 결제 조회 성공 - paymentId: {}", paymentId);
            return response.getBody();
            
        } catch (Exception e) {
            log.error("[포트원 V2] 결제 조회 실패 - paymentId: {}", paymentId, e);
            throw new RuntimeException("결제 정보 조회 실패", e);
        }
    }

    /**
     * V2: 결제 취소
     * @param paymentId merchantUid (V2에서는 paymentId)
     * @param reason 취소 사유
     * @return 취소 결과
     */
    public Map<String, Object> cancelPayment(String paymentId, String reason) {
        HttpHeaders headers = createHeaders();

        Map<String, Object> body = new HashMap<>();
        body.put("reason", reason);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                apiUrl + "/payments/" + paymentId + "/cancel",
                entity,
                Map.class
            );

            log.info("[포트원 V2] 결제 취소 성공 - paymentId: {}", paymentId);
            return response.getBody();
            
        } catch (Exception e) {
            log.error("[포트원 V2] 결제 취소 실패 - paymentId: {}", paymentId, e);
            throw new RuntimeException("결제 취소 실패", e);
        }
    }

    /**
     * V2 공통 헤더 생성
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "PortOne " + apiSecret);
        return headers;
    }
}