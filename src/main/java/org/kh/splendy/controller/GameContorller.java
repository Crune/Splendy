package org.kh.splendy.controller;

import javax.servlet.http.HttpSession;

import org.kh.splendy.service.PlayerService;
import org.kh.splendy.vo.UserCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GameContorller {

	@Autowired private PlayerService player;

	private static final Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@RequestMapping("/game/{rid}")
	public String servList(@PathVariable int rid, Model model, HttpSession session, RedirectAttributes rttr) throws Exception {
		UserCore user = (UserCore) session.getAttribute("user");
		if (user == null) {
			// 로그인 정보가 없을 경우는 로그인 페이지로 이동
			rttr.addFlashAttribute("msg","로그인이 필요합니다!");
			return "redirect:/";
		} else {
			int lastRoom = player.getLastRoomAndInit(user.getId());
			session.setAttribute("rid", lastRoom);
			if (lastRoom > 0) {
				return "game/main";
			} else {
				// 게임 중이 아닐경우 로비로 이동
				return "redirect:/lobby/";
			}
		}
	}
	
	@RequestMapping("/canvas")
	public String canvas() {
		return "canvas";
	}
}
