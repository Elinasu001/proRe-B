package com.kh.even.back.admin.model.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.kh.even.back.member.model.vo.MemberVO;

@Mapper
public interface AdminMemberMapper {

    // ✅ 파라미터 3개 - @Param 필요
    List<MemberVO> getMemberList(
        @Param("startRow") int startRow,
        @Param("endRow") int endRow,
        @Param("keyword") String keyword
    );

    // ✅ 파라미터 1개 - @Param 제거
    int getMemberCount(String keyword);

    // ✅ 파라미터 1개 - @Param 제거
    MemberVO getMemberDetail(Long userNo);

    // ✅ 파라미터 2개 - @Param 필요
    int updateMemberStatus(
        @Param("userNo") Long userNo, 
        @Param("status") char status
    );

    // ✅ 파라미터 2개 - @Param 필요
    int updatePenaltyStatus(
        @Param("userNo") Long userNo, 
        @Param("penaltyStatus") String penaltyStatus
    );

    // ✅ 파라미터 2개 - @Param 필요
    int updateUserRole(
        @Param("userNo") Long userNo, 
        @Param("userRole") String userRole
    );
}