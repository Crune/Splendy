package org.kh.splendy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GameContorller {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@RequestMapping("/game")
	public String servList(Model model) throws Exception {
		log.info("game service start!!!!!!!");
		
		return "game/game_main";
	}
}
