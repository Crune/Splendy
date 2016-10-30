package org.kh.splendy.sample;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = { "org.kh.splendy.sample" }) // xml의 namespace
@Controller
public class SampleController {

	private static final Logger log = LoggerFactory.getLogger(SampleController.class);

	@RequestMapping("/")
	public String index() {
		return "index";
	}
	

	@RequestMapping("/table")
	public String table() {
		return "table";
	}
}
