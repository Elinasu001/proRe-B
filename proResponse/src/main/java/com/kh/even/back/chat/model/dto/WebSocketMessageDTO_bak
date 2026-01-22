package com.kh.even.back.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WebSocketMessageDTO {
    private String type;      // "MESSAGE", "FILE", "ENTER", "LEAVE"
    private Long roomNo;
    private Long userNo;
    private String userName;
    private String content;
    private String filePath;  // 첨부파일 (있으면)
    private String originName;

    // 서버에서 추가하는 필드
    private Long messageNo;
    private String sentDate;
}
