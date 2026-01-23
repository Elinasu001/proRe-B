package com.kh.even.back.chat.model.dto;

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
public class ChatRoomActionsDTO {

    private boolean reported;   // 신고 여부
    private boolean reviewed;   // 리뷰 작성 여부
    private boolean paid;       // 결제 여부

}
