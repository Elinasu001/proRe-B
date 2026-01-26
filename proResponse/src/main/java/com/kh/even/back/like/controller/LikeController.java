package com.kh.even.back.like.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.even.back.auth.model.vo.CustomUserDetails;
import com.kh.even.back.common.ResponseData;
import com.kh.even.back.like.model.service.LikeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@Slf4j
public class LikeController {

    private final LikeService likeService;


    @PostMapping("/{expertNo}")
	public ResponseEntity<ResponseData<Map<String, Object>>> toggleLike(
			@PathVariable("expertNo") Long expertNo,
			@AuthenticationPrincipal CustomUserDetails user) {
		boolean isLiked = likeService.toggleLike(expertNo, user.getUserNo());
		return ResponseData.ok(Map.of("isLiked", isLiked), "전문가 좋아요 성공");
	}
    

}
