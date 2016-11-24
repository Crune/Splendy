package org.kh.splendy.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.kh.splendy.service.CardService;
import org.kh.splendy.service.CardServiceImpl;
import org.kh.splendy.service.LobbyService;
import org.kh.splendy.vo.Card;
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

	@Autowired private LobbyService lobbyServ;

	private static final Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@RequestMapping("/game/{roomId}")
	public String servList(@PathVariable int roomId, Model model, HttpSession session, RedirectAttributes rttr) throws Exception {
		log.info("game start / rid: "+roomId);

		session.setAttribute("roomId", roomId);
		UserCore user = (UserCore) session.getAttribute("user");
		if (user == null) {
			// 로그인 정보가 없을 경우는 로그인 페이지로 이동
			rttr.addFlashAttribute("msg","로그인이 필요합니다!");
			return "redirect:/";
		} else {
			user = lobbyServ.initPlayer(user, roomId); // 플레이어 인증 정보 생성
			session.setAttribute("user", user);
			int lastRoom = lobbyServ.getLastRoom(user.getId());
			if (lastRoom > 0) {
				return "game/main";
			} else {
				// 게임 중이 아닐경우 로비로 이동
				return "redirect:/lobby/";
			}
		}
	}
}
