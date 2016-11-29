package org.kh.splendy.protocol;

import com.google.gson.Gson;
import org.kh.splendy.config.assist.ProtocolHelper;
import org.kh.splendy.service.ChatService;
import org.kh.splendy.vo.UserCore;
import org.kh.splendy.vo.WSChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatProtocol extends ProtocolHelper {

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(ChatProtocol.class);

	@Autowired ChatService chatServ;

	@MessageMapping("/chat/prev")
	public void prevChat(SimpMessageHeaderAccessor head, int rid) throws Exception {
		UserCore sender = sender(head);  //head에는 uid, rid, UserCore user 객체 하나가 들어있다.
        chatServ.readPrev(rid, sender.getId());
	}
	
	@SubscribeMapping("/chat/new/{rid}")
	@SendTo("/chat/new/{rid}")
	public WSChat newChat(SimpMessageHeaderAccessor head, String message, @DestinationVariable int rid) throws Exception {
		UserCore sender = sender(head);
		if (rid(head) == rid) {
            String msg = new Gson().fromJson(message, String.class);
            return (WSChat) sock.copy(chatServ.writeMsg(sender, rid, msg));  //구독자에게 onchat.js 의 chat_new(evt)의 evt로 리턴된다.
        } else {
            WSChat error = new WSChat();
            error.setUid(0);
            error.setTime(sock.getCurrentTime());
            error.setNick("시스템");
            error.setType("sys");
            error.setCont("참가하지 않은 방의 채팅은 허가되지 않습니다.");
			sock.send(sender.getId(), "chat", "new", error);
            return null;
        }
	}
}