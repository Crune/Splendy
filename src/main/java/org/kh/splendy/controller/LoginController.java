package org.kh.splendy.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
import org.kh.splendy.service.UserService;
import org.kh.splendy.vo.UserCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 로그인 페이지로 이동
 * @author 민정
 *	
 */
@Controller
public class LoginController {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private UserService userServ;
	
	String storedState;
	
	@RequestMapping(
			value = "/user/google",
			method = {RequestMethod.GET, RequestMethod.POST},
			produces = "application/json")
	public @ResponseBody String google_logined(@ModelAttribute("googleForm") UserCore user, HttpSession session) {
		/**
		 * 소셜로그인 로그아웃
		 */
		String email = "G"+user.getEmail();
		user.setEmail(email);
		user.setPassword(RandomStringUtils.randomAlphanumeric(9));
		try {
			UserCore searchUser = userServ.checkEmail(email);
			if(searchUser == null) { //최초로 소셜로그인을 통해 접속할 때
				userServ.createUser(user);
			}
			user = userServ.checkEmail(email);
			user.openInfo();
			session.setAttribute("user", user);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	@RequestMapping(
			value = "/naver_loginPro",
			method = {RequestMethod.POST})
	public @ResponseBody String naverLoginPro(@RequestParam("email") String emailParam, 
									@RequestParam("nickname") String nicknameParam,HttpSession session){
		UserCore user = new UserCore();				
		user.setEmail("N" + emailParam.toLowerCase() + "N");
		user.setNickname(nicknameParam);
		String email = user.getEmail();
		user.setPassword(RandomStringUtils.randomAlphanumeric(9));
		try{
			UserCore searchUser = userServ.checkEmail(email);
			if(searchUser == null) { //최초로 소셜로그인을 통해 접속할 때
				userServ.createUser(user);
			}
			user = userServ.checkEmail(email);
			user.openInfo();
			session.setAttribute("user", user);
		} catch (Exception e){
			e.printStackTrace();
		}
		return "success";
	}
	
	/** FIXME 민정아 지워라 */
	@RequestMapping("/login/naver_loginPro")
	public String naverLoginPro(HttpServletRequest request){
		
		return "user/naver_loginPro";
	}

	@RequestMapping(
			value = "/user/facebook",
			method = RequestMethod.POST,
			produces = "application/json")
	public @ResponseBody String helloFacebook(@RequestParam("email") String email, @RequestParam("nickname") String nickname,
													HttpSession session) throws Exception {
		String text = "/";
		System.out.println("Name : "+ nickname);
		
		UserCore user = new UserCore();
		user.setEmail("F"+email);
		user.setNickname(nickname);
		user.setPassword(RandomStringUtils.randomAlphanumeric(9));
		try {
			UserCore searchUser = userServ.checkEmail(user.getEmail());
			if(searchUser == null) { //최초로 소셜로그인을 통해 접속할 때
				userServ.createUser(user);
			}
			user = userServ.checkEmail(user.getEmail());
			user.openInfo();
			session.setAttribute("user", user);
			text = "lobby/";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return text;
	}

}
