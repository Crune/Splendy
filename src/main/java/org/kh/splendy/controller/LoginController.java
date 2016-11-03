package org.kh.splendy.controller;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 로그인 페이지로 이동
 * @author 민정
 *	
 */
@Controller
public class LoginController {
	
	String storedState;
	
	@RequestMapping("/login/facebook")
	public String facebook() {
		return "user/facebookLogin";
	}
	@RequestMapping("/login/google")
	public String google() {
		return "user/googleLogin";
	}
	@RequestMapping("/main/google")
	public String google_login() {
		return "user/hello";
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
