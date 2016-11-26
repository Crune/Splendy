package org.kh.splendy.trash;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class PaintHandler extends TextWebSocketHandler {
	private Logger logger = LoggerFactory.getLogger(PaintHandler.class);
	/*
	 * 서버에 연결한 사용자들을 저장하는 리스트.
	 */
	private HashSet<WebSocketSession> connectedUsers;

	public PaintHandler() {
		connectedUsers = new HashSet<WebSocketSession>();
	}
	
	/*
	 * 접속과 관련되어 있는 Event Method
	 * 
	 * @param WebSocketSession 접속한 사용자
	 */

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		logger.info(session.getId() + "님이 접속했습니다.");
		logger.info("연결 IP :" + session.getRemoteAddress().getHostName());
		connectedUsers.add(session);
		
		
		for(WebSocketSession webSocketSession : connectedUsers){
			webSocketSession.sendMessage( new TextMessage("{ \"mode\" : \"fill\", \"color\" : \"#FFFFFF\" }"));			
		}
	}
	
	/*
	 * 두 가지 이벤트를 처리함.
	 * 1. send : client -> server로 메시지를 보냄
	 * 2. Emit : server -> all client로 메시지를 보냄
	 * @param WebSocketSesion 메시지를 보낸 클라이언트
	 * @param TextMessage 메시지의 내용
	 */
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
		for(WebSocketSession webSocketSession : connectedUsers){
			if(!webSocketSession.getId().equals(session.getId())){
				webSocketSession.sendMessage(new TextMessage(message.getPayload()));
			}
		}
	}
	
	/*
	 * 클라이어트가 서버와 연결 끊음.
	 * @param WebSocketSession 연결을 끊은 클라이언트
	 * @param CloseStatus 연결 상태 ( 확인 필요함 )
	 */
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		logger.info(session.getId() + "님이 퇴장했습니다.");
		connectedUsers.remove(session);
	}
}
