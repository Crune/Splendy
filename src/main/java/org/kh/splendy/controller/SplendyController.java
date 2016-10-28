package org.kh.splendy.controller;

import org.kh.splendy.SampleMapper;
import org.kh.splendy.vo.TestVO;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = { "org.kh.splendy" })
@Controller
public class SplendyController {

	@Autowired
	private SampleMapper demoMapper;

	private static final Logger log = LoggerFactory.getLogger(SplendyController.class);

	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
}
