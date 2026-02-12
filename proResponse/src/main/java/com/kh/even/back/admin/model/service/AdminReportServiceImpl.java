package com.kh.even.back.admin.model.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.even.back.admin.model.dto.AdminReportChatContext;
import com.kh.even.back.admin.model.dto.AdminReportDTO;
import com.kh.even.back.admin.model.dto.AdminReportListResponse;
import com.kh.even.back.admin.model.entity.Report;
import com.kh.even.back.admin.model.mapper.AdminMemberMapper;
import com.kh.even.back.admin.model.mapper.AdminReportMapper;
import com.kh.even.back.admin.model.repository.AdminReportRepository;
import com.kh.even.back.exception.ResourceNotFoundException;
import com.kh.even.back.util.PageInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 신고 관리 서비스 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminReportServiceImpl implements AdminReportService {

	private final AdminReportRepository reportRepository;
	private final AdminMemberMapper memberMapper;
	private final AdminReportValidator validator;
	private final AdminReportMapper adminReportMapper;
	private final AdminMemberService adminMemberService;

    // 페이지당 신고 수
    private static final int PAGE_SIZE = 10;

    /**
     * 신고 목록 조회 (페이징 + 검색)
     */
    @Override
    @Transactional(readOnly = true)
    public AdminReportListResponse getReportListWithPaging(int currentPage, String status, Integer reasonNo) {
        log.info("신고 목록 조회 - currentPage: {}, status: {}, reasonNo: {}", currentPage, status, reasonNo);

        // Pageable 생성 (최신순 정렬)
        Pageable pageable = PageRequest.of(currentPage - 1, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createDate"));

        // 검색 조건에 따라 조회
        Page<Report> reportPage;
        if (status != null || reasonNo != null) {
            reportPage = reportRepository.searchReports(status, reasonNo, pageable);
        } else {
            reportPage = reportRepository.findAllWithTag(pageable);
        }

        // Entity -> DTO 변환
        List<AdminReportDTO> reportList = reportPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // PageInfo 생성
        PageInfo pageInfo = createPageInfo(currentPage, (int) reportPage.getTotalElements());

        return AdminReportListResponse.builder()
                .reportList(reportList)
                .pageInfo(pageInfo)
                .build();
    }

    /**
     * 신고 상세 조회
     */
    @Override
    @Transactional(readOnly = true)
    public AdminReportDTO getReportDetail(Long reportNo) {
        log.info("신고 상세 조회 - reportNo: {}", reportNo);

        Report report = findReportByReportNo(reportNo);
        return convertToDTO(report);
    }

    /**
     * 신고 대상자별 신고 내역 조회
     */
    @Override
    @Transactional(readOnly = true)
    public AdminReportListResponse getReportsByTargetUser(Long targetUserNo, int currentPage) {
        log.info("신고 대상자별 내역 조회 - targetUserNo: {}, currentPage: {}", targetUserNo, currentPage);

        Pageable pageable = PageRequest.of(currentPage - 1, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createDate"));

        Page<Report> reportPage = reportRepository.findByTargetUserNoWithTag(targetUserNo, pageable);

        List<AdminReportDTO> reportList = reportPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        PageInfo pageInfo = createPageInfo(currentPage, (int) reportPage.getTotalElements());

        return AdminReportListResponse.builder()
                .reportList(reportList)
                .pageInfo(pageInfo)
                .build();
    }

    /**
     * 신고 상태 변경 + 답변 등록
     */
    @Override
    @Transactional
    public void updateReportStatus(Long reportNo, String status, String answer) {
        log.info("신고 상태 변경 - reportNo: {}, status: {}", reportNo, status);

        // 신고 조회
        Report report = findReportByReportNo(reportNo);

        // 검증
        validator.validateStatusWithAnswer(status, answer);
        validator.validateStatusChange(report.getStatus(), status);

        // 상태 변경
        report.updateStatus(status, answer);
        
        // 신고 승인 시 대상자에게 페널티 부여
        if ("RESOLVED".equals(status)) {
            try {
                adminMemberService.updatePenaltyStatus(report.getTargetUserNo(), 'Y');
                log.info("신고 대상자 페널티 부여 완료 - targetUserNo: {}", report.getTargetUserNo());
            } catch (Exception e) {
                log.warn("페널티 부여 실패 - targetUserNo: {}", report.getTargetUserNo(), e);
                // 신고 처리는 완료, 페널티 실패는 로그만
            }
        }
        
        log.info("신고 상태 변경 완료 - reportNo: {}", reportNo);
    }

    /**
     * 신고 답변만 수정
     */
    @Override
    @Transactional
    public void updateAnswer(Long reportNo, String answer) {
        log.info("신고 답변 수정 - reportNo: {}", reportNo, answer);

        // 신고 조회
        Report report = findReportByReportNo(reportNo);

        // 검증
        validator.validateAnswer(answer);

        // 답변 수정
        report.updateAnswer(answer);

        log.info("신고 답변 수정 완료 - reportNo: {}", reportNo);
    }

    /**
     * 신고 번호로 신고 조회 (공통 메서드)
     */
    private Report findReportByReportNo(Long reportNo) {
        return reportRepository.findByIdWithTag(reportNo)
                .orElseThrow(() -> new ResourceNotFoundException("신고를 찾을 수 없습니다. reportNo: " + reportNo));
    }

    /**
     * PageInfo 생성
     */
    private PageInfo createPageInfo(int currentPage, int totalCount) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setListCount(totalCount);
        pageInfo.setCurrentPage(currentPage);
        pageInfo.setBoardLimit(PAGE_SIZE);

        int pageLimit = 10; // 페이지 버튼 개수
        int maxPage = (int) Math.ceil((double) totalCount / PAGE_SIZE);
        int startPage = ((currentPage - 1) / pageLimit) * pageLimit + 1;
        int endPage = Math.min(startPage + pageLimit - 1, maxPage);

        pageInfo.setPageLimit(pageLimit);
        pageInfo.setMaxPage(maxPage);
        pageInfo.setStartPage(startPage);
        pageInfo.setEndPage(endPage);

        return pageInfo;
    }

    /**
     * Entity → DTO 변환
     */
    private AdminReportDTO convertToDTO(Report report) {
        AdminReportDTO dto = AdminReportDTO.fromEntity(report);

        // 닉네임 조회
        String reporterNickname = memberMapper.findNicknameByUserNo(report.getReporterUserNo());
        String targetNickname = memberMapper.findNicknameByUserNo(report.getTargetUserNo());

        dto.setReporterNickname(reporterNickname);
        dto.setTargetNickname(targetNickname);

        return dto;
    }
    @Override
    public AdminReportChatContext getReportChatContext(Long reportNo) {
        AdminReportChatContext context = adminReportMapper.selectReportChatContext(reportNo);
        
        if (context == null) {
            throw new ResourceNotFoundException("신고 번호 " + reportNo + "에 해당하는 채팅 내역을 찾을 수 없습니다.");
        }
        
        return context;
    }
}