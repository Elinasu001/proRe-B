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
public class PaymentPrepareRequest {
    private Long paymentNo;
    private Integer amount;         // 결제 금액
    private String itemName;        // 상품명
    private String merchantUid;     // 주문번호 (프론트 생성)
    private Long estimateNo;     // 견적 번호
}

