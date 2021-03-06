package org.kh.splendy.service;

import org.apache.ibatis.annotations.Param;
import org.kh.splendy.config.aop.SplendyAdvice;
import org.kh.splendy.mapper.UserMapper;
import org.kh.splendy.vo.UserCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
@EnableTransactionManagement
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMap;
	
	@Autowired
	private JavaMailSender mailSender;

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	private final RestTemplate restTemplate;

	public UserServiceImpl(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}
	
	@Override
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
		int num = 0;
		num = userMap.checkPassword(email, password);
		return num;
	}

	@Override
	public List<UserCore> searchEmail(String email) throws Exception {
		List<UserCore> list = userMap.searchEmail(email);
		return list;
	}

	@Override
	public void updatePassword(String email, String password) throws Exception {
		userMap.updatePassword(email, password);
	}
	
	@Override
	public void updateNickname(String email, String nickname) throws Exception {
		userMap.updateNickname(email, nickname);
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
	public int isNoneCredent(String email, String password) throws Exception {
		//String encryptPw = SplendyAdvice.getEncSHA256(password);
		int check = userMap.checkCredent(email, password);
		return check;
	}

	@Override
	public void credentUser(String code) throws Exception {
		userMap.credentUser(code);
		/**
		 * TODO 민정 : REG_CODE 지우기. 얘 일회용
		 */
	}
	
	@Override
	public List<UserCore> selectAll() throws Exception {
		List<UserCore> list = userMap.selectAll();
		return list;
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
	
	@Override @Transactional @Async
	public void joinUser(UserCore user, String credent_code) throws Exception {
		userMap.createUser(user);
		userMap.updateCredent(user.getEmail(), credent_code);
		sendEmail(user, credent_code);
	}

	@Override @Transactional
	public int findPw(String email, String new_pw) throws Exception {
		int result_pw = -1;
		UserCore user = null;
		user = checkEmail(email);
		if(user != null){
			String encryptPw = SplendyAdvice.getEncSHA256(new_pw);
			updatePassword(email, encryptPw);
			sendPw(email, new_pw);
			result_pw = 1;
		}
		return result_pw;
	}
	
	@Override @Transactional
	public void adminMF(UserCore user) throws Exception {
		String password = user.getPassword();
		if(password == null){
			userMap.adminMF(user);
		} else {
			userMap.adminPM(user);
		}
	}

	@Override @Transactional
	public void updateUser(UserCore user, String email) throws Exception {
		String password = user.getPassword();
		String nickname = user.getNickname();
		
		if(password != null){
			if(!password.isEmpty()){
				updatePassword(email, password);
			}
		}
		if(nickname != null){
			if(!nickname.isEmpty()){
				updateNickname(email, nickname);
			}
		}	
	}

	@Override
	public String selectPW(String email) throws Exception {
		String pw = userMap.selectPW(email);
		return pw;
	}

	@Override
	public void createUser(UserCore user) throws Exception {
		userMap.createUser(user);
	}
	

	@Override
	public UserCore selectOne(int id) {
		UserCore user = userMap.read(id);
		return user;
	}

	@Override
	public int serchID(String email) throws Exception {
		int id = userMap.serchID(email);
		return id;
	}
	
}