package org.kh.splendy;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.kh.splendy.service.StreamService;
import org.kh.splendy.vo.Chat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;

/** 웹소켓 핸들러
 * @author 진규 -> 윤 */
public class SplendyHandler extends TextWebSocketHandler {
	
	private Logger log = LoggerFactory.getLogger(SplendyHandler.class);
	
	@Autowired private StreamService stream;
	
	/**
	 * 접속과 관련되어 있는 Event Method
	 * 
	 * @param WebSocketSession 접속한 사용자
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		stream.connectPro(session);
	}
	
	/**
	 * 두 가지 이벤트를 처리함.
	 * 1. Send : client -> server로 메시지를 보냄
	 * 2. Emit : server -> client로 메시지를 보냄
	 * @param WebSocketSession 메시지를 보낸 클라이언트
	 * @param TextMessage 메시지의 내용과 정보
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		stream.msgPro(session, message);
	}
	/**
	 * 클라이언트가 서버와 연결을 끊음.
	 * 
	 * @param WebSocketSession 연결을 끊은 클라이언트
	 * @param CloseStatus 연결 상태
	 */
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		stream.disconnectPro(session);
	}
}
