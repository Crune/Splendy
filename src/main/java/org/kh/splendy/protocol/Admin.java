package org.kh.splendy.protocol;

import java.security.Principal;
import java.util.Map;

import org.kh.splendy.vo.WSMsg;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Admin {
	
	@PreAuthorize("hasRole('admin')")
	@MessageMapping("/admin")
	public void executeTrade(WSMsg msg, Principal principal) {
		
	}
	
	@MessageMapping("/help")
	@SendTo("/notice/everyone")
	public WSMsg greeting(SimpMessageHeaderAccessor headerAccessor, WSMsg message) throws Exception {
		System.out.println("sdfsdf");
		String sessionId = headerAccessor.getSessionId(); // Session ID
		
		@SuppressWarnings("unused")
		Map<String, Object> attrs = headerAccessor.getSessionAttributes();
		
		Thread.sleep(1000); // simulated delay
		return new WSMsg("rst","success - "+sessionId);
	}
}
