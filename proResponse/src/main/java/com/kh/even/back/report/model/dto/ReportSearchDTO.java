package com.kh.even.back.report.model.dto;

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
public class ReportSearchDTO {
    private Long roomNo;     // 채팅방 번호
    private Long userNo;        // 신고한 사용자 번호
}
