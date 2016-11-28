package org.kh.splendy.config.ws;

import org.kh.splendy.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class SplendyWSSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

	private static final Logger log = LoggerFactory.getLogger(SplendyWSSubscribeEventListener.class);

	private static final String privateChannel = "/private/";

    @Autowired private SocketService sock;

	@Override 
	public void onApplicationEvent(SessionSubscribeEvent event) {
		StompHeaderAccessor head = StompHeaderAccessor.wrap(event.getMessage());
		log.info(head.toString());

		String sessionId = head.getSessionId();
		String dest = head.getDestination();
		
		if (dest.contains(privateChannel)) {
			String[] mayUid = dest.split("/");
			int uid = (int) head.getSessionAttributes().get("uid");
			if (Integer.parseInt(mayUid[3]) == uid && mayUid.length == 4) {
				log.info("개인 채널 구독완료! - "+uid);
			} else {
				sock.kick(sessionId, dest);
			}
		}
	}

}
