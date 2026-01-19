package com.kh.even.back.payment.model.dto;


import java.time.LocalDateTime;

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
public class PaymentDetailDTO {
    private Long paymentNo;				// 결제 번호 (PK)
	private Integer amount;				// 결제 금액
	private String status;  			// READY, PAID, FAILED, DONE
	private String merchantUid;			// 주문 번호
	private String impUid;				// 결제 고유 번호
	private LocalDateTime createDate;	// 결제 생성 일자
	//private Long roomNo;				// 채팅방 번호 (FK)
	private Long estimateNo;			// 견적 번호 (FK)

	// 계산 필드
    private Boolean isPaid;       // 결제 완료 여부
    private Boolean canPay;       // 결제 가능 여부
    private String message;       // 안내 메시지
}
