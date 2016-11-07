package org.kh.splendy.controller;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MailController {

	@Autowired
	private JavaMailSender mailSender;
	
	
	@RequestMapping(value = "/mail.do")
	public String sendMail() {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message);
			messageHelper.setTo("doov1@naver.com");
			messageHelper.setText("합니다");
			messageHelper.setFrom("yjku2323@gmail.com");
			messageHelper.setSubject("테스트");	// 메일제목은 생략이 가능하다
			mailSender.send(message);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return "index";
	}
}