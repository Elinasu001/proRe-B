package com.kh.even.back.admin.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 관리자 정보 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminVO {
    
    private Long adminNo;       // 관리자 번호
    private Long userNo;        // 회원 번호 (FK)
    private String adminLevel;  // 관리자 레벨 (ROOT, MANAGER)
    private String adminMemo;   // 관리자 메모
    private Date createDate;    // 등록일
}