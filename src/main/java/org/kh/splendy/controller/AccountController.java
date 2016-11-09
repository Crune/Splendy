package org.kh.splendy.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.kh.splendy.sample.SampleController;
import org.kh.splendy.service.UserService;
import org.kh.splendy.vo.UserCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	public @ResponseBody int requestJoin(@RequestParam("email") String email,
										@RequestParam("password") String password,
										@RequestParam("nickname") String nickname) {
		
		log.info(email);
		UserCore user = new UserCore(); 
		user.setEmail(email);
		user.setPassword(password);
		user.setNickname(nickname);

		int result = -1;
		UserCore ck_user = null;
		
		try {
			ck_user = userServ.checkEmail(email);
			System.out.println("결과 : " + ck_user);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if (ck_user == null){
			try {
				userServ.join(user);
				userServ.get(email);
				result = 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			result = 2;
		}
		return result;
		
	}
	
	@RequestMapping("/user/login")
	public String login() {
		return "user/login";
	}
	
	@RequestMapping("/user/login_suc")
	public String login_suc(@RequestParam("email") String email,
							@RequestParam("password") String password,
							HttpServletRequest request,
							HttpSession session) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("email", email);
		map.put("password", password);
		int login_result = 0;
		/*HttpSession session = request.getSession();*/
		List<UserCore> user = null;
		
		try {
			login_result = userServ.checkPassword(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(login_result == 1){
			session.setAttribute("email", email);
			session.setAttribute("password", password);
			try {
				user = userServ.searchEmail(email);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		
		request.setAttribute("user", user);
		return "index";
	}
	
	@RequestMapping("/user/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		
		return "index";
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
		String password = (String)session.getAttribute("password");
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("email", email);
		map.put("password", password);
		
		try {
			userServ.deleteUser(map);
			session.invalidate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "index";
	}
	

}
