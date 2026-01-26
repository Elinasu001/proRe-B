package com.kh.even.back.like.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.even.back.like.model.dto.LikeVO;

@Mapper
public interface LikeMapper {
    int existsLike(LikeVO likeVo);
    int insertLike(LikeVO likeVo);
    int deleteLike(LikeVO likeVo);

    /**
     * 유저 권한 조회
     */
    String getUserRoleByUserNo(Long userNo);

    /**
     * 전문가 번호 존재 여부 확인
     */
    boolean existsExpertNo(Long expertNo);
}
