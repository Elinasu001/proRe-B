package com.kh.even.back.payment.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentCancelRequest {
    private String merchantUid;      // 포트원 결제 고유번호
    private String reason;          // 취소 사유
    private String status;          // 결제 상태 (FAILED)
}