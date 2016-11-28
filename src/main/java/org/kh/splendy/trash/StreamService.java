package org.kh.splendy.trash;
/*
import java.util.Map;

import org.kh.splendy.vo.GameRoom;
import org.kh.splendy.vo.WSPlayer;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
*/
public interface StreamService {
/*
	String getCwasid();
	//Map<Integer, GameRoom> getRooms();
	Map<String, WebSocketSession> getSessions();
	Map<String, WSPlayer> getWsplayers();

	void connectPro(WebSocketSession session);
	void msgPro(WebSocketSession session, TextMessage message) throws Exception;
	void close(int uid);
	void disconnectPro(WebSocketSession session);

	String cvMsg(String type, Object cont);

	void send(String sId, String message);
	void send(String sId, String type, Object cont);
	void sendAll(String type, Object cont);
	void sendWithoutSender(String sId, String type, Object cont);

	void sendChat(int rid, WSPlayer sender, String msg);

	void auth(String sId, String message) throws Exception;
	void chat(String sId, String message) throws Exception;

	String findSid(int uid);
	int findUid(String sid);
	void kick(String sid);
	void refreshConnector();

	void logToDB(String sid, String type, Object msg);
*/
}
