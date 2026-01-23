package com.kh.even.back.admin.model.dto;

import java.util.List;

import com.kh.even.back.util.PageInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;  // 추가
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder  
public class AdminMemberListResponse {
    private List<AdminMemberDTO> memberList;
    private PageInfo pageInfo;
}