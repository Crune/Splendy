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
	/**
	 * 접속 관련 Event Method
	 * @param WebSocketSession 접속한 사용자
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info(session.getRemoteAddress().getHostName()+"님이 접속하셨습니다.");
		SessionMapper.add(session);
	}
	/**
	 * 1. Send : 클라이언트가 서버에게 메세지 보냄
	 * 2. Emit : 서버에 연결되어 있는 클라이언트들에게 메세지 보냄
	 * @param WebSocketSession 메세지를 보낸 클라이언트
	 * @param TextMessage 메세지의 내용
	 * @param message.getPayload() 사용자가 보낸 메세지
	 */
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		log.info(session.getRemoteAddress().getHostName()+" : "+message.getPayload());
		SessionMapper.sendAllForWebsocket(session,message.getPayload());
	}
	/**
	 * 클라이언트가 서버와 연결 종료
	 * @param WebSocketSession 연결을 끊은 클라이언트
	 * @param CloseStatus 연결 상태
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info(session.getRemoteAddress().getHostName()+"님이 퇴장하셨습니다.");
		SessionMapper.remove(session);
	}
}
