package com.kh.even.back.admin.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 신고 관련 채팅 내역 조회 DTO
 */
@Getter
@Setter
@NoArgsConstructor  // 추가!
@AllArgsConstructor // 추가!
@Builder
public class AdminReportChatContext {
    private Long reportNo;      // 신고 번호
    private Long estimateNo;    // 견적서 번호
    private Long roomNo;        // 채팅방 번호
    private List<ChatMessageInfo> messages;  // 메시지 목록
    
    /**
     * 채팅 메시지 정보
     */
    @Getter
    @Setter
    @Builder
    public static class ChatMessageInfo {
        private Long messageNo;     // 메시지 번호
        private Long userNo;        // 작성자 번호
        private String nickname;    // 작성자 닉네임
        private String content;     // 메시지 내용
        private String sentDate;    // 전송 일시
        private String type;        // 메시지 타입 (TEXT, FILE 등)
    }
}