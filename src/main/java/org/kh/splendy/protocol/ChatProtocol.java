package org.kh.splendy.protocol;

import java.util.ArrayList;
import java.util.List;

import org.kh.splendy.assist.ProtocolHelper;
import org.kh.splendy.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
public class ChatProtocol extends ProtocolHelper {

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(ChatProtocol.class);

	@MessageMapping("/chat/prev")
	public void prevChat(SimpMessageHeaderAccessor head, int rid) throws Exception {
		UserCore sender = sender(head);

		List<Msg> msgs = msgMap.readPrevChat(rid, 31);
		List<WSChat> chats = new ArrayList<WSChat>();
		for (Msg cur : msgs) {
			WSChat curMsg = WSChat.convert(cur.getCont());
			if (curMsg.getUid() == sender.getId()) {
				curMsg.setType("me");
			} else {
				curMsg.setType("o");
			}
			chats.add(curMsg);
		}
		
		send("/chat/private/" + sender.getId() , chats);
	}
	
	@SubscribeMapping("/chat/new/{rid}")
	@SendTo("/chat/new/{rid}")
	public WSChat newChat(SimpMessageHeaderAccessor head, String message, @DestinationVariable String rid) throws Exception {
		UserCore sender = sender(head);
		
		WSChat chat = new WSChat();
		chat.setNick(sender.getNickname());
		chat.setUid(sender.getId());
		chat.setTime(getCurrentTime());
		chat.setType("o");
		chat.setCont(new Gson().fromJson(message, String.class));
		
		Msg newMsg = new Msg();
		newMsg.setRid(Integer.parseInt(rid));
		newMsg.setUid(sender.getId());
		newMsg.setType("chat.new");
		newMsg.setCont(new Gson().toJson(chat));
		msgMap.create(newMsg);
	
		return (WSChat) copy(chat);
	}

}