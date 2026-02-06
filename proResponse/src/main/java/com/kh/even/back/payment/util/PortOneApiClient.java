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

    @Value("${imp.api.key}")
    private String apiKey;

    @Value("${imp.api.secret}")
    private String apiSecret;

    private final RestTemplate restTemplate;
    private static final String API_URL = "https://api.iamport.kr";
    
    

    /**
     * 포트원 토큰 발급
     */
    public String getToken() {
        Map<String, String> body = new HashMap<>();
        body.put("imp_key", apiKey);
        body.put("imp_secret", apiSecret);

        ResponseEntity<Map> response = restTemplate.postForEntity(
            API_URL + "/users/getToken", body, Map.class);
        
        Map<String, Object> data = (Map<String, Object>) response.getBody().get("response");
        return (String) data.get("access_token");
    }

    /**
     * 결제 사전 등록
     */
    public void prepare(String merchantUid, Integer amount) {

        String token = getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("merchant_uid", merchantUid);
        body.put("amount", amount);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers);

        try {

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(
                            API_URL + "/payments/prepare",
                            entity,
                            Map.class
                    );

            log.info("포트원 prepare 응답 = {}", response.getBody());

        } catch (Exception e) {

            log.error("포트원 prepare 실패 merchantUid={}, amount={}",
                    merchantUid, amount, e);

            throw new RuntimeException("결제 준비 실패", e);
        }
    }

    /**
     * 결제 정보 조회
     */
    public Map<String, Object> getPaymentInfo(String impUid) {
        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
            API_URL + "/payments/" + impUid,
            HttpMethod.GET, entity, Map.class);

        return (Map<String, Object>) response.getBody().get("response");
    }

    /**
     * 결제 취소
     */
    public void cancel(String impUid, String reason) {
        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("imp_uid", impUid);
        if (reason != null) body.put("reason", reason);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(API_URL + "/payments/cancel", entity, Map.class);
    }
}