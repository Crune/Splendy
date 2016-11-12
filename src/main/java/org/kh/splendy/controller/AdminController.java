package org.kh.splendy.controller;

import java.util.List;

import org.kh.splendy.service.AdminService;
import org.kh.splendy.service.UserService;
import org.kh.splendy.vo.PropInDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {
	
	@Autowired
	private UserService userServ;
	
	@Autowired
	private AdminService adminServ;
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@RequestMapping("/admin")
	public String index() {
		log.info("admin access index");
		return "admin/index";
	}
	
	@RequestMapping("/servList")
	public String servList(Model model) throws Exception {
		log.info("admin access servicelist");
		List<PropInDB> list = adminServ.serchServ();
		model.addAttribute("list", list);
		return "admin/servList";
	}
	
	@RequestMapping("/servOn")
	public String servOn(@RequestParam("key") String key) throws Exception {
		adminServ.servOn(key);
		return "";
	}
	
	@RequestMapping("/servOff")
	public String servOff(@RequestParam("key") String key) throws Exception {
		adminServ.servOff(key);
		return "";
	}
}
