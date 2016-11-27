package org.kh.splendy.trash;

import org.kh.splendy.assist.*;
import org.kh.splendy.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InLobby extends ProtocolHelper {

	@Autowired private LobbyService lobby;

	private static final Logger log = LoggerFactory.getLogger(InLobby.class);
	
	@WSReqeust public void join(String sId, String msg) {
		lobby.join(sId, msg);
	}

	@WSReqeust public void left(String sId, String msg) {
		lobby.left(sId, msg);
	}
	
	@WSReqeust public void request(String sId, String msg) {
		lobby.request(sId, msg);
	}
}
