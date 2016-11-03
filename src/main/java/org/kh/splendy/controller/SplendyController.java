package org.kh.splendy.controller;

import javax.annotation.Resource;

import org.kh.splendy.service.CardService;
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

	@RequestMapping("/")
	public String index() {
		try {
			cardServ.get(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}
}
