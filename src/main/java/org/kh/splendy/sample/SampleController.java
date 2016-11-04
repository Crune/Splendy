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
@MapperScan(basePackages = { "org.kh.splendy.mapper" })
@Controller
public class SampleController {


	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SampleController.class);
	/*
	@Autowired
	private CardService cardServ;
	
	@Autowired
	private UserService userServ;
	 */
	@RequestMapping("/")
	public String index() {
		/*
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
		*/
		return "index";
	}

	@RequestMapping("/s/")
	public String indexs() {
		return "sample";
	}
	
}
