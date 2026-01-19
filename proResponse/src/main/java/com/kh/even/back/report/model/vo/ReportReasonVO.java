package com.kh.even.back.report.model.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReportReasonVO {
    private Integer reasonNo;       // 신고 사유 번호
    private String reasonName;      // 신고 사유명 (예: 욕설·비방 등 부적절한 내용, 허위 정보 또는 과장된 내용, 사기 및 금전 요구, 거래/서비스 문제, 도용 또는 저작권 침해, 스팸·광고, 기타)
}
