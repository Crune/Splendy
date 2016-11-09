package org.kh.splendy.controller;

import org.kh.splendy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {
	
	@Autowired
	private UserService userServ;
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@RequestMapping("/admin")
	public String index() {
		return "admin/index";
	}
}
