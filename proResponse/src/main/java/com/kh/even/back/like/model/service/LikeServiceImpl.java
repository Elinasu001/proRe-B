package com.kh.even.back.like.model.service;

import org.springframework.stereotype.Service;

import com.kh.even.back.exception.ReviewException;
import com.kh.even.back.like.model.dao.LikeMapper;
import com.kh.even.back.like.model.dto.LikeVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

	private final LikeMapper likeMapper;

        
    /**
	 * 좋아요 토글 (등록/삭제)
	 * @param expertNo 전문가 번호
	 * @param userNo 사용자 번호
	 */
	@Override
	public boolean toggleLike(Long expertNo, Long userNo) {

        assertRoleUser(userNo);

		if (!likeMapper.existsExpertNo(expertNo)) {
		    throw new ReviewException("존재하지 않는 전문가입니다.");
		}

		LikeVO likeVo = LikeVO.builder()
			.expertNo(expertNo)
			.userNo(userNo)
			.build();

		int exists = likeMapper.existsLike(likeVo);
		if (exists > 0) {
			likeMapper.deleteLike(likeVo);
			return false; // 좋아요 해제됨
		} else {
			likeMapper.insertLike(likeVo);
			return true; // 좋아요 됨
		}
	}


    // 회원 권한 체크 공통 메서드
    private void assertRoleUser(Long userNo) {
		if (userNo == null) {
			throw new ReviewException("로그인 후 이용해 주세요.");
		}
        String userRole = likeMapper.getUserRoleByUserNo(userNo);
        if (!"ROLE_USER".equals(userRole)) {
            throw new ReviewException("일반회원만 접근할 수 있습니다.");
        }
    }

}
