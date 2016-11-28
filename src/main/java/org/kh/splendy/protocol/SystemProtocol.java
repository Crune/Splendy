package org.kh.splendy.protocol;

import org.kh.splendy.vo.WSMsg;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemProtocol {
	
	@MessageMapping("/system/help")
	public void help(SimpMessageHeaderAccessor headerAccessor, String msg) {
		
	}

	@PreAuthorize("hasRole('admin')")
	@SendTo("/notice/everyone")
	public WSMsg notice(SimpMessageHeaderAccessor headerAccessor, WSMsg message) throws Exception {
		return new WSMsg("notice."+message.getType(), message.getCont());
	}
}
