package org.kh.splendy.controller;

import javax.activation.DataSource;
import javax.activation.URLDataSource;
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
			String fileName = "img/unnamed.png"; // src/main/webapp 폴더

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom("yjku2323@gmail.com", "splendy");
			helper.setTo("lc5@naver.com");
			helper.setSubject("제목");
			helper.setText("합니다 <img src='cid:image'", true);

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			if (classLoader == null) {
				classLoader = this.getClass().getClassLoader();
			}
			DataSource ds = new URLDataSource(classLoader.getResource(fileName));

			helper.addInline("image", ds);
			mailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "index";
	}
}