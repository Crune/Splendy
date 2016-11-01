package org.kh.splendy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 로그인 페이지로 이동
 * @author 민정
 *	
 */
@Controller
public class LoginController {
	@RequestMapping("/login/facebook")
	public String index() {
		return "user/facebookLogin";
	}
}
