package org.kh.splendy.sample.chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.kh.splendy.vo.Chat;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
/**
 * 
 * @author 진규
 * MyHandler에서 입력받은 값을 처리하는 곳
 */
public class SessionMapper {
	private static List<WebSocketSession> connectedUsers = new ArrayList<WebSocketSession>();
	private HashMap<String,String> roomInfo;
	/**
	 * 서버에 접속한 클라이언트의 목록 작성
	 * @param session 접속한 클라이언트
	 */
	public static synchronized void add(WebSocketSession session) {
		connectedUsers.add(session);
		//info.put(, session.getId());
		/**
		 *  TODO 진규 : 사용자 - 소켓세션 연동
		 *  일단 세션에 cSession.getId() 값 저장
		 *  누가 들어왔는지 확인되면 <게임방, 세션ID> 저장
		 */
	}
	/**
	 * 접속을 종료한 클라이언트의 정보 제거
	 * @param session 접속을 종료한 클라이언트
	 */
	public static synchronized void remove(WebSocketSession session) {
		connectedUsers.remove(session);
		try {
			for (WebSocketSession webSocketSession : connectedUsers) {
				if(webSocketSession.isOpen()){
					synchronized (webSocketSession) {
						webSocketSession.sendMessage(new TextMessage(session.getId() + "님이 나가셨습니다."));
					}
				}
			}
		} catch (IOException e) { e.printStackTrace(); }
	}
	/**
	 * 메세지 전송
	 * 
	 * @param session 메세지를 보낸 클라이언트
	 * @param message 사용자가 보낸 메세지와 정보
	 */
	public static void sendAllForWebsocket(WebSocketSession session,TextMessage message) {
		// convert는 MessageVO의 메소드
		// 파라미터로 넘어온 JSON 타입의 데이터를 자바 객체로 넣어주는 역활을 한다.
		Chat messageVO = Chat.convert(message.getPayload());
		try {
			for (WebSocketSession webSocketSession : connectedUsers) {
				if(webSocketSession.isOpen()){
					synchronized (webSocketSession) {
						if (!session.getId().equals(webSocketSession.getId())) {
							webSocketSession.sendMessage(new TextMessage(session.getId()
							+ " -> " + messageVO.getMessage()));
						}
					}
				}
			}
		} catch (IOException e) { e.printStackTrace(); }
	}

	/**
	 * TODO 진규 : 게임방별 전송 구현
	 * 해당 방 안에 있는 사용자를 검색해서 그 사용자의 세션 ID에 해당하는 세션에만 메시지 전송
	 * @param roomId
	 * @param message
	 */
	public static void sendToRoom(int roomId, String message) {
		
	}
	/**
	 * 귓속말을 보내는 메서드
	 * 
	 * @param session 귓속말을 보낸 클라이언트
	 * @param message 클라이언트가 보낸 메세지와 정보
	 */
	public static void sendToWhisper(WebSocketSession session,TextMessage message) {
		// convert는 MessageVO의 메소드
		// 파라미터로 넘어온 JSON 타입의 데이터를 자바 객체로 넣어주는 역활을 한다.
		Chat messageVO = Chat.convert(message.getPayload());
		String hostName = "";
		try {
			for (WebSocketSession webSocketSession : connectedUsers) {
				hostName = webSocketSession.getId();
				if(webSocketSession.isOpen()){
					synchronized (webSocketSession) {
						if ( messageVO.getTo().equals(hostName) ) {
							webSocketSession.sendMessage( new TextMessage(
							"(귓속말)"
							+ session.getId() 
							+ " -> " + messageVO.getMessage()) );
							break;
						}
					}
				}
			}
		} catch (IOException e) { e.printStackTrace(); }
	}
}
