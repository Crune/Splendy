package org.kh.splendy.controller;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 기본 컨트롤러
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = { "org.kh.splendy.mapper" })
@Controller
public class SplendyController {

	private static final Logger log = LoggerFactory.getLogger(SplendyController.class);
/*
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	*/
	
}
