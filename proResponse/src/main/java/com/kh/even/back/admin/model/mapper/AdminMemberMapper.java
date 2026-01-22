package com.kh.even.back.admin.model.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.kh.even.back.member.model.vo.MemberVO;

@Mapper
public interface AdminMemberMapper {

    /**
     * 회원 목록 조회 (페이징 + 검색)
     */
    List<MemberVO> getMemberList(
        @Param("startRow") int startRow,
        @Param("endRow") int endRow,
        @Param("keyword") String keyword
    );

    /**
     * 회원 전체 개수 (검색 포함)
     */
    int getMemberCount(String keyword);  // ✅ 단일 파라미터 - @Param 없음

    /**
     * 회원 상세 조회
     */
    MemberVO getMemberDetail(Long userNo);  // ✅ 단일 파라미터 - @Param 없음

    /**
     * 회원 상태 변경
     */
    int updateMemberStatus(
        @Param("userNo") Long userNo,
        @Param("status") String status  // ✅ char → String 변경
    );

    /**
     * 징계 상태 변경
     */
    int updatePenaltyStatus(
        @Param("userNo") Long userNo,
        @Param("penaltyStatus") String penaltyStatus
    );

    /**
     * 권한 변경
     */
    int updateUserRole(
        @Param("userNo") Long userNo,
        @Param("userRole") String userRole
    );
}