package com.kh.even.back.like.model.service;

public interface LikeService {

    /**
	 * 좋아요 토글
	 * @param expertNo
	 * @param userNo
	 */
	boolean toggleLike(Long expertNo, Long userNo);

}
