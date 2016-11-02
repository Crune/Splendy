package org.kh.splendy.sample.chat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
	public static synchronized void add(WebSocketSession cSession) {
		websocketSessionMap.put(cSession.getId(), cSession);
		//info.put(, cSession.getId());
		/**
		 *  TODO
		 *  일단 세션에 cSession.getId() 값 저장
		 *  누가 들어왔는지 확인되면 <게임방, 세션ID> 저장
		 *  누군지 모를경우  
		 */
		
		for(Map.Entry<String, WebSocketSession> em : websocketSessionMap.entrySet()){
			try {
				WebSocketSession session = em.getValue();
				if(session.getUri().equals(cSession.getUri())){
					if(session.isOpen()){
						session.sendMessage(new TextMessage(cSession.getRemoteAddress().getHostName()+"님이 입장하셨습니다."));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 접속을 종료한 클라이언트의 정보 제거
	 * @param session 접속을 종료한 클라이언트
	 */
	public static synchronized void remove(WebSocketSession session) {
		websocketSessionMap.remove(session.getId());
	}
	/**
	 * 메세지 전송
	 * @param cSession 메세지를 보낸 클라이언트
	 * @param message 사용자가 보낸 메세지
	 * 자신이 보낸 메세지는 me : 를 붙이고 아니면 보낸 사람의 아이피를 붙여서 전송
	 */
	public static void sendAllForWebsocket(WebSocketSession cSession,String message) {
		
		for(Map.Entry<String, WebSocketSession> em : websocketSessionMap.entrySet()){
				try {
					WebSocketSession session = em.getValue();
					if(session.getUri().equals(cSession.getUri())){
						if(session.isOpen()){
							synchronized (session) {
								if(!session.getId().equals(cSession.getId())){
									session.sendMessage(new TextMessage(cSession.getRemoteAddress().getHostName()+" : "+message));
								} else { session.sendMessage(new TextMessage("me : "+message)); }
							}
						}
					}	
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * TODO
	 * 해당 방 안에 있는 사용자를 검색해서 그 사용자의 세션 ID에 해당하는 세션에만 메시지 전송
	 * @param cSession
	 * @param roomId
	 * @param message
	 */
	public static void sendToRoom(WebSocketSession cSession, int roomId, String message) {
		
	}
		
}
