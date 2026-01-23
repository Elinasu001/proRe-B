package com.kh.even.back.chat.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.chat.model.dto.ChatAttachmentDTO;
import com.kh.even.back.chat.model.dto.ChatMessageDTO;
import com.kh.even.back.chat.model.vo.ChatMessageVO;
import com.kh.even.back.chat.model.vo.ChatRoomUserVO;
import com.kh.even.back.chat.model.vo.ChatRoomVO;
import com.kh.even.back.file.model.vo.FileVO;

@Mapper
public interface ChatMapper {

    /**
     * 견적 상태 조회
     */
	String getRequestStatusByEstimateNo(Long estimateNo);

    /**
     * 견적 상태 조회
     */
	String getResponseStatusByEstimateNo(Long estimateNo);

    /**
     * 채팅방 생성
     */
	int createRoom(ChatRoomVO roomVo);

    /**
     * 채팅방 사용자 저장
     */
	int createRoomUser(ChatRoomUserVO roomUserVo);

    /**
     * 메시지 저장
     */
	int saveMessage(ChatMessageVO messageVo);

    /**
     * 첨부파일 저장
     */
	int saveChatAttachment(FileVO fileVo);

    /**
     * 회원 번호로 닉네임 조회
     */
    String getNicknameByUserNo(Long userNo);

    /**
     * 견적 번호로 채팅방 번호 조회
     */
    Long getRoomNoByEstimateNo(Long estimateNo);

    /**
     * 커서 기반 메시지 목록 조회
     */
    List<ChatMessageDTO> getMessagesByCursor(Map<String, Object> params);

    /**
     * 여러 메시지의 첨부파일 일괄 조회
     */
    List<ChatAttachmentDTO> getAttachmentsByMessageNos(List<Long> messageNos);

}
