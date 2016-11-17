package org.kh.splendy.service;

import java.util.HashMap;
import java.util.List;

import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.internet.MimeMessage;

import org.apache.ibatis.annotations.Param;
import org.kh.splendy.mapper.*;
import org.kh.splendy.vo.*;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableTransactionManagement
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMap;
	
	@Autowired
	private JavaMailSender mailSender;

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override //???
	public UserCore get(@Param("email") String email) throws Exception {
		List<UserCore> userList = userMap.searchEmail(email);
		UserCore lastId = null;
		for (UserCore user : userList) {
			if (user.getEnabled() == 1) {
				if (lastId == null) {
					lastId = user;
				} else {
					lastId = new UserCore();
					lastId.setId(-1);
				}
			}
		}
		log.info("card info : "+lastId.toString());
		return lastId;
	}

	@Override
	public int checkPassword(String email, String password) throws Exception {
		int result = userMap.checkPassword(email, password);
		return result;
	}

	@Override
	public List<UserCore> searchEmail(String email) throws Exception {
		List<UserCore> list = userMap.searchEmail(email);
		return list;
	}

	@Override
	public void updateUser(String email, String password, String nickname) throws Exception {
		userMap.updateUser(email, password, nickname);
	}

	@Override
	public void deleteUser(String email) throws Exception {
		userMap.deleteUser(email);
	}

	@Override
	public UserCore checkEmail(String email) throws Exception {
		UserCore user = userMap.checkEmail(email);
		return user;
	}

	@Override
	public int checkCredent(String email, String password) throws Exception {
		int check = userMap.checkCredent(email, password);
		return check;
	}

	@Override
	public void credentUser(String code) throws Exception {
		userMap.credentUser(code);
	}

	@Override
	public void insertCredent(String credent_code) throws Exception {
		userMap.insertCredent(credent_code);
	}

	@Override
	public void updatePassword(String email, String password) throws Exception {
		userMap.updatePassword(email, password);
	}

	@Override
	public List<UserCore> selectAll() throws Exception {
		List<UserCore> list = userMap.selectAll();
		return list;
	}

	@Override
	public UserCore selectOne(String email) throws Exception {
		UserCore user = userMap.selectOne(email);
		return user;
	}

	@Override @Async
	public void sendEmail(UserCore user, String credent_code) throws Exception {
		String fileName = "img/unnamed.png"; // src/main/webapp 폴더

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		
		helper.setFrom("splendy.spd@gmail.com", "splendy");
		helper.setTo(user.getEmail());
		helper.setSubject("Splendy 회원 가입 Email 인증");
		helper.setText("<img src='cid:image'> <br/> 링크를 누르면 인증이 완료됩니다. <br/> "
						+ "<a href="+"http://spd.cu.cc/user/join_cert/"+credent_code+">"
						+"링크"+"</a> 로 이동해 로그인해주세요.", true);
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			classLoader = this.getClass().getClassLoader();
		}
		DataSource ds = new URLDataSource(classLoader.getResource(fileName));

		helper.addInline("image", ds);
		mailSender.send(message);
	}
	
	@Override @Async
	public void sendPw(String email, String new_pw) throws Exception {
		String fileName = "img/unnamed.png"; // src/main/webapp 폴더
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		
		helper.setFrom("splendy.spd@gmail.com", "splendy");
		helper.setTo(email);
		helper.setSubject("Splendy 임시 비밀번호 전송");
		helper.setText("<img src='cid:image'> <br/> 임시비밀번호 : "+new_pw+"<br/> 임시 비밀번호로 로그인 뒤 꼭 비밀번호를 재설정해주세요.", true);
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			classLoader = this.getClass().getClassLoader();
		}
		DataSource ds = new URLDataSource(classLoader.getResource(fileName));

		helper.addInline("image", ds);
		mailSender.send(message);
	}
	
	@Override @Transactional
	public void joinUser(UserCore user, String credent_code) throws Exception {
		userMap.createUser(user);
		userMap.insertCredent(credent_code);
		sendEmail(user, credent_code);
	}

	@Override @Transactional
	public int findPw(String email, String new_pw) throws Exception {
		int result_pw = -1;
		UserCore user = null;
		user = checkEmail(email);
		if(user != null){
			updatePassword(email, new_pw);
			sendPw(email, new_pw);
			result_pw = 1;
		}
		return result_pw;
	}
}
