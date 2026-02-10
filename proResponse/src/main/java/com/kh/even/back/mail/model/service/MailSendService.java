package com.kh.even.back.mail.model.service;

public interface MailSendService {
	
	/**
	 * 인증 메일 발송(HTML)
	 * @param toEmail 수신자
	 * @param title 제목
	 * @param code 인증코드
	 * @param ttlSeconds 유효시간(초) - 메일 본문 표시용
	 */
	void sendVerificationEmail(String toEmail, String title, String code, long ttlSeconds);
	
	/**
	 * 임시비밀번호 발송(HTML)
	 * @param toEmail 수신자
	 * @param title 제목
	 * @param tempPwd 임시비밀번호
	 */
	void sendTempPassword(String toEmail, String title, String tempPwd);
}
