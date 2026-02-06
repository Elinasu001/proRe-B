package com.kh.even.back.payment.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// PaymentPrepareRequest.java
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentPrepareRequest {
    private Long paymentNo;
    private String merchantUid;     // V2에서는 paymentId 역할
    private Integer amount;
    private String itemName;
    private Long estimateNo;
    //private Long roomNo;            // 추가 (ERD에 있음)
}
