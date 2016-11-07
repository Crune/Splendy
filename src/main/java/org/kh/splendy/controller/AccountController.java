package org.kh.splendy.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.kh.splendy.service.UserService;
import org.kh.splendy.vo.UserCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * 회원가입, 수정, 비밀번호 찾기, 탈퇴
 * @author 민정
 *
 */
@Controller
public class AccountController {
	
	@Autowired
	private UserService userServ;
	
	@RequestMapping("/user/join")
	public String join() {
		return "user/join";
	}
	
	@RequestMapping("/user/joined")
	public String createUser(@RequestParam("email") String email,
							@RequestParam("password") String password,
							@RequestParam("nickname") String nickname,
								HttpServletRequest request){
		UserCore user = new UserCore();
		user.setEmail(email);
		user.setPassword(password);
		user.setNickname(nickname);
		
		try {
			userServ.join(user);
			userServ.get(user.getEmail());
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("user", user);
		
		return "user/join_success";
	}
	
	@RequestMapping("/user/login")
	public String login() {
		return "user/login";
	}
	
	@RequestMapping("/user/login_suc")
	public String login_suc(@RequestParam("email") String email,
							@RequestParam("password") String password,
							HttpServletRequest request) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("email", email);
		map.put("password", password);
		int result = 0;
		HttpSession session = request.getSession();
		List<UserCore> user = null;
		
		try {
			result = userServ.checkPassword(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result == 1){
			session.setAttribute("email", email);
			session.setAttribute("password", password);
			try {
				user = userServ.searchEmail(email);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		
		request.setAttribute("result", result);
		request.setAttribute("user", user);
		return "user/login_success";
	}
	
	@RequestMapping("/user/modify")
	public String modify(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String email = (String) session.getAttribute("email");
		
		request.setAttribute("email", email);
		return "user/modify";
	}

}
