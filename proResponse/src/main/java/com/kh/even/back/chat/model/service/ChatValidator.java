
package com.kh.even.back.chat.model.service;

import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.chat.model.dto.ChatRoomDTO;
import com.kh.even.back.exception.ChatException;

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
     * 파일 유효성 검사
     */
    public static void validateFile(MultipartFile file) {
        // 1. 파일이 비어있는지 체크
        if (file == null || file.isEmpty()) {
            return; // 파일이 필수 옵션이 아니라면 그냥 리턴, 필수라면 예외 발생
        }

        String filename = file.getOriginalFilename();
        
        // 2. 파일명 존재 여부
        if (filename == null || filename.trim().isEmpty()) {
            throw new ChatException("파일명이 올바르지 않습니다.");
        }
        
        // 3. 확장자 추출 및 검사
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex == -1) {
            throw new ChatException("확장자가 없는 파일입니다.");
        }
        
        // String extension = filename.substring(dotIndex + 1).toLowerCase();

        // if (!ALLOWED_EXTENSIONS.contains(extension)) {
        //     throw new ChatException("허용되지 않는 파일 형식입니다. (허용: " + ALLOWED_EXTENSIONS + ")");
        // }
    }


}
