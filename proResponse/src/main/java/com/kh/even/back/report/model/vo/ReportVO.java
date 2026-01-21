package com.kh.even.back.report.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReportVO {
    private Long reportNo;            // 신고번호
    private String content;           // 신고내용
    private Date createDate;          // 신고 생성 일자(회원 신고)
    private Date updateDate;          // 신고 처리 일자(관리자) WAITING/RESOLVED/REJECTED (대기중/처리완료/신고거절)
    private String status;            // 신고 상태 WAITING/RESOLVED/REJECTED (대기중/처리완료/신고거절)
    private Integer reasonNo;         // 신고 사유 번호 (FK)
    private Long estimateNo;          // 신고된 견적서 번호 (FK)
    private Long reportUserNo;        // 신고자 (FK)
    private Long targetUserNo;        // 신고대상 (FK)
}
