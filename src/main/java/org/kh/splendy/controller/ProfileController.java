package org.kh.splendy.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.kh.splendy.mapper.UserMapper;
import org.kh.splendy.mapper.UserProfileMapper;
import org.kh.splendy.service.UserProfileService;
import org.kh.splendy.service.UserService;
import org.kh.splendy.vo.UserCore;
import org.kh.splendy.vo.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProfileController {
	@Autowired UserProfileService profServ;
	@Autowired UserProfileMapper profMap;
	@Autowired UserService userServ;
	@Autowired UserMapper userMap;
	
	
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
	
	@RequestMapping("/rank")
	public String rank(Model model){
		List<UserProfile> profList = profServ.getProfAll();
		for(int i = 0; i < profList.size(); i++){
			int id = profList.get(i).getUserId();
			UserCore user = userServ.selectOne(id);
			String nickname = user.getNickname();
			profList.get(i).setNickname(nickname);
			/*//승패가 정해졌을 때 코딩 다시해야함 
			int win = profList.get(i).getWin();
			int lose = profList.get(i).getLose();
			int rate = profList.get(i).getRate();
			
			int correctRate = (rate + (win*20) - (lose*20));
			profServ.updateRate(id, correctRate);
			profList.get(i).setRate(correctRate);*/
		}
		model.addAttribute("profList", profList);
		return "lobby";
	}
}
