package com.kh.even.back.payment.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.even.back.exception.PaymentException;
import com.kh.even.back.payment.model.dao.PaymentMapper;
import com.kh.even.back.payment.model.dto.PaymentCancelRequest;
import com.kh.even.back.payment.model.dto.PaymentPrepareRequest;
import com.kh.even.back.payment.model.dto.PaymentVerifyRequest;
import com.kh.even.back.payment.model.vo.PaymentVO;
import com.kh.even.back.payment.util.PortOneApiClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PortOneApiClient portOneClient;
    private final PaymentMapper paymentMapper;

    /**
     * 결제 사전 등록
     */
    @Override
    @Transactional
    public Map<String, Object> preparePayment(PaymentPrepareRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        try {

            // READY 중복 결제 방어
            PaymentVO ready = paymentMapper.selectReadyPayment(request.getEstimateNo());

            if (ready != null) {
                throw new PaymentException("이미 진행중인 결제가 있습니다.");
            }

            // 포트원에 준비
            portOneClient.prepare(request.getMerchantUid(), request.getAmount());

            // DB 저장
            paymentMapper.insertPreparePayment(request);
            
            result.put("success", true);
            result.put("merchantUid", request.getMerchantUid());
            
            log.info("결제 준비 완료 - merchantUid: {}, estimateNo: {}", 
                    request.getMerchantUid(), request.getEstimateNo());
            
        } catch (Exception e) {
            throw new PaymentException("결제 준비 실패", e);
        }
        
        return result;
    }

    /**
     * 결제 검증
     */
    @Override
    @Transactional
    public Map<String, Object> verifyPayment(PaymentVerifyRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 포트원에서 결제 정보 조회
            Map<String, Object> paymentInfo = portOneClient.getPaymentInfo(request.getImpUid());
            
            String status = (String) paymentInfo.get("status");
            Integer amount = (Integer) paymentInfo.get("amount");
            String merchantUid = (String) paymentInfo.get("merchant_uid");

            // 주문번호 검증
            if (!merchantUid.equals(request.getMerchantUid())) {
                log.error("주문번호 불일치 - 요청: {}, 포트원: {}", 
                        request.getMerchantUid(), merchantUid);
                result.put("success", false);
                result.put("message", "주문번호가 일치하지 않습니다.");
                return result;
            }

            // DB에서 사전 등록 정보 조회
            PaymentVO savedPayment = paymentMapper.selectPaymentByMerchantUid(request);
            if (savedPayment == null) {
                result.put("success", false);
                result.put("message", "등록되지 않은 결제입니다.");
                return result;
            }

            // 금액 검증
            if (!savedPayment.getAmount().equals(amount)) {
                log.error("금액 불일치 - DB: {}, 포트원: {}", savedPayment.getAmount(), amount);
                result.put("success", false);
                result.put("message", "결제 금액이 일치하지 않습니다.");
                return result;
            }

            // 결제 완료 처리
            if ("paid".equals(status)) {
                request.setAmount(amount);
                request.setStatus("PAID");
                paymentMapper.updatePaymentStatus(request);

                result.put("success", true);
                result.put("impUid", request.getImpUid());
                result.put("merchantUid", merchantUid);
                result.put("amount", amount);
                result.put("estimateNo", savedPayment.getEstimateNo());
                
                log.info("결제 완료 - impUid: {}, amount: {}, estimateNo: {}", 
                        request.getImpUid(), amount, savedPayment.getEstimateNo());
            } else {
                result.put("success", false);
                result.put("message", "결제가 완료되지 않았습니다.");
            }
            
        } catch (Exception e) {
            throw new PaymentException("결제 검증 실패", e);
        }
        
        return result;
    }

    /**
     * 결제 취소
     */
    @Override
    @Transactional
    public Map<String, Object> cancelPayment(PaymentCancelRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // DB에서 결제 정보 조회
            PaymentVO payment = paymentMapper.selectPaymentByImpUid(request);
            
            if (payment == null) {
                result.put("success", false);
                result.put("message", "결제 정보를 찾을 수 없습니다.");
                return result;
            }
            
            if (!"PAID".equals(payment.getStatus())) {
                result.put("success", false);
                result.put("message", "취소할 수 없는 결제 상태입니다.");
                return result;
            }

            // 포트원 취소 요청
            portOneClient.cancel(request.getImpUid(), request.getReason());
            
            // DB 상태 업데이트 (PAID > FAILED)
            request.setStatus("FAILED");
            paymentMapper.updateCancelStatus(request);
            
            result.put("success", true);
            result.put("message", "결제가 취소되었습니다.");
            
            log.info("결제 취소 완료 - impUid: {}", request.getImpUid());
            
        } catch (Exception e) {
            throw new PaymentException("결제 취소 실패", e);
        }
        
        return result;
    }
}