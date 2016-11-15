package org.kh.splendy.sample;

import org.kh.splendy.service.CardService;
import org.kh.splendy.service.UserService;
import org.kh.splendy.vo.UserCore;
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
@MapperScan(basePackages = { "org.kh.splendy.mapper" })
@Controller
public class SampleController {


	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SampleController.class);
	
	@Autowired
	private CardService cardServ;
	
	@Autowired
	private UserService userServ;


	@RequestMapping("/s/")
	public String indexs() {
		return "sample";
	}
	
}
