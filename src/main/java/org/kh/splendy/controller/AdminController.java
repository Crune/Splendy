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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
		List<PropInDB> list = adminServ.readAll();
		model.addAttribute("list", list);
		return "admin/servList";
	}
	
	@RequestMapping(
			value = "/admin/servState",
			method = RequestMethod.POST,
			produces = "application/json")
	public @ResponseBody void saveState(@RequestParam("key") String key, @RequestParam("value") String value) {
		PropInDB prop = new PropInDB();
		prop.setKey(key);
		prop.setValue(value);
		try {
			adminServ.update(prop);
		} catch(Exception e) { e.printStackTrace(); }
	}
	
}
