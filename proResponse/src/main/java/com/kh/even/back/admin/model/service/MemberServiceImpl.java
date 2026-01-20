package com.kh.even.back.admin.model.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.even.back.admin.model.dto.MemberDTO;
import com.kh.even.back.admin.model.mapper.MemberMapper;
import com.kh.even.back.member.model.vo.MemberVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {
    
    private final MemberMapper memberMapper;  // ✅ Mapper 주입
    
    private static final int BOARD_LIMIT = 10; // 페이지당 회원 수
    
    @Override
    public List<MemberDTO> getMemberList(int currentPage, String keyword) {
        int startRow = (currentPage - 1) * BOARD_LIMIT + 1;
        int endRow = currentPage * BOARD_LIMIT;
        
        List<MemberVO> memberList = memberMapper.getMemberList(startRow, endRow, keyword);
        
        return memberList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public int getMemberCount(String keyword) {
        return memberMapper.getMemberCount(keyword);
    }
    
    @Override
    public MemberDTO getMemberDetail(Long userNo) {
        MemberVO member = memberMapper.getMemberDetail(userNo);
        return member != null ? convertToDTO(member) : null;
    }
    
    @Override
    public boolean updateMemberStatus(Long userNo, char status) {
        int result = memberMapper.updateMemberStatus(userNo, status);
        log.info("회원 상태 변경 - userNo: {}, status: {}, result: {}", userNo, status, result);
        return result > 0;
    }
    
    @Override
    public boolean updateUserRole(Long userNo, String userRole) {
        int result = memberMapper.updateUserRole(userNo, userRole);
        log.info("회원 권한 변경 - userNo: {}, userRole: {}, result: {}", userNo, userRole, result);
        return result > 0;
    }
    
    @Override
    public boolean updatePenaltyStatus(Long userNo, char penaltyStatus) {
        // DTO의 char를 VO의 String으로 변환
        String penaltyStatusStr = String.valueOf(penaltyStatus);
        int result = memberMapper.updatePenaltyStatus(userNo, penaltyStatusStr);
        log.info("징계 상태 변경 - userNo: {}, penaltyStatus: {}, result: {}", userNo, penaltyStatus, result);
        return result > 0;
    }
    
    /**
     * VO -> DTO 변환
     */
    private MemberDTO convertToDTO(MemberVO vo) {
        return new MemberDTO(
            vo.getUserNo(),
            vo.getEmail(),
            vo.getUserName(),
            vo.getNickname(),
            vo.getPhone(),
            vo.getBirthday(),
            vo.getGender(),
            vo.getPostcode(),
            vo.getAddress(),
            vo.getAddressDetail(),
            vo.getStatus(),
            vo.getCreateDate(),
            vo.getDeleteDate(),
            vo.getUpdateDate(),
            vo.getUserRole(),
            vo.getPenaltyStatus() != null ? vo.getPenaltyStatus().charAt(0) : 'N'  // String -> char 변환
        );
    }
}