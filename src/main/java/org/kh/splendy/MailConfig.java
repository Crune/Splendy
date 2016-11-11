package org.kh.splendy;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
/**
 * 
 * @author 진규
 *
 */
@Configuration
public class MailConfig {
	/**
	 * google smtp를 사용하기 위한 정보 세팅
	 * @return
	 */
	@Bean
	public static JavaMailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setDefaultEncoding("UTF-8"); //encoding 설정
		mailSender.setHost("smtp.gmail.com"); // smtp 서버
		mailSender.setPort(587); // smtp 포트
		mailSender.setUsername("splendy.spd@gmail.com"); // 인증ID
		mailSender.setPassword("cutarmggoskomvzk"); // google 2단계 인증 비밀번호
		Properties prop = new Properties();
		prop.setProperty("mail.smtp.auth", "true");
		prop.setProperty("mail.smtp.starttls.enable", "true");
		mailSender.setJavaMailProperties(prop);
		return mailSender;
	}

}