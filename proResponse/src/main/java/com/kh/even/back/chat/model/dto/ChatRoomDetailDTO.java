package com.kh.even.back.chat.model.dto;


import java.time.LocalDateTime;
import java.util.List;

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
public class ChatRoomDetailDTO {
    private Long roomNo;
    private String status;
    private LocalDateTime createDate;
    private Long estimateNo;
    private Long userNo;
    private String userName;

    private List<ChatMessageDTO> messages;

}
