package com.kh.even.back.admin.model.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.even.back.admin.model.dto.AdminMemberDTO;
import com.kh.even.back.admin.model.dto.AdminMemberListResponse;
import com.kh.even.back.admin.model.dto.AdminMemberSearchRequest;
import com.kh.even.back.admin.model.mapper.AdminMemberMapper;
import com.kh.even.back.exception.ResourceNotFoundException;
import com.kh.even.back.member.model.vo.MemberVO;
import com.kh.even.back.util.PageInfo;

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
    private final AdminMemberValidator validator;
    
    // 페이지당 회원 수
    private static final int BOARD_LIMIT = 10;

    /**
     * 회원 목록 조회 (페이징 + 검색)
     */
    @Override
    @Transactional(readOnly = true)
    public AdminMemberListResponse getMemberListWithPaging(AdminMemberSearchRequest request) {
        log.info("회원 목록 조회 - {}", request);
        
        // 1. 검색 키워드 처리
        String keyword = request.getSearchKeyword() != null ? request.getSearchKeyword() : request.getKeyword();
        boolean isNumeric = isNumericKeyword(keyword);
        
        // 2. 페이징 정보 생성
        PageInfo pageInfo = createPageInfo(request.getCurrentPage(), keyword, isNumeric,
        		request.getStatus(),
        		request.getPenaltyStatus(),
        		request.getUserRole());
        
        // 3. 회원 목록 조회 및 변환
        List<AdminMemberDTO> memberList = fetchAndConvertMemberList(pageInfo, keyword, isNumeric,
        		request.getStatus(),
        		request.getPenaltyStatus(),
        		request.getUserRole());
        
        // 4. 응답 생성
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
        
        // 기존 회원 조회 (존재 확인)
        MemberVO member = findMemberByUserNo(userNo);
        
        // 검증
        validator.validateStatusChange(String.valueOf(member.getStatus()), String.valueOf(status));
        
        // 상태 변경
        adminMemberMapper.updateMemberStatus(userNo, status);
        
        log.info("회원 상태 변경 성공 - userNo: {}", userNo);
    }

    /**
     * 징계 상태 변경
     */
    @Override
    @Transactional
    public void updatePenaltyStatus(Long userNo, char penaltyStatus) {
        log.info("징계 상태 변경 - userNo: {}, penaltyStatus: {}", userNo, penaltyStatus);
        
        // 기존 회원 조회 (존재 확인)
        MemberVO member = findMemberByUserNo(userNo);
        
        // 검증
        validator.validatePenaltyChange(member.getPenaltyStatus(), String.valueOf(penaltyStatus));
        
        // 징계 상태 변경
        adminMemberMapper.updatePenaltyStatus(userNo, penaltyStatus);
        
        log.info("징계 상태 변경 성공 - userNo: {}", userNo);
    }

    /**
     * 권한 변경
     */
    @Override
    @Transactional
    public void updateUserRole(Long userNo, String userRole) {
        log.info("권한 변경 - userNo: {}, userRole: {}", userNo, userRole);
        
        // 기존 회원 조회 (존재 확인)
        MemberVO member = findMemberByUserNo(userNo);
        
        // 검증
        validator.validateRoleChange(member.getUserRole(), userRole);
        
        // 권한 변경
        adminMemberMapper.updateUserRole(userNo, userRole);
        
        log.info("권한 변경 성공 - userNo: {}", userNo);
    }

    /**
     * 검색 키워드가 숫자인지 판별
     */
    private boolean isNumericKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return false;
        }
        return keyword.matches("^[0-9]+$");
    }

    /**
     * 페이징 정보 생성
     */
    private PageInfo createPageInfo(int currentPage, String keyword, boolean isNumeric, String status, String penaltyStatus, String userRole) {
        int totalCount = adminMemberMapper.getMemberCount(keyword, isNumeric, status, penaltyStatus, userRole);
        log.debug("전체 회원 수: {}", totalCount);
        
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
        
        return pageInfo;
    }

    /**
     * 회원 목록 조회 및 DTO 변환
     */
    private List<AdminMemberDTO> fetchAndConvertMemberList(PageInfo pageInfo, String keyword, boolean isNumeric,
    		String status,
    		String penaltyStatus,
    		String userRole) {
        int startRow = (pageInfo.getCurrentPage() - 1) * BOARD_LIMIT + 1;
        int endRow = pageInfo.getCurrentPage() * BOARD_LIMIT;
        
        List<MemberVO> memberVOList = adminMemberMapper.getMemberList(startRow, endRow, keyword, isNumeric, status, penaltyStatus, userRole);
        log.debug("조회된 회원 수: {}", memberVOList.size());
        
        return memberVOList.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * 회원번호로 회원 조회 (공통 메서드)
     */
    private MemberVO findMemberByUserNo(Long userNo) {
        MemberVO member = adminMemberMapper.getMemberDetail(userNo);
        if (member == null) {
            throw new ResourceNotFoundException("회원을 찾을 수 없습니다. userNo: " + userNo);
        }
        return member;
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
        dto.setStatus(String.valueOf(vo.getStatus()));
        dto.setUserRole(vo.getUserRole());
        dto.setPenaltyStatus(vo.getPenaltyStatus());
        return dto;
    }
}