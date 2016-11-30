package org.kh.splendy.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
import org.kh.splendy.config.security.CustomUserDetails;
import org.kh.splendy.service.UserService;
import org.kh.splendy.vo.UserCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 * 회원가입, 로그인, 수정, 임시 비밀번호 전송, 탈퇴
 * @author 민정
 *
 */
@Controller
public class AccountController {
	
	@Autowired
	private UserService userServ;
	
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(AccountController.class);

	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpSession session) {
		if ((UserCore) session.getAttribute("user") != null) {
			return "redirect:/lobby/";
		} else {
			return "index";
		}
	}
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String indexWithMsg(@RequestParam("msg") String msg, Model model, HttpSession session) {
		if ((UserCore) session.getAttribute("user") != null) {
			return "redirect:/lobby/";
		} else {
			if(msg == null){
				msg = "";
			}
			model.addAttribute(msg);
			return "index";
		}
	}

	@RequestMapping(value = "/user/requestJoin", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody int requestJoin(@ModelAttribute("joinForm") UserCore user, HttpServletRequest request) {
		int result = -1;
		UserCore ck_user = null;

		String credent_code = RandomStringUtils.randomAlphanumeric(9);
		System.out.println(credent_code);
		
		try {
			if (user.getEmail() != null) {
				ck_user = userServ.checkEmail(user.getEmail());
		
				if (ck_user == null) {
					userServ.joinUser(user, credent_code);
					result = 1;	
				} else {
					result = 2;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping("/user/join_cert/{code}")
	public String join_cert(@PathVariable String code, HttpServletRequest request, RedirectAttributes rttr) {
		
		int credent_result = -1;
		try {
			userServ.credentUser(code);
			credent_result = 1;
		} catch (Exception e) {
			credent_result = 0;
			e.printStackTrace();
		}
		
		rttr.addFlashAttribute("msg","이메일 인증을 완료하였습니다."); // 해당 게시글의 게시판 읽어와서 설정 요망
		return "redirect:/";
	}

	@RequestMapping(value = "/user/send_pw", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody int send_pw(@RequestParam("email") String email) {
		
		int result_pw = -1;
		String new_pw = RandomStringUtils.randomAlphanumeric(9);
		
		try {
			result_pw = userServ.findPw(email, new_pw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result_pw;
	}
	
	@RequestMapping( value = "/user/login_suc", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
	public @ResponseBody String login_suc(Authentication authentication, @ModelAttribute("loginForm") UserCore user0, HttpSession session) {
				
		int isSameAccountInfo = -1;
		int isAlreadyCredent = -1;
		
		UserCore user = null;
		
		String result = null;
		String email = null;
		String password = null;

		CustomUserDetails cud = null;
		try {
			cud = (CustomUserDetails)authentication.getPrincipal();
		} finally { }
		
		try {
			if(cud != null) {
				if (authentication.getName() == null) {
					result="idFalse"; // id 틀림
				} else if(cud.getPassword() == null) {
					result="pwFalse"; // pw 틀림
				} else if(cud.isAccountNonLocked() == false) {
					result="lockAccount"; // 잠긴계정
				} else if(cud.isCredentialsNonExpired() == false) {
					result="credentFalse"; // 인증실패
				} else {
					email = cud.getUsername();
					password = cud.getPassword();
				}
			} else {
				email = user0.getEmail();
				password = user0.getPassword();
			}
			isSameAccountInfo = userServ.checkPassword(email, password);
			isAlreadyCredent = userServ.isNoneCredent(email, password);

			user = userServ.checkEmail(email);
			user.openInfo();
			if (isSameAccountInfo == 1 && isAlreadyCredent == 0) {
				session.setAttribute("user", user);
				result = "true";
			} else if (isSameAccountInfo == 1 && isAlreadyCredent == 1) {
				result = "credentFalse";
			} else if (isSameAccountInfo == 0) {
				result = "loginFalse";
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@RequestMapping("/user/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping(
			value = "/user/modify_suc",
			method = {RequestMethod.GET, RequestMethod.POST},
			produces = "application/json")
	public  @ResponseBody UserCore modify_suc(@ModelAttribute("modal_form") UserCore user0, HttpSession session) {
		
		List<UserCore> user1 = null;
		String email = ((UserCore)session.getAttribute("user")).getEmail();
				
		try {
			userServ.updateUser(user0, email);
			user1 = userServ.searchEmail(user0.getEmail());
		} catch (Exception e) {
			e.printStackTrace();
		}
		UserCore user = user1.get(0);
		return user;
	}
	
	@RequestMapping("/user/delete_suc")
	public String remove_suc(HttpSession session) {
		String email = ((UserCore)session.getAttribute("user")).getEmail();
		
		try {
			userServ.deleteUser(email);
			session.invalidate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/";
	}
	

}
