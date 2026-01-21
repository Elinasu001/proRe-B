package com.kh.even.back.admin.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.even.back.admin.model.dto.AdminMemberDTO;
import com.kh.even.back.admin.model.dto.AdminMemberListResponse;
import com.kh.even.back.admin.model.mapper.AdminMemberMapper;
import com.kh.even.back.member.model.vo.MemberVO;
import com.kh.even.back.util.PageInfo;
import com.kh.even.back.util.Pagenation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor  // ✅ @Builder 제거됨
public class AdminMemberServiceImpl implements AdminMemberService {

    private final AdminMemberMapper adminMemberMapper;
    private final Pagenation pagenation;

    private static final int BOARD_LIMIT = 10;

    @Override
    public AdminMemberListResponse getMemberListWithPaging(int currentPage, String keyword) {
        int totalCount = adminMemberMapper.getMemberCount(keyword);
        PageInfo pageInfo = pagenation.getPageInfo(totalCount, currentPage, 5, BOARD_LIMIT);
        
        int startRow = (currentPage - 1) * BOARD_LIMIT + 1;
        int endRow = currentPage * BOARD_LIMIT;
        
        List<MemberVO> memberVOList = adminMemberMapper.getMemberList(startRow, endRow, keyword);
        
        List<AdminMemberDTO> memberList = memberVOList.stream()
            .map(this::convertToDTO)
            .toList();
        
        return new AdminMemberListResponse(memberList, totalCount, currentPage, pageInfo);
    }

    private AdminMemberDTO convertToDTO(MemberVO vo) {
        return AdminMemberDTO.builder()
            .userNo(vo.getUserNo())
            .email(vo.getEmail())
            .userName(vo.getUserName())
            .nickname(vo.getNickname())
            .phone(vo.getPhone())
            .createDate(vo.getCreateDate())
            .userRole(vo.getUserRole())
            .penaltyStatus(vo.getPenaltyStatus())
            .status(String.valueOf(vo.getStatus()))  // ✅ char → String 변환
            .build();
    }
    
    // ✅ 나머지 메서드들도 구현 필요
    @Override
    public AdminMemberDTO getMemberDetail(Long userNo) {
        MemberVO vo = adminMemberMapper.getMemberDetail(userNo);
        return convertToDTO(vo);
    }

    @Override
    public boolean updateMemberStatus(Long userNo, char status) {
        return adminMemberMapper.updateMemberStatus(userNo, status) > 0;
    }

    @Override
    public boolean updatePenaltyStatus(Long userNo, String penaltyStatus) {
        return adminMemberMapper.updatePenaltyStatus(userNo, penaltyStatus) > 0;
    }

    @Override
    public boolean updateUserRole(Long userNo, String userRole) {
        return adminMemberMapper.updateUserRole(userNo, userRole) > 0;
    }
}