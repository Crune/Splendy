package org.kh.splendy.controller;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.kh.splendy.service.UserService;
import org.kh.splendy.vo.UserCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 로그인 페이지로 이동
 * @author 민정
 *	
 */
@Controller
public class LoginController {
	
	@Autowired
	private UserService userServ;
	
	String storedState;
	
	@RequestMapping("/user/facebook")
	public class FaceController {
		private Facebook facebook;
		private ConnectionRepository connectionRepository;

		public FaceController(Facebook facebook, ConnectionRepository connectionRepository) {
			this.facebook = facebook;
			this.connectionRepository = connectionRepository;
		}

		@GetMapping
		public String helloFacebook(HttpSession session) throws Exception {
			if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
				return "redirect:/connect/facebook";
			}

			UserCore user = new UserCore();
			user.setEmail("F"+facebook.userOperations().getUserProfile().getId());
			user.setNickname(facebook.userOperations().getUserProfile().getName());
			user.setPassword("0");
			try {
				UserCore searchUser = userServ.checkEmail(user.getEmail());
				if(searchUser == null) { //최초로 소셜로그인을 통해 접속할 때
					userServ.createUser(user);
				}
				user = userServ.checkEmail(user.getEmail());
				user.openInfo();
				session.setAttribute("user", user);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return "user/hello";
		} 
	}
	
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
		user.setPassword("0");
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
	
	@RequestMapping("login/naver")
	public String naver(HttpServletRequest request){
		String state = generateState();
		// 세션 또는 별도의 저장 공간에 상태 토큰을 저장
		request.setAttribute("state", state);
		
		System.out.println(state);
		

		return "user/naver_login";
	}
	
	@RequestMapping("/login/naver_loginPro")
	public String naverLoginPro(HttpServletRequest request){
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		request.setAttribute("code", code);
		request.setAttribute("state", state);
		return "user/naver_loginPro";
	}

	public String generateState()
	{
	    SecureRandom random = new SecureRandom();
	    return new BigInteger(130, random).toString(32);
	}
}
