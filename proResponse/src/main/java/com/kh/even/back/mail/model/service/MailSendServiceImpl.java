package com.kh.even.back.mail.model.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.kh.even.back.exception.EmailAuthFailException;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSendServiceImpl implements MailSendService {
	
	private final JavaMailSender mailSender;
	
	@Override
	public void sendVerificationEmail(String toEmail, String title, String code, long ttlSeconds) {
		
		try {
			// 1) MIME 메세지 생성 (HTML 메일 가능)
			MimeMessage message = mailSender.createMimeMessage();
			
			// 2) Helper로 메세지 조립
			// 		- false : 첨부파일 없음 (필요하면 true로 바꾸고 multipart 처리)
			// 		- UTF-8 : 한글 깨짐 방지
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
			
			// 3) 수신자/제목 설정
			helper.setTo(toEmail);
			helper.setSubject(title);
			
			// 4) HTML 본문 작성
			String html =
	                "<div style='font-family: Arial, sans-serif; line-height: 1.6;'>"
	              + "  <h2 style='margin:0 0 8px;'>이메일 인증번호 안내</h2>"
	              + "  <p style='margin:0 0 16px;'>아래 인증번호를 입력해주세요.</p>"
	              + "  <div style='display:inline-block; padding:12px 16px; border:1px solid #ddd; border-radius:8px;'>"
	              + "    <span style='font-size:28px; font-weight:700; letter-spacing:6px;'>" + code + "</span>"
	              + "  </div>"
	              + "  <p style='color:#666; margin:16px 0 0;'>유효시간: " + (ttlSeconds / 60) + "분</p>"
	              + "</div>";
			
			// 5) 본문 세팅 (true = HTML)
			helper.setText(html, true);
			
			// 6) 전송
			mailSender.send(message);
			
		} catch(Exception e) {
			throw new EmailAuthFailException("이메일 인증번호 발송에 실패했습니다.");
		}
	
	}

}
