package com.kh.even.back.admin.model.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.even.back.admin.model.dto.AdminMemberDTO;
import com.kh.even.back.admin.model.dto.AdminMemberListResponse;
import com.kh.even.back.admin.model.mapper.AdminMemberMapper;
import com.kh.even.back.member.model.vo.MemberVO;
import com.kh.even.back.util.PageInfo;
import com.kh.even.back.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 회원 관리 서비스 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminMemberServiceImpl implements AdminMemberService {

    private final AdminMemberMapper adminMemberMapper;
    
    // 페이지당 회원 수
    private static final int BOARD_LIMIT = 10;

    /**
     * 회원 목록 조회 (페이징 + 검색)
     */
    @Override
    @Transactional(readOnly = true)
    public AdminMemberListResponse getMemberListWithPaging(int currentPage, String keyword) {
        log.info("회원 목록 조회 - currentPage: {}, keyword: {}", currentPage, keyword);
        
        // 숫자 판별 로직 추가
        boolean isNumeric = false;
        if (keyword != null && !keyword.trim().isEmpty()) {
            isNumeric = keyword.matches("^[0-9]+$");
        }
        
        // 전체 회원 수 조회
        int totalCount = adminMemberMapper.getMemberCount(keyword, isNumeric);
        log.debug("전체 회원 수: {}", totalCount);
        
        // 페이징 정보 생성
        PageInfo pageInfo = new PageInfo();
        pageInfo.setListCount(totalCount);
        pageInfo.setCurrentPage(currentPage);
        pageInfo.setBoardLimit(BOARD_LIMIT);
        
        int pageLimit = 10; // 페이지 버튼 개수
        int maxPage = (int) Math.ceil((double) totalCount / BOARD_LIMIT);
        int startPage = ((currentPage - 1) / pageLimit) * pageLimit + 1;
        int endPage = Math.min(startPage + pageLimit - 1, maxPage);
        
        pageInfo.setPageLimit(pageLimit);
        pageInfo.setMaxPage(maxPage);
        pageInfo.setStartPage(startPage);
        pageInfo.setEndPage(endPage);
        
        // 페이징 범위 계산
        int startRow = (currentPage - 1) * BOARD_LIMIT + 1;
        int endRow = currentPage * BOARD_LIMIT;
        
        // 회원 목록 조회
        List<MemberVO> memberVOList = adminMemberMapper.getMemberList(startRow, endRow, keyword, isNumeric);
        log.debug("조회된 회원 수: {}", memberVOList.size());
        
        // VO → DTO 변환
        List<AdminMemberDTO> memberList = memberVOList.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return AdminMemberListResponse.builder()
            .memberList(memberList)
            .pageInfo(pageInfo)
            .build();
    }

    /**
     * 회원 상세 조회
     */
    @Override
    @Transactional(readOnly = true)
    public AdminMemberDTO getMemberDetail(Long userNo) {
        log.info("회원 상세 조회 - userNo: {}", userNo);
        
        MemberVO memberVO = adminMemberMapper.getMemberDetail(userNo);
        
        if (memberVO == null) {
            log.warn("회원을 찾을 수 없음 - userNo: {}", userNo);
            throw new ResourceNotFoundException("회원을 찾을 수 없습니다. userNo: " + userNo);
        }
        
        return convertToDTO(memberVO);
    }

    /**
     * 회원 상태 변경
     */
    @Override
    @Transactional
    public void updateMemberStatus(Long userNo, char status) {
        log.info("회원 상태 변경 - userNo: {}, status: {}", userNo, status);
        
        int result = adminMemberMapper.updateMemberStatus(userNo, status);
        
        if (result == 0) {
            log.warn("회원 상태 변경 실패 - 회원을 찾을 수 없음: {}", userNo);
            throw new ResourceNotFoundException("회원을 찾을 수 없습니다. userNo: " + userNo);
        }
        
        log.info("회원 상태 변경 성공 - userNo: {}", userNo);
    }

    /**
     * 징계 상태 변경
     */
    @Override
    @Transactional
    public void updatePenaltyStatus(Long userNo, char penaltyStatus) {
        log.info("징계 상태 변경 - userNo: {}, penaltyStatus: {}", userNo, penaltyStatus);
        
        int result = adminMemberMapper.updatePenaltyStatus(userNo, penaltyStatus);
        
        if (result == 0) {
            log.warn("징계 상태 변경 실패 - 회원을 찾을 수 없음: {}", userNo);
            throw new ResourceNotFoundException("회원을 찾을 수 없습니다. userNo: " + userNo);
        }
        
        log.info("징계 상태 변경 성공 - userNo: {}", userNo);
    }

    /**
     * 권한 변경
     */
    @Override
    @Transactional
    public void updateUserRole(Long userNo, String userRole) {
        log.info("권한 변경 - userNo: {}, userRole: {}", userNo, userRole);
        
        int result = adminMemberMapper.updateUserRole(userNo, userRole);
        
        if (result == 0) {
            log.warn("권한 변경 실패 - 회원을 찾을 수 없음: {}", userNo);
            throw new ResourceNotFoundException("회원을 찾을 수 없습니다. userNo: " + userNo);
        }
        
        log.info("권한 변경 성공 - userNo: {}", userNo);
    }

    /**
     * VO → DTO 변환 (private 헬퍼 메서드)
     */
    private AdminMemberDTO convertToDTO(MemberVO vo) {
        AdminMemberDTO dto = new AdminMemberDTO();
        dto.setUserNo(vo.getUserNo());
        dto.setEmail(vo.getEmail());
        dto.setUserName(vo.getUserName());
        dto.setNickname(vo.getNickname());
        dto.setPhone(vo.getPhone());
        dto.setGender(vo.getGender());
        dto.setBirthday(vo.getBirthday());
        dto.setPostcode(vo.getPostcode());
        dto.setAddress(vo.getAddress());
        dto.setAddressDetail(vo.getAddressDetail());
        dto.setCreateDate(vo.getCreateDate());
        dto.setUpdateDate(vo.getUpdateDate());
        dto.setStatus(String.valueOf(vo.getStatus()));  // char → String
        dto.setUserRole(vo.getUserRole());
        dto.setPenaltyStatus(vo.getPenaltyStatus());  // String → String
        return dto;
    }
}