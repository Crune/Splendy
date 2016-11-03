package org.kh.splendy.sample.chat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.kh.splendy.vo.Chat;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
/**
 * 
 * @author 진규
 * MyHandler에서 입력받은 값을 처리하는 곳
 */
public class SessionMapper {
	private static final ConcurrentHashMap<String, WebSocketSession> websocketSessionMap = new ConcurrentHashMap<String, WebSocketSession>();
	private static HashMap<String,String> info = new HashMap<String,String>();
	/**
	 * 서버에 접속한 클라이언트의 목록 작성
	 * @param cSession 접속한 클라이언트
	 */
	public static synchronized void add(WebSocketSession session) {
		websocketSessionMap.put(session.getId(), session);
		//info.put(, cSession.getId());
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
		try {
			for (Map.Entry<String, WebSocketSession> em : websocketSessionMap.entrySet()) {
				WebSocketSession se = em.getValue();
				if ( !session.getId().equals(se.getId()) ) {
					session.sendMessage( 
					new TextMessage(session.getId() + "퇴장했습니다.") );
					websocketSessionMap.remove(session.getId());
				}
			}
		} catch (IOException e) { e.printStackTrace(); }
	}
	/**
	 * 메세지 전송
	 * @param cSession 메세지를 보낸 클라이언트
	 * @param message 사용자가 보낸 메세지
	 * 자신이 보낸 메세지는 me : 를 붙이고 아니면 보낸 사람의 아이피를 붙여서 전송
	 */
	public static void sendAllForWebsocket(WebSocketSession session,TextMessage message) {
		// convertMessage는 MessageVO의 메소드
		// 파라미터로 넘어온 JSON 타입의 데이터를 자바 객체로 넣어주는 역활을 한다.
		Chat messageVO = Chat.convert(message.getPayload());
		String hostName = "";
		try {
			for (Map.Entry<String, WebSocketSession> em : websocketSessionMap.entrySet()) {
				WebSocketSession se = em.getValue();
				if ( messageVO.getType().equals("all")) {
					if ( !session.getId().equals(se.getId()) ) {
						session.sendMessage( new TextMessage(session.getId()  
								+ " -> " + messageVO.getMessage()) );
					}
				}
				// 귓속말 전송
				else {
					hostName = se.getId();
						if ( messageVO.getTo().equals(hostName) ) {
							session.sendMessage( new TextMessage(
							"<span style='color:red;'>"
							+ session.getId() 
							+ " -> " + messageVO.getMessage()
							+ "</span>") );
							break;
						}
				}
			}
		} catch (IOException e) { e.printStackTrace(); }
	}

	/**
	 * TODO 진규 : 게임방별 전송 구현
	 * 해당 방 안에 있는 사용자를 검색해서 그 사용자의 세션 ID에 해당하는 세션에만 메시지 전송
	 * @param cSession
	 * @param roomId
	 * @param message
	 */
	public static void sendToRoom(WebSocketSession cSession, int roomId, String message) {
		
	}
		
}
