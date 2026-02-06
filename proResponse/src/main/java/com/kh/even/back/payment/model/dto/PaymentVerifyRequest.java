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
    private Long userNo;
    private String impUid;
    private String merchantUid;
    private Integer amount;
    private String status;
}
