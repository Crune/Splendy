package org.kh.splendy.controller;

import javax.servlet.http.HttpSession;

import org.kh.splendy.mapper.UserProfileMapper;
import org.kh.splendy.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProfileController {
	@Autowired UserProfileService profServ;
	@Autowired UserProfileMapper profMap;
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(AccountController.class);
	
	@RequestMapping(
			value = "/prof/iconChange")
	public @ResponseBody String iconChangePro(@ModelAttribute("icon") String icon, HttpSession session){
		try{
			profServ.updateIcon(icon, session);
		} catch(Exception e){
			e.printStackTrace();
		}
		return icon;
	}
	
	@RequestMapping("/prof/iconModify")
	public @ResponseBody String iconModifyPro(HttpSession session){
		String icon = "";
		try{
			icon = profServ.getIcon(session);
		} catch(Exception e){
			e.printStackTrace();
		}
		return icon;
	}
}
