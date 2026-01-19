package com.kh.even.back.review.model.service;

import org.springframework.stereotype.Service;

import com.kh.even.back.review.model.dao.ReviewMapper;
import com.kh.even.back.review.model.dto.ReviewDetailDTO;
import com.kh.even.back.review.model.dto.ReviewSearchDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    
    private final ReviewMapper reviewMapper;
    
    @Override
    public ReviewDetailDTO getReview(Long roomNo, Long userNo) {
        ReviewSearchDTO dto = new ReviewSearchDTO();
        dto.setRoomNo(roomNo);
        dto.setUserNo(userNo);
        return reviewMapper.getByRoom(dto);
    }

}
