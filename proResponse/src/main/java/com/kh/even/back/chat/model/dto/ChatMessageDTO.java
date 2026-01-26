package com.kh.even.back.chat.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ChatMessageDTO {

    private Long messageNo;				// 메시지 번호 (PK)
    private String content;				// 메시지 내용
    private LocalDateTime sentDate;		// 보낸 시간
    private String status;				// 확인 여부
    private Long userNo;				// 회원 번호 (FK)
    private Long roomNo;				// 채팅방 번호 (FK)
    private String type;                // 메시지 타입 (TEXT, FILE, PAYMENT, SYSTEM 등)
    private String nickname;            // 작성자 닉네임
    private Long estimateNo;            // 견적 번호 (FK)

    private boolean isMine;             // 내 메시지인지 (UI용)
    //private boolean read;             // 읽음 상태 (UI용)

    private List<MultipartFile> files;             // 첨부파일 (FILE 타입 메시지용), 업로드용
    private List<ChatAttachmentDTO> attachments;   // 첨부파일 정보 (FILE 타입 메시지용)

    private ChatRoomActionsDTO actions;          // 채팅방 액션 목록(신고, 리뷰, 결제 상태)
    private String userRole;                     // 전문가/일반회원 구분
}
