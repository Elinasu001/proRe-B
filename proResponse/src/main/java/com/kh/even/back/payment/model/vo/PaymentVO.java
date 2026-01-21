package com.kh.even.back.payment.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PaymentVO {
	private Long paymentNo;				// 결제 번호 (PK)
	private Integer amount;				// 결제 금액
	private String status;  			// READY, PAID, FAILED, DONE
	private String merchantUid;			// 주문 번호
	private String impUid;				// 결제 고유 번호
	private Date createDate;	// 결제 생성 일자
	private Long roomNo;				// 채팅방 번호 (FK)
	private Long estimateNo;			// 견적 번호 (FK)
}
