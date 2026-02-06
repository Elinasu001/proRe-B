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
public class PaymentVerifyRequest {
    private String impUid;          // 포트원 결제 고유번호
    private String merchantUid;     // 주문번호
    private Integer amount;         // 결제 금액 (검증 후 저장용)
    private String status;          // 결제 상태 (PAID)
}
