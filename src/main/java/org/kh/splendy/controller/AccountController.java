package org.kh.splendy.controller;

import java.util.HashMap;
import java.util.List;

import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.ibatis.annotations.Param;
import org.kh.splendy.sample.SampleController;
import org.kh.splendy.service.UserService;
import org.kh.splendy.vo.UserCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 회원가입, 수정, 비밀번호 찾기, 탈퇴
 * @author 민정
 *
 */
@Controller
public class AccountController {
	
	@Autowired
	private UserService userServ;
	
	@Autowired
	private JavaMailSender mailSender;

	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SampleController.class);
	
	@RequestMapping("/user/join")
	public String join() {
		return "user/join";
	}
	
	/*@RequestMapping("/user/joined")
	public String createUser(@RequestParam("email") String email,
							@RequestParam("password") String password,
							@RequestParam("nickname") String nickname,
								HttpServletRequest request){
		UserCore user = new UserCore();
		user.setEmail(email);
		user.setPassword(password);
		user.setNickname(nickname);
		UserCore ck_user = null;
		int result = 0;
		
		try {
			ck_user = userServ.checkEmail(email);
			System.out.println("결과 : " + ck_user);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if (ck_user == null){
			try {
				userServ.join(user);
				userServ.get(user.getEmail());
				result = 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			result = 2;
		}
		System.out.println(result);
		request.setAttribute("result", result);
		return "index";
	}*/
	
	@RequestMapping(
			value = "/user/requestJoin",
			method = RequestMethod.POST,
			produces = "application/json")
	public @ResponseBody int requestJoin(HttpServletRequest request) {
		int result = -1;
		UserCore ck_user = null;
		
		String email = null;
		String password = null;
		String nickname = null;
		
		String credent_code = RandomStringUtils.randomAlphanumeric(9);
		System.out.println(credent_code);
		
		if(request.getParameter("email") != null){
			email = request.getParameter("email");
			password = request.getParameter("password");
			nickname = request.getParameter("nickname");
			UserCore user = new UserCore(); 
			user.setEmail(email);
			user.setPassword(password);
			user.setNickname(nickname);

			try {
				ck_user = userServ.checkEmail(email);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			if (ck_user == null){
				try {
					userServ.join(user);
					userServ.insertCredent(credent_code);
					try {
						String fileName = "img/unnamed.png"; // src/main/webapp 폴더

						MimeMessage message = mailSender.createMimeMessage();
						MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
						
						helper.setFrom("splendy.spd@gmail.com", "splendy");
						helper.setTo(email);
						helper.setSubject("Splendy 회원 가입 Email 인증");
						helper.setText("<img src='cid:image'> <br/> 링크를 누르면 인증이 완료됩니다. <br/> "
										+ "<a href="+"http://localhost/user/join_cert/"+credent_code+">"
										+"링크"+"</a> 로 이동해 로그인해주세요.", true);
						
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
					
					userServ.get(email);
					result = 1;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				result = 2;
			}
		}
		
		return result;	
	}
	
	@RequestMapping("/user/join_cert/{code}")
	public String join_cert(@PathVariable String code, HttpServletRequest request) {
		/**
		 * TODO 민정:메일인증 1순위 !
		 */
		System.out.println(code);
		int credent_result = -1;
		try {
			userServ.credentUser(code);
			credent_result = 1;
		} catch (Exception e) {
			credent_result = 0;
			e.printStackTrace();
		}
		
		request.setAttribute("credent_result", credent_result);
		return "index";
	}

	@RequestMapping(
			value = "/user/send_pw",
			method = RequestMethod.POST,
			produces = "application/json")
	public @ResponseBody int send_pw(@RequestParam("email") String email) {
		
		int result_pw = -1;
		/**
		 * TODO 민정:새 비밀번호 메일 전송
		 */
		String new_pw = RandomStringUtils.randomAlphanumeric(9);
		/*HashMap<String, String> map = new HashMap<String, String>();*/
		
		try {
			userServ.updatePassword(email, new_pw);
			result_pw = 1;
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

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result_pw;
	}
	
	@RequestMapping("/user/login")
	public String login() {
		return "user/login";
	}
	
	@RequestMapping(
			value = "/user/login_credent",
			method = RequestMethod.POST,
			produces = "application/json")
	public @ResponseBody int login_credent(@RequestParam("email") String email,
											@RequestParam("password") String password) {
		int credent = -1;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("email", email);
		map.put("password", password);
		
		try {
			credent = userServ.checkCredent(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(credent);
		return credent;
	}
	
	@RequestMapping("/user/login_suc")
	public String login_suc(@RequestParam("email") String email,
							@RequestParam("password") String password,
							HttpServletRequest request,
							HttpSession session) {
		int result = -1;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("email", email);
		map.put("password", password);
		int login_result = -1;
		int credent = -1;
		List<UserCore> user = null;
		
		try {
			login_result = userServ.checkPassword(map);
			credent = userServ.checkCredent(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(login_result == 1 && credent == 0){
			session.setAttribute("email", email);
			try {
				user = userServ.searchEmail(email);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		request.setAttribute("credent", credent);
		request.setAttribute("login_result", login_result);
		request.setAttribute("user", user);
		request.setAttribute("result", result);
		return "index";
	}
	
	@RequestMapping("/user/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "index";
	}
	
	@RequestMapping("/user/find")
	public String find() {
		
		return "user/findPW";
	}
	
	@RequestMapping("/user/findPW")
	public String findPW() {
		
		return "";
	}
	
	/** TODO 민정.계정: 정보수정 구현
	* 
	*/
	@RequestMapping("/user/modify")
	public String modify(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String email = (String) session.getAttribute("email");
		List<UserCore> user = null;
		try {
			user = userServ.searchEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("user", user);
		return "user/modify";
	}
	
	@RequestMapping("/user/modify_suc")
	public String modify_suc(@RequestParam("email") String email,
								@RequestParam("password") String password,
								@RequestParam("nickname") String nickname,
								HttpServletRequest request) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("email", email);
		map.put("password", password);
		map.put("nickname", nickname);
		List<UserCore> user = null;
		try {
			userServ.updateUser(map);
			user = userServ.searchEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("user", user);
		return "user/modify_success";
	}
	
	@RequestMapping("/user/delete")
	public String remove(HttpServletRequest request) {
		List<UserCore> user = null;
		HttpSession session = request.getSession();
		String email = (String) session.getAttribute("email");
		try {
			user = userServ.searchEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("user", user);
		return "user/delete";
	}
	
	@RequestMapping("/user/delete_suc")
	public String remove_suc(HttpSession session) {
		String email = (String)session.getAttribute("email");
		
		try {
			userServ.deleteUser(email);
			session.invalidate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "index";
	}
	

}
