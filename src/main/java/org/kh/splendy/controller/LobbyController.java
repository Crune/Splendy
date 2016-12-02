package org.kh.splendy.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.kh.splendy.mapper.UserMapper;
import org.kh.splendy.mapper.UserProfileMapper;
import org.kh.splendy.service.PlayerService;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("rooms")
@RequestMapping("lobby")
/** 윤.로비: 게임 로비 컨트롤러 구현
 * 게임 참여 및 타 메뉴로 이동할 수 있는 로비 구현
 * @author 최윤 **/
public class LobbyController {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(LobbyController.class);

	@Autowired
	private PlayerService player;
	@Autowired
	private UserProfileService profServ;
	@Autowired
	UserProfileMapper profMap;
	@Autowired
	UserService userServ;
	@Autowired
	UserMapper userMap;

	@ModelAttribute("profile")
	/** 뷰어(JSP)에서 profile을 요청했을때 선언되지 않을 경우 반환할 값
	 * @return 프로필 초기값 전송 */
	public UserProfile defaultProfile() {
		UserProfile profile = new UserProfile();
		return profile;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index() {
		return "redirect:/lobby/";
	}

	/**
	 * 로비 첫 화면
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String list(Model model, HttpSession session, RedirectAttributes rttr) throws Exception{
		/** 윤.로비: 로비 입장 화면 구현
		 * - 세션에서 유저ID 불러옴
		 * - 플레이어 정보에 계정 인증코드 입력
		 */
		UserCore user = (UserCore) session.getAttribute("user");
		if (user == null) {
			// 로그인 정보가 없을 경우는 로그인 페이지로 이동
			rttr.addFlashAttribute("msg", "로그인이 필요합니다!");
			return "redirect:/";
		} else {
			//user = serv.initPlayer(user, 0); // 플레이어 인증 정보 생성
			session.setAttribute("user", user);

			int lastRoom = player.getLastRoomAndInit(user.getId());
			session.setAttribute("rid", lastRoom);
			if (lastRoom > 0) {
				// 게임 중 재접속시에는 해당 게임방으로 이동. 
				return "redirect:/game/" + lastRoom;
			} else {
				//랭킹 처리 부분
				model.addAttribute("profList", profServ.getProfAll());
				model.addAttribute("user", userServ.get(user.getEmail()));
				//랭킹 처리 부분 끝
				profServ.refreshUserProf(session);
				return "lobby";
			}
		}
	}

	@RequestMapping(value = "/out", method = RequestMethod.GET)
	public String outRoom(HttpSession session) {
		UserCore user = (UserCore) session.getAttribute("user");
		player.left(user);
		return "redirect:/";
	}
}