package org.kh.splendy.controller;

import java.util.ArrayList;
import java.util.List;

import org.kh.splendy.service.*;
import org.kh.splendy.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("rooms")
@RequestMapping("lobby")
/** TODO 윤.로비: 게임 로비 컨트롤러 구현
 * 게임 참여 및 타 메뉴로 이동할 수 있는 로비 구현
 * @author 최윤 **/
public class LobbyController {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(LobbyController.class);

	@Autowired
	private UserService userServ;
	
	@Autowired
	private RoomService roomServ;

	@ModelAttribute("rooms")
	/** 뷰어(JSP)에서 room을 요청했을때 선언되지 않을 경우 반환할 값
	 * @return 게시글 읽기 실패했다는 id가 -1인 결과를 반환 */
	public List<Room> defaultRooms() {
		List<Room> rooms = new ArrayList<Room>();
		Room room = new Room();
		room.setId(-1);
		room.setTitle("무제");
		room.setInfo("게임방");
		room.setPlayerLimits(0);
		room.setState(-1);
		rooms.add(room);
		return rooms;
	}
	
	/** 로비 첫 화면 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String list(Model model) {
		/** TODO 윤.로비: 로비 입장 화면 구현 */
		List<Room> rooms = roomServ.getList();
		model.addAttribute("rooms",rooms);
		
		return "lobby";
	}

	@RequestMapping(value = "/sample", method = RequestMethod.GET)
	public String sample(Model model) {
		
		return "admin/sample";
	}
}
