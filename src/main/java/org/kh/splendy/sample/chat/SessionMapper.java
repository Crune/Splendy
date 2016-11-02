package org.kh.splendy.sample.chat;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
/**
 * 
 * @author 진규
 *
 */
public class SessionMapper {
	private static final ConcurrentHashMap<String, WebSocketSession> websocketSessionMap = new ConcurrentHashMap<String, WebSocketSession>();
	/**
	 * 서버에 접속한 클라이언트의 목록
	 * @param session 접속한 클라이언트
	 */
	public static synchronized void add(WebSocketSession cSession) {
		websocketSessionMap.put(cSession.getId(), cSession);
		for(Map.Entry<String, WebSocketSession> em : websocketSessionMap.entrySet()){
			try {
				WebSocketSession session = em.getValue();
					if(session.isOpen()){
						session.sendMessage(new TextMessage(cSession.getRemoteAddress().getHostName()+"님이 입장하셨습니다."));
					}
			} catch (IOException e) {
			}
		}
	}
	/**
	 * 접속을 종료한 클라이언트의 정보를 제거
	 * @param session 접속을 종료한 클라이언트
	 */
	public static synchronized void remove(WebSocketSession session) {
		websocketSessionMap.remove(session.getId());
	}
	
	public static void sendAllForWebsocket(WebSocketSession cSession,String message) {
		for(Map.Entry<String, WebSocketSession> em : websocketSessionMap.entrySet()){
				try {
					WebSocketSession session = em.getValue();
						if(session.isOpen()){
							synchronized (session) {
								if(!session.getRemoteAddress().equals(cSession.getRemoteAddress())){
									session.sendMessage(new TextMessage(cSession.getRemoteAddress().getHostName()+" : "+message));
								} else { session.sendMessage(new TextMessage("나 : "+message)); }
							}
						}
				} catch (IOException e) {
				}
		}
	}	
}
