package org.kh.splendy.sample.chat;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
/**
 * 
 * @author 진규
 *
 */
public class MyHandler extends TextWebSocketHandler {
	
	private Logger log = LoggerFactory.getLogger(MyHandler.class);
	private List<WebSocketSession> connectedUsers;
	/*
	 * 접속 관련 Event Method
	 * @param WebSocketSession 접속한 사용자
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("연결 IP : "+session.getRemoteAddress().getHostName());
		super.afterConnectionEstablished(session);
		SessionMapper.add(session);
	}
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		log.info("");
		super.handleTextMessage(session, message);
		SessionMapper.sendAllForWebsocket(message.getPayload());
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info("");
		super.afterConnectionClosed(session, status);
		SessionMapper.remove(session);
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {		
	}
	
	public MyHandler() {
		connectedUsers = new ArrayList<WebSocketSession>();
	}
}
