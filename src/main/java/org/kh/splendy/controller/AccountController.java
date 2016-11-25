package org.kh.splendy.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
import org.kh.splendy.service.UserService;
import org.kh.splendy.vo.CustomUserDetails;
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
		
		/*request.setAttribute("credent_result", credent_result);*/
		
		rttr.addFlashAttribute("msg","이메일 인증을 완료하였습니다."); // 해당 게시글의 게시판 읽어와서 설정 요망
		return "redirect:/";
	}

	@RequestMapping(
			value = "/user/send_pw",
			method = RequestMethod.POST,
			produces = "application/json")
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
	
	@RequestMapping(
			value = "/user/login_suc",
			method = {RequestMethod.GET, RequestMethod.POST},
			produces = "application/json")
	public @ResponseBody String login_suc(
				Authentication authentication,
				@ModelAttribute("loginForm") UserCore user0,
				HttpServletRequest request,HttpSession session) {
		CustomUserDetails cud = (CustomUserDetails)authentication.getPrincipal();
		int result = -1;
		
		int login_result = -1;
		int credent = -1;
		
		UserCore user = null;
		String email = cud.getUsername();
		String password = cud.getPassword();
		
		String text = null;

		System.out.println("email : " + email);
		System.out.println("password : " + password);
		
		try {
			login_result = userServ.checkPassword(email, password);
			credent = userServ.checkCredent(email, password);

			int login_result_org = userServ.checkPassword(user0.getEmail(), user0.getPassword());
			int credent_org = userServ.checkCredent(user0.getEmail(), user0.getPassword());
		
			if(login_result == 1 && credent == 0){
			
				user = userServ.checkEmail(email);
				user.openInfo();
				session.setAttribute("user", user);
				text = "true";

			} else if(login_result_org == 1 && credent_org == 0) {

				user = userServ.checkEmail(user0.getEmail());
				user.openInfo();
				session.setAttribute("user", user);
				text = "true";
				
			} else if ((login_result == 1 && credent == 1) || (login_result_org == 1 && credent_org == 1)) {
				text = "credentFalse";
				
			} else if (login_result == 0 ||login_result_org == 0) {
				text = "loginFalse";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return text;
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
