package org.kh.splendy.controller;

import javax.servlet.http.HttpSession;

import org.kh.splendy.service.UserService;
import org.kh.splendy.vo.UserCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


/**
 * 로그인 후 프로필 정보 전달
 * 
 * @author 민정
 *
 */

@Controller
public class FaceController {
	@Autowired
	private UserService userServ;
	
	private Facebook facebook;
	private ConnectionRepository connectionRepository;

	public FaceController(Facebook facebook, ConnectionRepository connectionRepository) {
		this.facebook = facebook;
		this.connectionRepository = connectionRepository;
	}

	@RequestMapping("/user/facebook")
	public String helloFacebook(Model model, HttpSession session) throws Exception {
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
