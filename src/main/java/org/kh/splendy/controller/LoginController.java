package org.kh.splendy.controller;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.kh.splendy.service.UserService;
import org.kh.splendy.vo.UserCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
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
	
	@RequestMapping("/login/facebook")
	public String facebook(RedirectAttributes rttr) {
		
		String scope = "user_posts";
		
		rttr.addFlashAttribute("scope", scope);
		/*return "redirect:/connect/facebook";*/
		return "user/facebookLogin";
	
	}
	
	@RequestMapping(
			value = "/facebook",
			method = RequestMethod.POST)
	public class HelloController {
		private Facebook facebook;
		private ConnectionRepository connectionRepository;
		
		public HelloController(Facebook facebook, ConnectionRepository connectionRepository) {
			this.facebook = facebook;
			this.connectionRepository = connectionRepository;
			log.info("sadfas");
		}
		
		@PostMapping
		public String helloFacebook(HttpSession session, RedirectAttributes rttr) throws Exception {
			if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
				
				String scope = "user_posts";
				rttr.addFlashAttribute("scope", scope);
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
		
		return "user/naver_login";
	}
	
	@RequestMapping("/login/naver_loginPro")
	public String naverLoginPro(HttpServletRequest request){
		
		return "user/naver_loginPro";
	}


}
