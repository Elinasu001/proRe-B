package com.kh.even.back.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.even.back.exception.BusinessLogicException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

	private final JavaMailSender emailSender;
	
	/**
	 * 이메일 발송 메서드
	 * @param toEmail 수신자
	 * @param title 제목
	 * @param text 내용
	 */
	public void sendEmail(String toEmail, String title, String text) {
		
		SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
		
		try {
			emailSender.send(emailForm);
		} catch(RuntimeException e) {
			log.debug("MailService.sendEmail exception occur toEmail: {}, " + "title: {}, text : {}", toEmail, title, text);
			throw new BusinessLogicException("이메일 인증에 실패했습니다.");
		} 
	}
	
	/**
	 * 발송할 이메일 데이터 설정
	 * @param toEmail
	 * @param title
	 * @param text
	 * @return 세팅된 이메일을 SimpleMailMessage 객체 반환
	 */
	private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject(title);
		message.setText(text);
		
		return message;
	}
}
