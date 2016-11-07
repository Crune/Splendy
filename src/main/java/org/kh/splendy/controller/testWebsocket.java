package org.kh.splendy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class testWebsocket {
	
	@RequestMapping("testws")
	public String testWebsocket(){
			
		return "user/testWebsocket";
	}

}
