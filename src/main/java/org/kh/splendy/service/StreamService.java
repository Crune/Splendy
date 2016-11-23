package org.kh.splendy.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.kh.splendy.vo.GameRoom;
import org.kh.splendy.vo.UserCore;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public interface StreamService {

	void connectPro(WebSocketSession session);

	void close(int uid);
	void disconnectPro(WebSocketSession session);

	void msgPro(WebSocketSession session, TextMessage message) throws Exception;

	String cvMsg(String type, Object cont);
	
	void send(String sId, String message) throws Exception;
	void send(String sId, String type, Object cont) throws Exception;
	void sendAll(String type, Object cont);
	void sendWithoutSender(String sId, String type, Object cont);

	void auth(String sId, String message) throws Exception;
	void chat(String sId, String message) throws Exception;
	void request(String sId, String msg) throws Exception;
	
	void cardRequest(String sId, String msg)throws Exception;

	void sendR(String sId, String type, Object cont) throws Exception;

	void cardCount(String sId, String msg) throws Exception;

	void refreshConnector();

	void join(String sId, String msg);

	void left(String sId, String msg);

	String findSid(int uid);

	int findUid(String sid);

	void kick(String sid);

	void createRoom(int rst);

}
