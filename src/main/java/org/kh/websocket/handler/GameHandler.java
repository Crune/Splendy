package org.kh.websocket.handler;

import java.io.IOException;
import java.util.HashSet;

import org.kh.splendy.paint.PaintHandler;
import org.kh.splendy.vo.GameLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class GameHandler extends TextWebSocketHandler {
	private Logger logger = LoggerFactory.getLogger(GameHandler.class);

	private HashSet<WebSocketSession> connectedUsers;

	public GameHandler() {
		connectedUsers = new HashSet<WebSocketSession>();
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		if(connectedUsers.size() > 4){
			logger.info(session.getId() + "님이 풀방이라 못들어옴");
			return;
		}
		logger.info(session.getId() + "님이 접속했습니다.");
		logger.info("연결 IP :" + session.getRemoteAddress().getHostName());
		
		connectedUsers.add(session);
	}

	/*
	 * 두 가지 이벤트를 처리함. 1. send : client -> server로 메시지를 보냄 2. Emit : server ->
	 * all client로 메시지를 보냄
	 * 
	 * @param WebSocketSesion 메시지를 보낸 클라이언트
	 * 
	 * @param TextMessage 메시지의 내용
	 */

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		GameLog gLog = GameLog.convert(message.getPayload());
		
		try{
			for (WebSocketSession webSocketSession : connectedUsers) {	
					if(gLog.getMode().equals("getJewel")){
						webSocketSession.sendMessage(new TextMessage(gLog.getScore()));
					}					
			}
		} catch (IOException e) { e.printStackTrace(); }		
	}

	/*
	 * 클라이어트가 서버와 연결 끊음.
	 * 
	 * @param WebSocketSession 연결을 끊은 클라이언트
	 * 
	 * @param CloseStatus 연결 상태 ( 확인 필요함 )
	 */

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		logger.info(session.getId() + "님이 퇴장했습니다.");
		connectedUsers.remove(session);
	}
}
