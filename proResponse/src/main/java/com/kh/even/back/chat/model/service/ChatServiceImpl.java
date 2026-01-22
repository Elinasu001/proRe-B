
package com.kh.even.back.chat.model.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.chat.model.dao.ChatMapper;
import com.kh.even.back.chat.model.dto.ChatMessageDTO;
import com.kh.even.back.chat.model.dto.ChatRoomDTO;
import com.kh.even.back.chat.model.vo.ChatAttachmentVO;
import com.kh.even.back.chat.model.vo.ChatMessageVO;
import com.kh.even.back.chat.model.vo.ChatRoomUserVO;
import com.kh.even.back.chat.model.vo.ChatRoomVO;
import com.kh.even.back.exception.ChatException;
import com.kh.even.back.file.service.S3Service;
import com.kh.even.back.review.model.service.ReviewService;
import com.kh.even.back.util.CursorPagination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMapper chatMapper;
    private final ReviewService reviewService;
    private final S3Service s3Service;


    /**
     * 채팅방 생성 및 초기 메시지 저장
     */
    @Override
    @Transactional
    public ChatRoomVO createRoom(ChatRoomDTO chatRoomDto, ChatMessageDTO chatMessageDto, List<MultipartFile> files, Long userNo) {

        // 1. 견적 상태 체크
        validateEstimateStatus(chatRoomDto.getEstimateNo());

        // 2. 이미 채팅방이 있으면 반환, 없으면 생성
        boolean exists = reviewService.existsByEstimateNo(chatRoomDto.getEstimateNo());
        if (exists) {
            throw new ChatException("이미 채팅방이 존재합니다");
        }

        // 3. 채팅방 생성
        ChatRoomVO roomVo = ChatRoomVO.builder()
            .estimateNo(chatRoomDto.getEstimateNo())
            .status("Y")
            .createDate(LocalDateTime.now())
            .build();
        int roomResult = chatMapper.createRoom(roomVo);
        ChatValidator.validateDbResult(roomResult, "채팅방 생성에 실패했습니다.");

        // 4. 생성자 등록
        ChatRoomUserVO roomUserVo = ChatRoomUserVO.builder()
            .roomNo(roomVo.getRoomNo())
            .userNo(userNo)
            .build();
        int userResult = chatMapper.createRoomUser(roomUserVo);
        ChatValidator.validateDbResult(userResult, "채팅방 유저 등록에 실패했습니다.");

        // 5. 메시지 저장
        ChatMessageVO messageVo = ChatMessageVO.builder()
            .roomNo(roomVo.getRoomNo())
            .userNo(userNo)
            .content(chatMessageDto.getContent())
            .status("Y")
            .sentDate(LocalDateTime.now())
            .type(chatMessageDto.getType())
            .build();
        int messageResult = chatMapper.saveMessage(messageVo);
        ChatValidator.validateDbResult(messageResult, "메시지 저장에 실패했습니다.");

        // 5. 첨부파일 저장
        saveAttachments(files, messageVo.getMessageNo());

        return roomVo;
    }


    /**
     *  메시지 저장
     * - TEXT: 일반 텍스트 메시지 (첨부파일 X)
     * - FILE: 파일 전송 메시지 (첨부파일 필수)
     * - PAYMENT: 결제 메시지 (첨부파일 X)
     */
    @Override
    @Transactional
    public ChatMessageVO saveMessage(ChatMessageDTO chatMessageDto, List<MultipartFile> files) {
        
        String type = chatMessageDto.getType();
        
        // FILE 타입 검증
        if ("FILE".equals(type)) {
            if (files == null || files.isEmpty()) {
                throw new ChatException("파일 타입 메시지는 첨부파일이 필수입니다.");
            }
        }
        
        // 메시지 저장
        String failMsg = getFailMessage(type);
        ChatMessageVO messageVo = buildMessageVO(chatMessageDto);
        saveMessageAndValidate(messageVo, failMsg);
        
        // FILE 타입이면 첨부파일 저장
        if ("FILE".equals(type)) {
            saveAttachments(files, messageVo.getMessageNo());
        }
        
        return messageVo;
    }

 
    /**
     * 견적 상태 검증
     */
    private void validateEstimateStatus(Long estimateNo) {
        String requestStatus = chatMapper.getRequestStatusByEstimateNo(estimateNo);
        String responseStatus = chatMapper.getResponseStatusByEstimateNo(estimateNo);

        if (!"MATCHED".equals(requestStatus) || !"ACCEPTED".equals(responseStatus)) {
            throw new ChatException("채팅방 생성 조건이 충족되지 않았습니다.");
        }
    }


    /**
     * 공통: 메시지 VO 빌더
     */
    private ChatMessageVO buildMessageVO(ChatMessageDTO chatMessageDto) {
        return ChatMessageVO.builder()
            .roomNo(chatMessageDto.getRoomNo())
            .userNo(chatMessageDto.getUserNo())
            .content(chatMessageDto.getContent())
            .status("Y")
            .sentDate(LocalDateTime.now())
            .type(chatMessageDto.getType())
            .build();
    }


    /**
     * 공통: 메시지 저장 및 검증
     */
    private void saveMessageAndValidate(ChatMessageVO messageVo, String failMsg) {
        int result = chatMapper.saveMessage(messageVo);
        ChatValidator.validateDbResult(result, failMsg);
    }


    /**
     * 공통: 첨부파일 저장
     */
    private void saveAttachments(List<MultipartFile> files, Long messageNo) {
        if (files == null || files.isEmpty()) return;
        
        for (MultipartFile file : files) {
            String filePath = s3Service.store(file, "chat");
            ChatAttachmentVO attachmentVo = ChatAttachmentVO.builder()
                .messageNo(messageNo)
                .originName(file.getOriginalFilename())
                .filePath(filePath)
                .uploadDate(LocalDateTime.now())
                .status("Y")
                .build();
            int attachResult = chatMapper.saveChatAttachment(attachmentVo);
            ChatValidator.validateDbResult(attachResult, "첨부파일 저장에 실패했습니다.");
        }
    }


    /**
     * 타입별 에러 메시지
     */
    private String getFailMessage(String type) {
        return switch (type) {
            case "FILE" -> "파일 메시지 저장에 실패했습니다.";
            case "PAYMENT" -> "결제 메시지 저장에 실패했습니다.";
            default -> "메시지 저장에 실패했습니다.";
        };
    }

    
    /**
     * 채팅 메시지 조회 (커서 기반 페이징)
     */
    @Override
    public List<ChatMessageDTO> getMessages(Long roomNo, Long userNo, Long messageNo, int size) {
        if (size <= 0) {
            size = 50;
        }
        ChatValidator.validateGetMessagesParams(roomNo, size);

        Map<String, Object> params = CursorPagination.getCursorParams(messageNo, size);
        params.put("roomNo", roomNo);
        params.put("userNo", userNo);

        List<ChatMessageDTO> messages = chatMapper.getMessagesByCursor(params);
        if (messages == null) {
            throw new ChatException("메시지 조회에 실패했습니다.");
        }
        return messages;
    }


    /**
     * 회원 번호로 닉네임 조회
     */
    @Override
    public String getNicknameByUserNo(Long userNo) {
        return "사용자" + userNo;
        // TODO: 실제 닉네임 조회로 변경
        // return chatMapper.getNicknameByUserNo(userNo);
    }


    /**
     * 견적 번호로 채팅방 번호 조회
     */
    @Override
    public Long getRoomNoByEstimateNo(Long estimateNo) {
        Long roomNo = chatMapper.getRoomNoByEstimateNo(estimateNo);
        if (roomNo == null) {
            throw new ChatException("해당 견적의 채팅방을 찾을 수 없습니다.");
        }
        return roomNo;
    }
}