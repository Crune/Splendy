package org.kh.splendy.controller;

import javax.servlet.http.HttpSession;

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

@Controller
@SessionAttributes("rooms")
@RequestMapping("lobby")
/** TODO 윤.로비: 게임 로비 컨트롤러 구현
 * 게임 참여 및 타 메뉴로 이동할 수 있는 로비 구현
 * @author 최윤 **/
public class LobbyController {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(LobbyController.class);

	@Autowired private StreamService stream;
	
	@Autowired private LobbyService serv;

	@ModelAttribute("profile")
	/** 뷰어(JSP)에서 profile을 요청했을때 선언되지 않을 경우 반환할 값
	 * @return 프로필 초기값 전송 */
	public UserProfile defaultProfile() {
		UserProfile profile = new UserProfile();
		return profile;
	}

	/** 로비 첫 화면 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String list(Model model, HttpSession session) {
		/** 윤.로비: 로비 입장 화면 구현
		 * - 세션에서 유저ID 불러옴
		 * - 플레이어 정보에 계정 인증코드 입력
		 */
		UserCore user = (UserCore) session.getAttribute("user");
		user = serv.initPlayer(user); // 플레이어 인증 정보 생성
		session.setAttribute("user", user);
		return "lobby";
	}

	@RequestMapping(value = "/getAuthCode", method = RequestMethod.GET)
	public @ResponseBody Auth getAuth(HttpSession session) {
		String user_id = session.getAttribute("user_id").toString();
		int uid = Integer.parseInt(user_id);
		return serv.getAuth(uid);
	}
}
