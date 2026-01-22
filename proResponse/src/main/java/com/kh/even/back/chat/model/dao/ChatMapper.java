package com.kh.even.back.chat.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.chat.model.dto.ChatMessageDTO;
import com.kh.even.back.chat.model.vo.ChatAttachmentVO;
import com.kh.even.back.chat.model.vo.ChatMessageVO;
import com.kh.even.back.chat.model.vo.ChatRoomUserVO;
import com.kh.even.back.chat.model.vo.ChatRoomVO;

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
	int createRoom(ChatRoomVO roomVO);

    /**
     * 채팅방 사용자 저장
     */
	int createRoomUser(ChatRoomUserVO roomUserVO);

    /**
     * 메시지 저장
     */
	int saveMessage(ChatMessageVO messageVO);

    /**
     * 첨부파일 저장
     */
	int saveChatAttachment(ChatAttachmentVO attachmentVO);



    /**
     * 커서 기반 메시지 목록 조회
     */
    List<ChatMessageDTO> getMessagesByCursor(Map<String, Object> params);

}
