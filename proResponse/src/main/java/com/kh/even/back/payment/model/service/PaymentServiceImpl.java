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
    // private final EstimateRepository estimateRepository;
    // private final ExpertEstimateRepository expertEstimateRepository;

    /**
     * 결제 사전 등록 (V2)
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

            // V2에서는 사전등록 API 없음, DB에만 저장
            paymentMapper.insertPreparePayment(request);
            
            result.put("success", true);
            result.put("merchantUid", request.getMerchantUid());
            
            // log.info("[V2 결제 준비 완료] merchantUid: {}, estimateNo: {}", 
            //         request.getMerchantUid(), request.getEstimateNo());
            
        } catch (Exception e) {
            //log.error("[V2 결제 준비 실패]", e);
            throw new PaymentException("결제 준비 실패", e);
        }
        
        return result;
    }

    /**
     * 결제 검증 (V2)
     */
    @Override
    @Transactional
    public Map<String, Object> verifyPayment(PaymentVerifyRequest request) {
        Map<String, Object> result = new HashMap<>();  
        try {
            // 포트원 V2에서 결제 정보 조회 (merchantUid 사용)
            Map<String, Object> paymentInfo = portOneClient.getPaymentInfo(request.getMerchantUid());
            
            // V2 응답 구조 파싱
            String status = (String) paymentInfo.get("status");
            Map<String, Object> amountObj = (Map<String, Object>) paymentInfo.get("amount");
            Integer totalAmount = (Integer) amountObj.get("total");
            String txId = (String) paymentInfo.get("id");  // V2 txId

            // log.info("[V2 결제 정보 조회] status: {}, amount: {}, txId: {}", 
            //         status, totalAmount, txId);


            // DB에서 사전 등록 정보 조회 (merchantUid 기준)
            PaymentVO savedPayment = paymentMapper.selectPaymentByMerchantUid(request.getMerchantUid());
            if (savedPayment == null) {
                result.put("success", false);
                result.put("message", "등록되지 않은 결제입니다.");
                return result;
            }

            // 이미 결제 완료/취소/만료된 건이면 중복 처리 방지
            if ("PAID".equals(savedPayment.getStatus()) || "CANCELLED".equals(savedPayment.getStatus()) || "EXPIRED".equals(savedPayment.getStatus())) {
                result.put("success", false);
                result.put("message", "이미 처리된 결제입니다. 상태: " + savedPayment.getStatus());
                return result;
            }

            // 금액 검증
            if (!savedPayment.getAmount().equals(totalAmount)) {
                result.put("success", false);
                result.put("message", "결제 금액이 일치하지 않습니다.");
                return result;
            }

            // 결제 완료 처리
            if ("PAID".equals(status)) {
                request.setImpUid(txId);  // V2 txId를 impUid로 저장
                request.setAmount(totalAmount);
                request.setStatus("PAID");
                log.info("[결제상태업데이트] merchantUid: {}, status: {}", request.getMerchantUid(), request.getStatus());
                int updateCount = paymentMapper.updatePaymentStatus(request);
                log.info("[결제상태업데이트] updatePaymentStatus 반환값: {}", updateCount);

                // 결제 완료 시 견적 상태 업데이트
                int respUpdate = updateEstimateStatus("RESPONSE", savedPayment.getEstimateNo(), "EXPIRED");
                log.info("[견적상태업데이트] RESPONSE, estimateNo: {}, status: DONE, updateCount: {}", savedPayment.getEstimateNo(), respUpdate);
                int reqUpdate = updateEstimateStatus("REQUEST", savedPayment.getEstimateNo(), "DONE");
                log.info("[견적상태업데이트] REQUEST, estimateNo: {}, status: EXPIRED, updateCount: {}", savedPayment.getEstimateNo(), reqUpdate);

                result.put("success", true);
                result.put("merchantUid", request.getMerchantUid());
                result.put("impUid", txId);
                result.put("amount", totalAmount);
                result.put("estimateNo", savedPayment.getEstimateNo());
            } else {
                result.put("success", false);
                result.put("message", "결제가 완료되지 않았습니다. 상태: " + status);
            }
            
        } catch (Exception e) {
            //log.error("[V2 결제 검증 실패]", e);
            throw new PaymentException("결제 검증 실패", e);
        }
        
        return result;
    }

    /**
     * 견적 상태 업데이트 (테이블 타입에 따라 분기)
     */
    public int updateEstimateStatus(String type, Long estimateNo, String status) {
        Map<String, Object> param = new HashMap<>();
        param.put("estimateNo", estimateNo);
        param.put("status", status); // 상태값도 함께 전달
        if ("RESPONSE".equalsIgnoreCase(type)) {
            return paymentMapper.updateEstimateResponseStatus(param);
        } else if ("REQUEST".equalsIgnoreCase(type)) {
            return paymentMapper.updateEstimateRequestStatus(param);
        }
        throw new IllegalArgumentException("Unknown type: " + type);
    }

    /**
     * 결제 취소 (V2)
     */
    @Override
    @Transactional
    public Map<String, Object> cancelPayment(PaymentCancelRequest request) {
        Map<String, Object> result = new HashMap<>();  
        
        try {
            // DB에서 결제 정보 조회 (merchantUid 기준)
            PaymentVO payment = paymentMapper.selectPaymentByMerchantUid(request.getMerchantUid());
            
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

            // 포트원 V2 취소 요청 (merchantUid 사용)
            portOneClient.cancelPayment(request.getMerchantUid(), request.getReason());
            
            // DB 상태 업데이트 (PAID → CANCELLED)
            request.setStatus("CANCELLED");
            paymentMapper.updateCancelStatus(request);
            
            result.put("success", true);
            result.put("message", "결제가 취소되었습니다.");
            
            //log.info("[V2 결제 취소 완료] merchantUid: {}, paymentNo: {}",
                    //request.getMerchantUid(), payment.getPaymentNo());
            
        } catch (Exception e) {
            //log.error("[V2 결제 취소 실패]", e);
            throw new PaymentException("결제 취소 실패", e);
        }
        
        return result;
    }
}