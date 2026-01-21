package com.kh.even.back.admin.model.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.even.back.admin.model.dto.MemberDTO;
import com.kh.even.back.admin.model.mapper.AdminMemberMapper;  // ✅ 수정
import com.kh.even.back.member.model.vo.MemberVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service  // ✅ Bean 이름 제거
@RequiredArgsConstructor
@Transactional
public class AdminMemberServiceImpl implements AdminMemberService {  // ✅ 클래스명 변경
    
    private final AdminMemberMapper adminMemberMapper;  // ✅ 변경
    
    private static final int BOARD_LIMIT = 10;
    
    @Override
    public List<AdminMemberDTO> getMemberList(int currentPage, String keyword) {
        int startRow = (currentPage - 1) * BOARD_LIMIT + 1;
        int endRow = currentPage * BOARD_LIMIT;
        
        List<MemberVO> memberList = adminMemberMapper.getMemberList(startRow, endRow, keyword);  // ✅ 변경
        
        return memberList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public int getMemberCount(String keyword) {
        return adminMemberMapper.getMemberCount(keyword);  // ✅ 변경
    }
    
    @Override
    public AdminMemberDTO getMemberDetail(Long userNo) {
        MemberVO member = adminMemberMapper.getMemberDetail(userNo);  // ✅ 변경
        return member != null ? convertToDTO(member) : null;
    }
    
    @Override
    public boolean updateMemberStatus(Long userNo, char status) {
        return adminMemberMapper.updateMemberStatus(userNo, status) > 0;  // ✅ 변경
    }
    
    @Override
    public boolean updatePenaltyStatus(Long userNo, String penaltyStatus) {
        return adminMemberMapper.updatePenaltyStatus(userNo, penaltyStatus) > 0;  // ✅ 변경
    }
    
    @Override
    public boolean updateUserRole(Long userNo, String userRole) {
        return adminMemberMapper.updateUserRole(userNo, userRole) > 0;  // ✅ 변경
    }
    
    // DTO 변환 메서드
    private AdminMemberDTO convertToDTO(MemberVO vo) {
        AdminMemberDTO dto = new AdminMemberDTO();
        dto.setUserNo(vo.getUserNo());
        dto.setEmail(vo.getEmail());
        dto.setUserName(vo.getUserName());
        dto.setNickname(vo.getNickname());
        dto.setPhone(vo.getPhone());
        dto.setBirthday(vo.getBirthday());
        dto.setGender(vo.getGender());
        dto.setPostcode(vo.getPostcode());
        dto.setAddress(vo.getAddress());
        dto.setAddressDetail(vo.getAddressDetail());
        dto.setStatus(vo.getStatus());
        dto.setCreateDate(vo.getCreateDate());
        dto.setDeleteDate(vo.getDeleteDate());
        dto.setUpdateDate(vo.getUpdateDate());
        dto.setUserRole(vo.getUserRole());
        dto.setPenaltyStatus(vo.getPenaltyStatus());
        return dto;
    }
}