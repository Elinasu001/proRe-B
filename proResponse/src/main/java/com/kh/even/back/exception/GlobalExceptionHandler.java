package com.kh.even.back.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kh.even.back.common.ResponseData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	/* ===================== 인증 / 인가 ===================== */
	@ExceptionHandler(CustomAuthenticationException.class)
	public ResponseEntity<ResponseData<Object>> handleAuth(CustomAuthenticationException e) {
		log.warn("인증 실패: {}", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	// 작성자만 수정/삭제 같은 권한 예외
	@ExceptionHandler(CustomAuthorizationException.class)
	public ResponseEntity<ResponseData<Object>> handleAuthorization(CustomAuthorizationException e) {
		log.warn("인가 실패: {}", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ResponseData<Object>> handleAccessDenied(AccessDeniedException e) {
		log.warn("접근 권한 거부: {}", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(LoginException.class)
	public ResponseEntity<ResponseData<Object>> handleLogin(LoginException e) {
	    log.warn("로그인 실패: {}", e.getMessage());
	    return ResponseData.failure(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(ChatException.class)
	public ResponseEntity<ResponseData<Object>> handleChatException(ChatException e) {
	    log.warn("채팅 오류: {}", e.getMessage());
	    return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ReviewException.class)
	public ResponseEntity<ResponseData<Object>> handleReviewException(ReviewException e) {
		log.warn("리뷰 오류: {}", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	/* ===================== 회원 / 예약 / 차량 / 게시판 도메인 예외 ===================== */

	/* ===================== 공통 Runtime / Exception ===================== */
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ResponseData<Object>> handlerIllegalState(IllegalStateException e) {
		log.warn("잘못된 상태: {}", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ResponseData<Object>> handleRuntimeException(RuntimeException e) {
		log.error("런타임 오류 발생: {} ", e.getMessage());
		// 별도 핸들러 안 만든 RuntimeException 들
		return ResponseData.failure(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseData<Object>> handleException(Exception e) {
		log.error("알 수 없는 서버 오류 발생: {} ", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ResponseData<Object>> handleuserNameNotFound(UsernameNotFoundException e) {

		log.error("유저 이름 못찾음: {}", e.getMessage());

		return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ResponseData<Object>> handleNotFound(NotFoundException e) {

		log.error("조회 결과를 못찾음 : {}", e.getMessage());

		return ResponseData.failure(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(EmailDuplicateException.class)
	public ResponseEntity<ResponseData<Object>> handleEmailDuplicateException(EmailDuplicateException e) {
		
		log.error("이메일 중복 : {}", e.getMessage());
		
		return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(InvalidFileException.class)
	public ResponseEntity<ResponseData<Object>> handleInvalidFileExceptiona(InvalidFileException e) {
		
		log.error("파일 4개 이상 업로드 : {}", e.getMessage());
		
		return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(CustomServerException.class)
	public ResponseEntity<ResponseData<Object>> handleServerException(CustomServerException e) {
		
		log.error("서버 오류 발생 : {}", e.getMessage());
		
		return ResponseData.failure(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
