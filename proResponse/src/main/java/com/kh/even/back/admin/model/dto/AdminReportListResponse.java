package com.kh.even.back.admin.model.dto;

import java.util.List;

import com.kh.even.back.util.PageInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 관리자 신고 목록 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AdminReportListResponse {
    
    private List<AdminReportDTO> reportList;
    private PageInfo pageInfo;
}