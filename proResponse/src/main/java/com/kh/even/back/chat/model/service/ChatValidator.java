
package com.kh.even.back.chat.model.service;

import org.springframework.stereotype.Component;

import com.kh.even.back.chat.model.dto.ChatRoomDTO;
import com.kh.even.back.exception.ChatException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ChatValidator {

    /**
     * Validator는 주로 "입력값의 유효성(파라미터, DTO 등)"을 사전에 검증하는 역할
     *  DB 작업 결과(예: update, insert 결과값 등)에 대한 검증은 서비스 로직에서 바로 처리하는 것이 일반적
     */

    // private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "jfif");

    
    /*
        캠페인 DTO 유효성 검사
    */
    public void validateChatDetailDTO(ChatRoomDTO dto) {
        if(dto == null) {
            throw new ChatException("채팅 정보가 없습니다.");
        }
    }

    /**
     * DB 작업 결과 유효성 검사 (insert, update 등)
     */
    public static void validateDbResult(int result, String errorMessage) {
        if (result != 1) {
            throw new ChatException(errorMessage);
        }
    }

    /**
     * getMessages 파라미터 유효성 검사
     */
    public static void validateGetMessagesParams(Long roomNo, int size) {
        if (roomNo == null) {
            throw new ChatException("채팅방 번호가 없습니다.");
        }
        if (size <= 0) {
            throw new ChatException("조회할 메시지 개수가 올바르지 않습니다.");
        }
    }
}
