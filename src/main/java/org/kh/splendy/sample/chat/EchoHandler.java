package org.kh.splendy.sample.chat;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class EchoHandler extends TextWebSocketHandler {
	private Logger logger = LoggerFactory.getLogger(EchoHandler.class);
	private List<WebSocketSession> connectedUsers;
	//서버에 연결한 사용자들 저장
	public EchoHandler() {
		connectedUsers = new ArrayList<WebSocketSession>();
	}
	/*	클라이언트가 서버와 연결 종료
	 * 
	 * @param webSocketSession 연결을 끊은 클라이언트
	 * @param CloseStatus 연결 상태 (확인 필요)	 
	 */
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		logger.info(session.getId() + "님 접속 종료");
		connectedUsers.remove(session);
		super.afterConnectionClosed(session, status);
	}
	
	/*	접속관련 Event Method
	 * @param WebSocketSession 접속한 사용자
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info(session.getId() + "님 접속");
		logger.info("연결 IP : "+session.getRemoteAddress().getHostName());
		connectedUsers.add(session);
		super.afterConnectionEstablished(session);
	}
	
	/*	2가지 이벤트 처리
	 * 	1.Send : 클라이언트가 서버에게 메시지를 보냄
	 * 	2.Emit : 서버에 연결되어 있는 클라이언트들에게 메시지 보냄
	 * 	@param WebSocketSession 메시지를 보낸 클라이언트
	 *  @param TextMessage 메시지의 내용 
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// Payload = 사용자가 보낸 메시지
		logger.info(session.getId() +"님이 메시지 전송" +message.getPayload());
		
		for(WebSocketSession webSocketSession : connectedUsers){
		//보낸 사용자는 받지 않기 위한 조건문
			if(!session.getId().equals(webSocketSession)){
				webSocketSession.sendMessage(new TextMessage(message.getPayload()));
			}
		}
		super.handleTextMessage(session, message);
	}


	
}
