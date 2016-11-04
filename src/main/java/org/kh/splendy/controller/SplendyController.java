package org.kh.splendy.controller;

import org.kh.splendy.service.*;
import org.kh.splendy.vo.UserCore;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

/**
 * 기본 컨트롤러
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = { "org.kh.splendy.mapper" })
@Controller
public class SplendyController {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SplendyController.class);
	
	@Autowired
	private CardService cardServ;
	
	@Autowired
	private UserService userServ;

	@RequestMapping("/")
	public String index() {
		try {
			cardServ.get(1);
			userServ.get("admin@spd.cu.cc");
			
			UserCore newUser = new UserCore();
			newUser.setEmail("rune.choe@gmail.com");
			newUser.setNickname("최윤");
			newUser.setPassword("1234");
			userServ.join(newUser);
			userServ.get("rune.choe@gmail.com");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}
}
