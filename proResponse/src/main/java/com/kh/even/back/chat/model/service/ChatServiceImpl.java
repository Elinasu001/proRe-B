
package com.kh.even.back.chat.model.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.even.back.chat.model.dao.ChatMapper;
import com.kh.even.back.chat.model.dto.ChatAttachmentDTO;
import com.kh.even.back.chat.model.dto.ChatMessageDTO;
import com.kh.even.back.chat.model.dto.ChatRoomDTO;
import com.kh.even.back.chat.model.vo.ChatAttachmentVO;
import com.kh.even.back.chat.model.vo.ChatMessageVO;
import com.kh.even.back.chat.model.vo.ChatRoomUserVO;
import com.kh.even.back.chat.model.vo.ChatRoomVO;
import com.kh.even.back.common.validator.AssertUtil;
import com.kh.even.back.exception.ChatException;
import com.kh.even.back.file.service.FileUploadService;
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
    private final FileUploadService fileUploadService;

    /**
     * 채팅방 생성 및 초기 메시지 저장
     */
    @Override
    @Transactional
    public ChatRoomVO createRoom(Long estimateNo, ChatMessageDTO chatMessageDto, Long userNo) {

        // 1. 견적 상태 체크
        validateEstimateStatus(estimateNo);

        // 2. 이미 채팅방이 있으면 반환, 없으면 생성
        boolean exists = reviewService.existsByEstimateNo(estimateNo);
        if (exists) {
            throw new ChatException("이미 채팅방이 존재합니다");
        }

        // 3. 채팅방 생성
        ChatRoomVO roomVo = ChatRoomVO.builder()
            .estimateNo(estimateNo)
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
        saveAttachments(chatMessageDto.getFiles(), messageVo.getMessageNo());
        
        return roomVo;
    }

    /**
     *  견적 상태 검증
     */
    private void validateEstimateStatus(Long estimateNo) {
        String requestStatus = chatMapper.getRequestStatusByEstimateNo(estimateNo);
        String responseStatus = chatMapper.getResponseStatusByEstimateNo(estimateNo);

        if (!"MATCHED".equals(requestStatus) || !"ACCEPTED".equals(responseStatus)) {
            throw new ChatException("채팅방 생성 조건이 충족되지 않았습니다.");
        }
    }

    /**
     * 첨부파일 저장
     */
    private void saveAttachments(List<MultipartFile> files, Long messageNo) {
        AssertUtil.validateImageFiles(files);
        fileUploadService.uploadFiles(files, "chat", messageNo, chatMapper::saveChatAttachment);
    }


    /**
     *  메시지 저장
     * - TEXT: 일반 텍스트 메시지 (첨부파일 X)
     * - FILE: 파일 전송 메시지 (첨부파일 필수)
     * - PAYMENT: 결제 메시지 (첨부파일 X)
     */
    @Override
    @Transactional
    public ChatMessageVO saveMessage(ChatMessageDTO chatMessageDto, Long userNo) {
        
        String type = chatMessageDto.getType();

        // FILE 타입 검증
        if ("FILE".equals(type)) {
            if (chatMessageDto.getFiles() == null || chatMessageDto.getFiles().isEmpty()) {
                throw new ChatException("파일 타입 메시지는 첨부파일이 필수입니다.");
            }
        }

        // 메시지 저장
        String failMsg = getFailMessage(type);
        ChatMessageVO messageVo = buildMessageVO(chatMessageDto, userNo);
        saveMessageAndValidate(messageVo, failMsg);
        
        // FILE 타입이면 첨부파일 저장
        if ("FILE".equals(type)) {
            saveAttachments(chatMessageDto.getFiles(), messageVo.getMessageNo());
        }
        return messageVo;
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
     * 메시지 VO 빌더
     */
    private ChatMessageVO buildMessageVO(ChatMessageDTO chatMessageDto, Long userNo) {
        return ChatMessageVO.builder()
            .roomNo(chatMessageDto.getRoomNo())
            .userNo(userNo)
            .content(chatMessageDto.getContent())
            .status("Y")
            .sentDate(LocalDateTime.now())
            .type(chatMessageDto.getType())
            .build();
    }


    /**
     * 메시지 저장 및 검증
     */
    private void saveMessageAndValidate(ChatMessageVO messageVo, String failMsg) {
        int result = chatMapper.saveMessage(messageVo);
        ChatValidator.validateDbResult(result, failMsg);
    }
    
    /**
     * 채팅 메시지 조회 (커서 기반 페이징)
     */
    @Override
    public List<ChatMessageDTO> getMessages(Long roomNo, ChatMessageDTO chatMessageDto, Long userNo) {
        if (chatMessageDto.getSize() <= 0) {
            chatMessageDto.setSize(50);
        }
        ChatValidator.validateGetMessagesParams(roomNo, chatMessageDto.getSize());

        Map<String, Object> params = CursorPagination.getCursorParams(chatMessageDto.getMessageNo(), chatMessageDto.getSize());
        params.put("roomNo", roomNo);
        params.put("userNo", userNo);

        List<ChatMessageDTO> messages = chatMapper.getMessagesByCursor(params);
        if (messages == null) {
            throw new ChatException("메시지 조회에 실패했습니다.");
        }

        // 각 메시지의 userNo와 현재 userNo를 비교하여 isMine 값 세팅
        // 그리고 FILE 타입 메시지의 messageNo 수집
        List<Long> fileMessageNos = new ArrayList<>();
        for (ChatMessageDTO msg : messages) {
            msg.setMine(msg.getUserNo() != null && msg.getUserNo().equals(userNo));
            if ("FILE".equals(msg.getType())) {
                fileMessageNos.add(msg.getMessageNo());
            }
        }

        // // FILE 타입 메시지의 첨부파일 일괄 조회 및 매핑
        if (!fileMessageNos.isEmpty()) {
            List<ChatAttachmentDTO> attachments = chatMapper.getAttachmentsByMessageNos(fileMessageNos);
            // messageNo -> List<ChatAttachmentDTO> 맵핑
            Map<Long, List<ChatAttachmentDTO>> attachMap = new HashMap<>();
            for (ChatAttachmentDTO att : attachments) {
                attachMap.computeIfAbsent(att.getMessageNo(), k -> new ArrayList<>()).add(att);
            }
            for (ChatMessageDTO msg : messages) {
                if ("FILE".equals(msg.getType())) {
                    msg.setAttachments(attachMap.getOrDefault(msg.getMessageNo(), Collections.emptyList()));
                }
            }
        }
        

        return messages;
    }

    
    /**
     * 회원 번호로 닉네임 조회
     */
    @Override
    public String getNicknameByUserNo(Long userNo) {
        return "사용자" + userNo;
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