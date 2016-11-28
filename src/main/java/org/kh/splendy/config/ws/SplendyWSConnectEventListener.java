package org.kh.splendy.config.ws;

import org.kh.splendy.service.SocketService;
import org.kh.splendy.vo.UserCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Component
public class SplendyWSConnectEventListener implements ApplicationListener<SessionConnectEvent> {

	private static final Logger log = LoggerFactory.getLogger(SplendyWSConnectEventListener.class);

    @Autowired private SocketService sock;

	@Override
	public void onApplicationEvent(SessionConnectEvent event) {
		StompHeaderAccessor head = StompHeaderAccessor.wrap(event.getMessage());
		UserCore sender = (UserCore) head.getSessionAttributes().get("user");
		if (sender != null) {
            sock.putConnectors(sender);
            log.info("웹소켓 접속: " + sender);
        }
	}
}
