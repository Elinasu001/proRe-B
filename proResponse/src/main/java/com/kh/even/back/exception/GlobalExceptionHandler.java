package com.kh.even.back.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
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

	/* ===================== 도메인 예외 ===================== */
	
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

	@ExceptionHandler(ReportException.class)
	public ResponseEntity<ResponseData<Object>> handleReportException(ReportException e) {
		log.warn("신고 오류: {}", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(PaymentException.class)
	public ResponseEntity<ResponseData<Object>> handlePaymentException(PaymentException e) {
		log.warn("결제 오류: {}", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	/* ===================== 회원 / 예약 / 차량 / 게시판 도메인 예외 ===================== */

	/* ===================== 공통 Runtime / Exception ===================== */
	// @ExceptionHandler(IllegalStateException.class) 중복 제거: 아래 handleIllegalState만 남김

	// @ExceptionHandler(UsernameNotFoundException.class) 중복 제거: 아래 handleUsernameNotFoundException만 남김
	
	@ExceptionHandler(AlreadyReportedException.class)
	public ResponseEntity<ResponseData<Object>> handleAlreadyReportedException(AlreadyReportedException e) {
		log.warn("이미 신고됨: {}", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	/* ===================== 검증 예외 ===================== */
	
	@ExceptionHandler(EmailDuplicateException.class)
	public ResponseEntity<ResponseData<Object>> handleEmailDuplicateException(EmailDuplicateException e) {
		log.warn("이메일 중복: {}", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidFileException.class)
	public ResponseEntity<ResponseData<Object>> handleInvalidFileException(InvalidFileException e) {
		log.warn("파일 검증 실패: {}", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Validation 예외 처리
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseData<Object>> handleValidationException(MethodArgumentNotValidException e) {
	    Map<String, String> errors = new HashMap<>();
	    e.getBindingResult().getAllErrors().forEach(error -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    
	    log.warn("입력값 검증 실패: {}", errors);
	    return ResponseData.failure("입력값이 올바르지 않습니다: " + errors, HttpStatus.BAD_REQUEST);
	}
	//  추가: IllegalArgumentException
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ResponseData<Object>> handleIllegalArgumentException(IllegalArgumentException e) {
		log.warn("잘못된 인자: {}", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	/* ===================== 조회 예외 ===================== */
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ResponseData<Object>> handleNotFound(NotFoundException e) {
		log.warn("조회 결과 없음: {}", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ResponseData<Object>> handleUsernameNotFoundException(UsernameNotFoundException e) {
		log.warn("사용자 없음: {}", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	/* ===================== 공통 예외 ===================== */
	
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ResponseData<Object>> handleIllegalState(IllegalStateException e) {
		log.warn("잘못된 상태: {}", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CustomServerException.class)
	public ResponseEntity<ResponseData<Object>> handleServerException(CustomServerException e) {
		log.error("서버 오류 발생: {}", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(BusinessLogicException.class)
	public ResponseEntity<ResponseData<Object>> handleBusinessLogicException(BusinessLogicException e) {
		
		log.error("비즈니스 로직 오류 발생 : {}", e.getMessage());
		
		return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ResponseData<Object>> handleRuntimeException(RuntimeException e) {
		log.error("런타임 오류 발생", e);
		return ResponseData.failure("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseData<Object>> handleException(Exception e) {
		log.error("예상치 못한 오류 발생", e);
		return ResponseData.failure("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(EmailAuthFailException.class)
	public ResponseEntity<ResponseData<Object>> EmailAuthFailException(EmailAuthFailException e) {
		log.error("이메일 인증 오류 발생 : {}", e.getMessage());
		return ResponseData.failure("이메일 인증 오류가 발생했습니다.", HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(EmailAuthCooltimeException.class)
	public ResponseEntity<ResponseData<Object>> EmailAuthCooltimeException(EmailAuthCooltimeException e) {
		log.error("인증번호 재발송 오류 발생 : {}", e.getMessage());
		return ResponseData.failure("30초 후 다시 시도해주세요.", HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ResponseData<Object>> handleEntityNotFoundException(EntityNotFoundException e) {
		log.error("유효하지 않은 요청 번호 : {} ", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(PhoneDuplicateException.class)
	public ResponseEntity<ResponseData<Object>> handlePhoneDuplicateException(PhoneDuplicateException e) {
		log.error("연락처 업데이트 오류 : {} ", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UpdateMemberException.class)
	public ResponseEntity<ResponseData<Object>> handleUpdateMemberException(UpdateMemberException e) {
		log.error("내정보 수정 오류 : {} ", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ExpertRegisterException.class)
	public ResponseEntity<ResponseData<Object>> handleExpertRegisterException(ExpertRegisterException e) {
		log.error("전문가 등록 오류 : {}", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ExpertNotFoundException.class)
	public ResponseEntity<ResponseData<Object>> handleExpertNotFoundException(ExpertNotFoundException e) {
		log.error("전문가 조회 오류 : {}", e.getMessage());
		return ResponseData.failure(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}