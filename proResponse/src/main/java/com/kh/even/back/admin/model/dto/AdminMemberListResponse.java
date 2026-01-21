package com.kh.even.back.admin.model.dto;

import java.util.List;

import com.kh.even.back.util.PageInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMemberListResponse {
    private List<AdminMemberDTO> memberList;
    private int totalCount;
    private int currentPage;
    private PageInfo pageInfo;
}