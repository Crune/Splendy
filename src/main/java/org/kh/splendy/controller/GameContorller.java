package org.kh.splendy.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.kh.splendy.service.CardService;
import org.kh.splendy.service.CardServiceImpl;
import org.kh.splendy.vo.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GameContorller {
	
	@Autowired
	private CardService cardServ;

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(AdminController.class);
	
	@RequestMapping("/game/{roomId}")
	public String servList(@PathVariable int roomId, Model model, HttpSession session) throws Exception {
		log.info("game service start!!!!!!!");
		
		session.setAttribute("roomId", roomId);
		
		return "game/game_main";
	}
	
	@RequestMapping("/card")
	public String cardTest(Model model) throws Exception{
		
		List<Card> level_1 = cardServ.getLevel_1();
		for(Card cd : level_1){
			System.out.println(" lev_1 : " + cd.getCode());
		}
		
		List<Card> level_2 = cardServ.getLevel_2();
		for(Card cd : level_2){
			System.out.println(" lev_2 : " + cd.getCode());
		}
		
		List<Card> level_3 = cardServ.getLevel_3();
		for(Card cd : level_3){
			System.out.println(" lev_3 : " + cd.getCode());
		}
		
		List<Card> level_noble = cardServ.getLevel_noble();
		for(Card cd : level_noble){
			System.out.println(" lev_noble : " + cd.getCode());
		}
		
		return "game/card_test";
	}
}
