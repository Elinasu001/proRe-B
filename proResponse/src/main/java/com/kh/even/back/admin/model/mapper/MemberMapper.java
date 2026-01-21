package com.kh.even.back.admin.model.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;  // ← 추가
import org.springframework.stereotype.Component;
import com.kh.even.back.member.model.vo.MemberVO;

@Mapper
@Component("adminMemberMapper")
public interface MemberMapper {
    
    List<MemberVO> getMemberList(
        @Param("startRow") int startRow,    // ← 추가
        @Param("endRow") int endRow,        // ← 추가
        @Param("keyword") String keyword    // ← 추가
    );
    
    int getMemberCount(@Param("keyword") String keyword);  // ← 추가
    
    MemberVO getMemberDetail(@Param("userNo") Long userNo);  // ← 추가
    
    int updateMemberStatus(@Param("userNo") Long userNo, @Param("status") char status);  // ← 추가
    
    int updatePenaltyStatus(@Param("userNo") Long userNo, @Param("penaltyStatus") String penaltyStatus);  // ← 추가
    
    int updateUserRole(@Param("userNo") Long userNo, @Param("userRole") String userRole);  // ← 추가
}