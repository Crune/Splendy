package org.kh.splendy.protocol;

import java.util.List;

import org.kh.splendy.assist.ProtocolHelper;
import org.kh.splendy.assist.WSController;
import org.kh.splendy.assist.WSReqeust;
import org.kh.splendy.service.LobbyService;
import org.kh.splendy.service.LobbyServiceImpl;
import org.kh.splendy.vo.Msg;
import org.kh.splendy.vo.Room;
import org.kh.splendy.vo.WSChat;
import org.kh.splendy.vo.WSPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class LobbyP extends ProtocolHelper {

	@Autowired private LobbyService lobby;

	private static final Logger log = LoggerFactory.getLogger(LobbyP.class);
	
	@WSReqeust public void join(String sId, String msg) {
		lobby.join(sId, msg);
	}

	@WSReqeust public void left(String sId, String msg) {
		lobby.left(sId, msg);
	}
	@WSReqeust public void request(String sId, String msg) {

		log.info("테스트3dfd426");
		
		lobby.request(sId, msg);
	}
}
