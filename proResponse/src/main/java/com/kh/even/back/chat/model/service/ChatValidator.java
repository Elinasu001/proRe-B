
package com.kh.even.back.chat.model.service;

import org.springframework.stereotype.Component;

import com.kh.even.back.chat.model.dao.ChatMapper;
import com.kh.even.back.chat.model.dto.ChatMessageDTO;
import com.kh.even.back.exception.ChatException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class ChatValidator {

    private final ChatMapper chatMapper;


     /**
     *  견적 상태 검증
     */
    public void validateEstimateStatus(Long estimateNo) {
        String requestStatus = chatMapper.getRequestStatusByEstimateNo(estimateNo);
        String responseStatus = chatMapper.getResponseStatusByEstimateNo(estimateNo);
       // 예: 둘 다 "ACCEPTED" 또는 "MATCHED"여야만 통과
        if (!"ACCEPTED".equals(requestStatus) || !"MATCHED".equals(responseStatus)) {
            throw new ChatException("견적 상태가 채팅방 입장 조건을 만족하지 않습니다.");
        }
    }


    /**
     * 메시지 저장 및 검증
     */
    public void validateDbResult(int result, String errorMessage) {
        if (result != 1) {
            throw new ChatException(errorMessage);
        }
    }


    public static void validateCreatable(String requestStatus, String responseStatus) {
        if (!"MATCHED".equals(requestStatus) || !"ACCEPTED".equals(responseStatus)) {
            throw new ChatException("채팅방 생성 조건이 충족되지 않았습니다.");
        }
    }

    public static void validateGetMessagesParams(Long roomNo/*, int size */) {
        if (roomNo == null) {
            throw new ChatException("채팅방 번호가 없습니다.");
        }
        // if (size <= 0) {
        //     throw new ChatException("조회할 메시지 개수가 올바르지 않습니다.");
        // }
    }

    public static void validateByType(ChatMessageDTO dto) {
        boolean hasFiles = dto.getFiles() != null && !dto.getFiles().isEmpty();
        boolean hasAttachments = dto.getAttachments() != null && !dto.getAttachments().isEmpty();
        if (!hasFiles && !hasAttachments) {
            throw new ChatException("파일 타입 메시지는 첨부파일이 필수입니다.");
        }
    }


    public static void notNull(Object value, String message) {
    if (value == null) {
        throw new ChatException(message);
    }
}
}

