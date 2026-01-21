package com.kh.even.back.admin.model.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.kh.even.back.member.model.vo.MemberVO;

@Mapper  // ✅ @Component 제거
public interface AdminMemberMapper {  // ✅ 클래스명 변경
    
    List<MemberVO> getMemberList(
        @Param("startRow") int startRow,
        @Param("endRow") int endRow,
        @Param("keyword") String keyword
    );
    
    int getMemberCount(@Param("keyword") String keyword);
    
    MemberVO getMemberDetail(@Param("userNo") Long userNo);
    
    int updateMemberStatus(@Param("userNo") Long userNo, @Param("status") char status);
    
    int updatePenaltyStatus(@Param("userNo") Long userNo, @Param("penaltyStatus") String penaltyStatus);
    
    int updateUserRole(@Param("userNo") Long userNo, @Param("userRole") String userRole);
}