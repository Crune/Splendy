package org.kh.splendy.controller;

import java.util.List;


import org.kh.splendy.service.AdminService;
import org.kh.splendy.service.ServService;
import org.kh.splendy.service.UserService;
import org.kh.splendy.vo.PropInDB;
import org.kh.splendy.vo.Role;
import org.kh.splendy.vo.UserCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@Autowired
	private ServService servServ;
	
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
		List<PropInDB> list = servServ.readAll();
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
			servServ.update(prop);
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	@RequestMapping("/userList")
	public String userList(Model model) throws Exception {
		log.info("admin access userlist");
		List<UserCore> list = userServ.selectAll();
		model.addAttribute("list", list);
		return "admin/userList";
	}
	
	@RequestMapping("/admin/userState/{email}/")
	public @ResponseBody UserCore userState(@PathVariable String email) throws Exception {
		log.info("admin serch user name : "+email);
		UserCore user = userServ.selectOne(email);
		return user;
	}
	
	@RequestMapping(
			value = "/admin/user_modify",
			method = RequestMethod.POST,
			produces = "application/json")
	public @ResponseBody void saveState(@RequestParam("id") int id, @RequestParam("nickname") String nickname
										,@RequestParam("enabled") int enabled,@RequestParam("notLocked") int notLocked
										,@RequestParam("notExpired") int notExpired,@RequestParam("notCredential") int notCredential) {
		UserCore user = new UserCore();
		user.setId(id);
		user.setNickname(nickname);
		user.setEnabled(enabled);
		user.setNotLocked(notLocked);
		user.setNotExpired(notExpired);
		user.setNotCredential(notCredential);
		try {
			log.info("admin modify : "+id);
			userServ.adminMF(user);
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	@RequestMapping("/adminList")
	public String adminList(Model model) throws Exception {
		log.info("admin access adminlist");
		List<Role> list = adminServ.readAll();
		model.addAttribute("list", list);
		return "admin/adminList";
	}
}
