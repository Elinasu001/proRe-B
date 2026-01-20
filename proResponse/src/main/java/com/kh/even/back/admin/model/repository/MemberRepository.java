package com.kh.even.back.admin.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kh.even.back.member.model.vo.MemberVO;

public interface MemberRepository extends JpaRepository<MemberVO, Long> {
    // 기본 CRUD는 자동 제공
    // 추가 메서드만 정의
}