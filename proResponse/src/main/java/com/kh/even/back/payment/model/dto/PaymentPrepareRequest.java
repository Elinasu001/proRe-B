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
    private Long userNo;
    private Integer amount;
    private String itemName;
    private Long roomNo;        // 채팅방 번호
    private Long estimateNo;    // 견적 번호
    private String merchantUid; // Service에서 생성 후 설정
}

