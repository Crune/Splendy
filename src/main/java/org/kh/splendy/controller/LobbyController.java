package org.kh.splendy.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;
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

	@Autowired private StreamService stream;
	@Autowired private UserProfileService profServ;
	@Autowired private LobbyService serv;

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
	/** 로비 첫 화면 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String list(Model model, HttpSession session, RedirectAttributes rttr) {
		/** 윤.로비: 로비 입장 화면 구현
		 * - 세션에서 유저ID 불러옴
		 * - 플레이어 정보에 계정 인증코드 입력
		 */
		UserCore user = (UserCore) session.getAttribute("user");
		if (user == null) {
			// 로그인 정보가 없을 경우는 로그인 페이지로 이동
			rttr.addFlashAttribute("msg","로그인이 필요합니다!");
			return "redirect:/";
		} else {
			user = serv.initPlayer(user, 0); // 플레이어 인증 정보 생성
			session.setAttribute("user", user);
			int lastRoom = serv.getLastRoom(user.getId());
			if (lastRoom > 0) {
				// 게임 중 재접속시에는 해당 게임방으로 이동. 
				return "redirect:/game/"+lastRoom;
			} else {
				profServ.refreshUserProf(session);
				return "lobby";				
			}
		}
	}

	@RequestMapping(value = "/getAuthCode", method = RequestMethod.GET)
	public @ResponseBody Auth getAuth(HttpSession session) {
		UserCore user = (UserCore) session.getAttribute("user");
		return serv.getAuth(user.getId());
	}

	@RequestMapping(value = "/out", method = RequestMethod.GET)
	public String outRoom(HttpSession session) {
		UserCore user = (UserCore) session.getAttribute("user");
		serv.left("", user.getId()+"");
		return "redirect:/";
	}

	@RequestMapping(value = "/room_new", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody int requestCreate(@ModelAttribute Room reqRoom, HttpSession session) {
		int result = -1;

		UserCore user = (UserCore) session.getAttribute("user");
		result = serv.createRoom(reqRoom, user);

		return result;
	}
}
