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
    
    private final MemberMapper memberMapper;
    private final MemberValidator memberValidator;
    private final MemberStatusManager statusManager;
    
    private static final int BOARD_LIMIT = 10;
    
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
    public boolean activateMember(Long userNo) {
        log.info("회원 활성화 시작 - userNo: {}", userNo);
        
        // 1. 현재 회원 정보 조회
        MemberVO currentMember = memberMapper.getMemberDetail(userNo);
        if (currentMember == null) {
            log.warn("회원을 찾을 수 없음 - userNo: {}", userNo);
            return false;
        }
        
        // 2. 검증
        String currentStatus = String.valueOf(currentMember.getStatus());
        String newStatus = statusManager.activateMember();
        memberValidator.validateStatusChange(currentStatus, newStatus);
        
        // 3. 상태 변경
        char statusChar = statusManager.toStatusChar(newStatus);
        int result = memberMapper.updateMemberStatus(userNo, statusChar);
        
        log.info("회원 활성화 완료 - userNo: {}, result: {}", userNo, result);
        return result > 0;
    }
    
    @Override
    public boolean deactivateMember(Long userNo) {
        log.info("회원 비활성화 시작 - userNo: {}", userNo);
        
        // 1. 현재 회원 정보 조회
        MemberVO currentMember = memberMapper.getMemberDetail(userNo);
        if (currentMember == null) {
            log.warn("회원을 찾을 수 없음 - userNo: {}", userNo);
            return false;
        }
        
        // 2. 검증
        String currentStatus = String.valueOf(currentMember.getStatus());
        String newStatus = statusManager.deactivateMember();
        memberValidator.validateStatusChange(currentStatus, newStatus);
        
        // 3. 상태 변경
        char statusChar = statusManager.toStatusChar(newStatus);
        int result = memberMapper.updateMemberStatus(userNo, statusChar);
        
        log.info("회원 비활성화 완료 - userNo: {}, result: {}", userNo, result);
        return result > 0;
    }
    
    @Override
    public boolean changeMemberRole(Long userNo, String newRole) {
        log.info("회원 권한 변경 시작 - userNo: {}, newRole: {}", userNo, newRole);
        
        // 1. 현재 회원 정보 조회
        MemberVO currentMember = memberMapper.getMemberDetail(userNo);
        if (currentMember == null) {
            log.warn("회원을 찾을 수 없음 - userNo: {}", userNo);
            return false;
        }
        
        // 2. 검증
        memberValidator.validateRoleChange(currentMember.getUserRole(), newRole);
        
        // 3. 권한 변경
        int result = memberMapper.updateUserRole(userNo, newRole);
        
        log.info("회원 권한 변경 완료 - userNo: {}, newRole: {}, result: {}", userNo, newRole, result);
        return result > 0;
    }
    
    @Override
    public boolean applyPenaltyToMember(Long userNo) {
        log.info("징계 적용 시작 - userNo: {}", userNo);
        
        // 1. 현재 회원 정보 조회
        MemberVO currentMember = memberMapper.getMemberDetail(userNo);
        if (currentMember == null) {
            log.warn("회원을 찾을 수 없음 - userNo: {}", userNo);
            return false;
        }
        
        // 2. 검증
        String newPenalty = statusManager.applyPenalty();
        memberValidator.validatePenaltyChange(currentMember.getPenaltyStatus(), newPenalty);
        
        // 3. 징계 적용
        int result = memberMapper.updatePenaltyStatus(userNo, newPenalty);
        
        log.info("징계 적용 완료 - userNo: {}, result: {}", userNo, result);
        return result > 0;
    }
    
    @Override
    public boolean removePenaltyFromMember(Long userNo) {
        log.info("징계 해제 시작 - userNo: {}", userNo);
        
        // 1. 현재 회원 정보 조회
        MemberVO currentMember = memberMapper.getMemberDetail(userNo);
        if (currentMember == null) {
            log.warn("회원을 찾을 수 없음 - userNo: {}", userNo);
            return false;
        }
        
        // 2. 검증
        String newPenalty = statusManager.removePenalty();
        memberValidator.validatePenaltyChange(currentMember.getPenaltyStatus(), newPenalty);
        
        // 3. 징계 해제
        int result = memberMapper.updatePenaltyStatus(userNo, newPenalty);
        
        log.info("징계 해제 완료 - userNo: {}, result: {}", userNo, result);
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
            String.valueOf(vo.getStatus()),
            vo.getCreateDate(),
            vo.getDeleteDate(),
            vo.getUpdateDate(),
            vo.getUserRole(),
            vo.getPenaltyStatus()
        );
    }
}