package com.kh.even.back.admin.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMemberDTO {
    private Long userNo;
    private String email;
    private String userName;
    private String nickname;
    private String phone;
    private String birthday;        // birthDate → birthday
    private char gender;
    private String postcode;        // 추가
    private String address;
    private String addressDetail;   // 추가
    private String status;          // String
    private Date createDate;
    private Date updateDate;
    private String userRole;
    private String penaltyStatus;   // String
}