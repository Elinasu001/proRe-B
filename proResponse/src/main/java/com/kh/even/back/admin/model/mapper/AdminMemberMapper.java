package com.kh.even.back.admin.model.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.kh.even.back.member.model.vo.MemberVO;
import com.kh.even.back.admin.model.vo.AdminVO;

@Mapper
public interface AdminMemberMapper {

    /**
     * 관리자 정보 조회 (ROOT 여부 확인용)
     */
    @Select("SELECT * FROM TB_ADMIN WHERE USER_NO = #{userNo}")
    AdminVO getAdminByUserNo(Long userNo);

    /**
     * 회원 목록 조회 (페이징 + 검색)
     * 검색: 닉네임 또는 회원번호
     */
    @Select("""
        <script>
        SELECT * FROM (
            SELECT A.*, ROWNUM RN FROM (
                SELECT * FROM TB_MEMBER
                <where>
                    <if test="keyword != null and keyword != ''">
                        <choose>
                            <when test="keyword matches '^[0-9]+$'">
                                USER_NO = #{keyword}
                            </when>
                            <otherwise>
                                NICKNAME LIKE '%' || #{keyword} || '%'
                            </otherwise>
                        </choose>
                    </if>
                </where>
                ORDER BY CREATE_DATE DESC
            ) A
        )
        WHERE RN BETWEEN #{startRow} AND #{endRow}
        </script>
    """)
    List<MemberVO> getMemberList(
        @Param("startRow") int startRow,
        @Param("endRow") int endRow,
        @Param("keyword") String keyword
    );

    /**
     * 회원 전체 개수 (검색 포함)
     * 검색: 닉네임 또는 회원번호
     */
    @Select("""
        <script>
        SELECT COUNT(*) 
        FROM TB_MEMBER
        <if test="keyword != null and keyword != ''">
        WHERE
            <choose>
                <when test="keyword matches '^[0-9]+$'">
                    USER_NO = #{keyword}
                </when>
                <otherwise>
                    NICKNAME LIKE '%' || #{keyword} || '%'
                </otherwise>
            </choose>
        </if>
        </script>
    """)
    int getMemberCount(String keyword);

    /**
     * 회원 상세 조회
     */
    @Select("SELECT * FROM TB_MEMBER WHERE USER_NO = #{userNo}")
    MemberVO getMemberDetail(Long userNo);

    /**
     * 회원 상태 변경
     */
    @Update("UPDATE TB_MEMBER SET STATUS = #{status}, UPDATE_DATE = SYSDATE WHERE USER_NO = #{userNo}")
    int updateMemberStatus(@Param("userNo") Long userNo, @Param("status") char status);

    /**
     * 징계 상태 변경
     */
    @Update("UPDATE TB_MEMBER SET PENALTY_STATUS = #{penaltyStatus}, UPDATE_DATE = SYSDATE WHERE USER_NO = #{userNo}")
    int updatePenaltyStatus(@Param("userNo") Long userNo, @Param("penaltyStatus") char penaltyStatus);

    /**
     * 권한 변경
     */
    @Update("UPDATE TB_MEMBER SET USER_ROLE = #{userRole}, UPDATE_DATE = SYSDATE WHERE USER_NO = #{userNo}")
    int updateUserRole(@Param("userNo") Long userNo, @Param("userRole") String userRole);
}