package org.kh.splendy.config.assist;

import org.kh.splendy.service.GameService;
import org.kh.splendy.service.SocketService;
import org.kh.splendy.vo.UserCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public abstract class ProtocolHelper {

	@Autowired protected SocketService sock;
    @Autowired protected GameService game;

	public UserCore sender(SimpMessageHeaderAccessor head) {
		return (UserCore) head.getSessionAttributes().get("user");
	}
	public int uid(SimpMessageHeaderAccessor head) {
		return (int) head.getSessionAttributes().get("uid");
	}
	public int rid(SimpMessageHeaderAccessor head) {
		return (int) head.getSessionAttributes().get("rid");
	}
}
