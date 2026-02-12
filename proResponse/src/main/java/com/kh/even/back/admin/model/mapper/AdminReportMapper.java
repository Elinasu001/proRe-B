package com.kh.even.back.admin.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.admin.model.dto.AdminReportChatContext;

@Mapper
public interface AdminReportMapper {
    
    /**
     * 신고 관련 채팅 내역 조회
     */
    AdminReportChatContext selectReportChatContext(Long reportNo);
}