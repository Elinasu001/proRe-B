package com.kh.even.back.chat.model.dto;


import java.time.LocalDateTime;
import java.util.List;

import com.kh.even.back.report.model.dto.ReportDetailDTO;
import com.kh.even.back.review.model.dto.ReviewDetailDTO;

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
public class ChatDetailDTO {
    private Long roomNo;				// 채팅방 번호 (PK)
    private String status;				// 상태
    private LocalDateTime createdDate;	// 생성 일자
    private Long estimateNo;			// 견적 번호 (FK)


    private ChatUserDTO otherUser;				// 상대방 정보
    private List<ChatRoomUserDTO> participants; // 채팅방 참여자 목록
    private List<ChatMessageDTO> messages;      // 채팅 메시지 목록
    private ReviewDetailDTO reviewDetail;       // 연관된 리뷰 상세 정보
    private ReportDetailDTO reportDetail;       // 연관된 신고 상세 정보

}   
