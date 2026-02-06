package com.kh.even.back.payment.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class PreparePaymentRequest {

    private Long paymentNo;

    @NotBlank(message = "주문번호는 필수입니다")
    private String merchantUid;

    @NotNull(message = "견적 번호는 필수입니다")
    private Long estimateNo;

    @NotNull(message = "채팅방 번호는 필수입니다")
    private Long roomNo;

    @NotNull(message = "결제 금액은 필수입니다")
    @Positive(message = "결제 금액은 0보다 커야 합니다")
    private Integer amount;

}
