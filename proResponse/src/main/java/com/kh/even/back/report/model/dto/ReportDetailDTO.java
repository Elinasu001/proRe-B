package com.kh.even.back.report.model.dto;

import java.sql.Date;
import java.util.List;

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
public class ReportDetailDTO {
    private Long reportNo;            // 신고번호
    private String content;           // 신고내용
    private Date createDate;          // 신고 생성 일자(회원 신고)
    private Date updateDate;          // 신고 처리 일자(관리자) WAITING/RESOLVED/REJECTED (대기중/처리완료/신고거절)
    private String status;            // 신고 상태 WAITING/RESOLVED/REJECTED (대기중/처리완료/신고거절)
    private Integer reasonNo;         // 신고 사유 번호 (FK)
    private Long estimateNo;          // 신고된 견적서 번호 (FK)
    private Long reporterUserNo;      // 신고자 (FK)
    private Long targetUserNo;        // 신고대상 (FK)

    // 선택된 태그 목록
    private List<ReportTagDTO> selectedTags;

}
