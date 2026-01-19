package com.kh.even.back.report.model.dto;

import java.sql.Date;
import java.time.LocalDateTime;
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
    private String reportContent;     // 신고내용
    private Date updateDate;          // 신고 처리 일자(관리자)
    private LocalDateTime createDate; // 신고 생성 일자(회원 신고)
    private String status;            // 신고 상태 WAITING/RESOLVED/REJECTED (대기중/처리완료/ 신고 거절)
    private Long userNo;              // 신고한 회원 번호
    private Long roomNo;              // 신고된 채팅방 번호
    private Integer reasonNo;         // 신고 사유 번호

    private List<ReportReasonDTO> reportReasons; // 신고 사유 목록
}
