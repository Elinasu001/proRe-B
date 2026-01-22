
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
        ChatMessageVO messageVO = ChatMessageVO.builder()
            .roomNo(roomVo.getRoomNo())
            .userNo(userNo)
            .content(chatMessageDto.getContent())
            .status("Y")
            .sentDate(LocalDateTime.now())
            .type(chatMessageDto.getType())
            .build();
        int messageResult = chatMapper.saveMessage(messageVO);
        ChatValidator.validateDbResult(messageResult, "메시지 저장에 실패했습니다.");

        // 저장된 메시지의 PK get 하기
        Long messageNo = messageVO.getMessageNo();

        // 5. 첨부파일 등록
        if (files != null && !files.isEmpty()) {
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

        return roomVo;
    }

    /**
     * 견적 상태 검증
     */
    private void validateEstimateStatus(Long estimateNo) {
        // 견적 요청 상태 조회 (TB_ESTIMATE_REQUEST)
        String requestStatus = chatMapper.getRequestStatusByEstimateNo(estimateNo);
        // 견적 응답 상태 조회 (TB_ESTIMATE_RESPONSE)
        String responseStatus = chatMapper.getResponseStatusByEstimateNo(estimateNo);

        // 두 조건 모두 충족해야 채팅방 생성 가능
        if (!"MATCHED".equals(requestStatus) || !"ACCEPTED".equals(responseStatus)) {
            throw new ChatException("채팅방 생성 조건이 충족되지 않았습니다.");
        }
    }


    @Override
    @Transactional
    public ChatMessageVO saveMessage(ChatMessageDTO chatMessageDto, List<MultipartFile> files) {
        String type = chatMessageDto.getType();
        String failMsg = "메시지 저장에 실패했습니다.";
        if ("FILE".equals(type)) failMsg = "파일 메시지 저장에 실패했습니다.";
        else if ("PAYMENT".equals(type)) failMsg = "결제 메시지 저장에 실패했습니다.";

        ChatMessageVO messageVO = buildMessageVO(chatMessageDto);
        saveMessageAndValidate(messageVO, failMsg);

        if ("FILE".equals(type)) {
            saveAttachments(files, messageVO.getMessageNo());
        }
        // 결제 메시지라면 결제 관련 추가 처리 필요시 여기에
        return messageVO;
    }

    // 공통 메시지 VO 빌더
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

    // 공통 메시지 저장 및 검증
    private void saveMessageAndValidate(ChatMessageVO messageVO, String failMsg) {
        int result = chatMapper.saveMessage(messageVO);
        ChatValidator.validateDbResult(result, failMsg);
    }

    // 첨부파일 저장 공통 메서드
    private void saveAttachments(List<MultipartFile> files, Long messageNo) {
        if (files == null || files.isEmpty()) return;
        for (MultipartFile file : files) {
            String filePath = s3Service.store(file, "chat");
            ChatAttachmentVO attachmentVO = ChatAttachmentVO.builder()
                    .messageNo(messageNo)
                    .originName(file.getOriginalFilename())
                    .filePath(filePath)
                    .uploadDate(LocalDateTime.now())
                    .status("Y")
                    .build();
            int attachResult = chatMapper.saveChatAttachment(attachmentVO);
            ChatValidator.validateDbResult(attachResult, "첨부파일 저장에 실패했습니다.");
        }
    }

    
    /**
     * 채팅 메시지 조회 (커서 기반 페이징)
     */
    @Override
    public List<ChatMessageDTO> getMessages(Long roomNo, Long userNo, Long messageNo, int size) {
        if (size <= 0) {
            size = 50; // 기본값 지정
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

}