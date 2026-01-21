package com.kh.even.back.chat.model.dto;

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
public class ChatRoomSearchDTO  {

    // 공통 키
    private Long roomNo;          // 채팅방 번호
    private Long userNo;          // 요청 사용자 (권한 검증용)

    // 메시지 조회용
    private Long lastMessageNo;   // 페이징 기준
    private Integer size;         // 조회 개수
}
