package org.kh.splendy.service;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public interface StreamService {

	void connectPro(WebSocketSession session);

	void disconnectPro(WebSocketSession session);

	void msgPro(WebSocketSession session, TextMessage message) throws Exception;

	String cvMsg(String type, Object cont);
	
	void sendAll(String message) throws Exception;
	void sendAll(String type, Object cont) throws Exception;
	void send(String sId, String message) throws Exception;
	void send(String sId, String type, Object cont) throws Exception;

	void auth(String sId, String message);
	void chat(String sId, String message) throws Exception;
	void request(String sId, String msg) throws Exception;
	
	void cardRequest(String sId, String msg)throws Exception;

	void sendR(String sId, String type, Object cont) throws Exception;







}
