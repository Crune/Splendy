package org.kh.splendy.config.ws;

import org.kh.splendy.service.PlayerService;
import org.kh.splendy.service.SocketService;
import org.kh.splendy.vo.UserCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class SplendyWSDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {

	private static final Logger log = LoggerFactory.getLogger(SplendyWSDisconnectEventListener.class);

	@Autowired private PlayerService player;

	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		StompHeaderAccessor head = StompHeaderAccessor.wrap(event.getMessage());
        try {
            int uid = (int) head.getSessionAttributes().get("uid");
            int rid = (int) head.getSessionAttributes().get("rid");
            player.disconnect(uid, rid);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
