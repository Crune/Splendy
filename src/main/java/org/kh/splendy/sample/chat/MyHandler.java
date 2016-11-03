package org.kh.splendy.sample.chat;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.kh.splendy.vo.Chat;
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
	 * 접속과 관련되어 있는 Event Method
	 * 
	 * @param WebSocketSession 접속한 사용자
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info(session.getId() + "님이 접속했습니다.");
		log.info("연결 IP : " + session.getRemoteAddress().getHostName() );
		SessionMapper.add(session);
	}
	/**
	 * 두 가지 이벤트를 처리함.
	 * 1. Send : client -> server로 메시지를 보냄
	 * 2. Emit : server -> all client로 메시지를 보냄
	 * 
	 * @param WebSocketSession 메시지를 보낸 클라이언트
	 * @param TextMessage 메시지의 내용
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		log.info(session.getId() + " -> " + message.getPayload());
		SessionMapper.sendAllForWebsocket(session, message);
	}
	/**
	 * 클라이언트가 서버와 연결을 끊음.
	 * 
	 * @param WebSocketSession 연결을 끊은 클라이언트
	 * @param CloseStatus 연결 상태 (확인 필요함..)
	 */
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info(session.getId() + "님이 퇴장했습니다.");
		SessionMapper.remove(session);
	}
}
