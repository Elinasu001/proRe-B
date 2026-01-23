
package com.kh.even.back.chat.model.service;

import com.kh.even.back.chat.model.dto.ChatMessageDTO;
import com.kh.even.back.exception.ChatException;


public final class ChatValidator {

    private ChatValidator() {}

    public static void validateCreatable(String requestStatus, String responseStatus) {
        if (!"MATCHED".equals(requestStatus) || !"ACCEPTED".equals(responseStatus)) {
            throw new ChatException("채팅방 생성 조건이 충족되지 않았습니다.");
        }
    }

    public static void validateGetMessagesParams(Long roomNo, int size) {
        if (roomNo == null) {
            throw new ChatException("채팅방 번호가 없습니다.");
        }
        if (size <= 0) {
            throw new ChatException("조회할 메시지 개수가 올바르지 않습니다.");
        }
    }

    public static void validateByType(ChatMessageDTO dto) {
        if ("FILE".equals(dto.getType())) {
            if (dto.getFiles() == null || dto.getFiles().isEmpty()) {
                throw new ChatException("파일 타입 메시지는 첨부파일이 필수입니다.");
            }
        }
    }

    public static void notNull(Object value, String message) {
    if (value == null) {
        throw new ChatException(message);
    }
}
}

