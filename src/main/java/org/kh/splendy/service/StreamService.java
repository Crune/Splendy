package org.kh.splendy.service;

import org.kh.splendy.vo.Msg;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public interface StreamService {

	void connectPro(WebSocketSession session);

	void disconnectPro(WebSocketSession session);

	void msgPro(WebSocketSession session, TextMessage message);

	void sendAll(Msg message);

	void send(String sId, Msg message);

	void sendEmotion(String sId, Msg message);

	void sendWhisper(String sId, Msg message);
	
	void shake(String sId, Msg message);


}
