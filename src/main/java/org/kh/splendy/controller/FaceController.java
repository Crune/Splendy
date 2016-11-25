package org.kh.splendy.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.kh.splendy.service.UserService;
import org.kh.splendy.vo.UserCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
		user.setPassword("0");
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
