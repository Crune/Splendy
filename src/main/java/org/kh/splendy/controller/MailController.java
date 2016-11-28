package org.kh.splendy.controller;

import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 
 * @author 진규
 *
 */
@Controller
public class MailController {

	@Autowired
	private MailSender mailSender;

	@RequestMapping(value = "/mail.do")
	public String sendMail() {
		try {
			String fileName = "img/unnamed.png"; // src/main/webapp 폴더
			
			MimeMessage message = ((JavaMailSender) mailSender).createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom("yjku2323@gmail.com", "splendy"); // 보내는 사람 주소 정확하게 입력해야 보내짐
			helper.setTo("lc5@naver.com"); // 받는사람 주소
			helper.setSubject("제목"); //제목 생략가능
			helper.setText("합니다 <img src='cid:image'", true); // 내용 + 이미지

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			if (classLoader == null) {
				classLoader = this.getClass().getClassLoader();
			}
			DataSource ds = new URLDataSource(classLoader.getResource(fileName));

			helper.addInline("image", ds);
			((JavaMailSender)mailSender).send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "index";
	}
}