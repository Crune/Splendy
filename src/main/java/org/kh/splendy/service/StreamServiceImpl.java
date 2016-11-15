package org.kh.splendy.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.kh.splendy.annotation.WSReqeust;
import org.kh.splendy.mapper.*;
import org.kh.splendy.vo.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;
import com.google.gson.annotations.*;

import lombok.Data;

@Service
@EnableTransactionManagement
public class StreamServiceImpl implements StreamService {
	
	private Logger log = LoggerFactory.getLogger(StreamServiceImpl.class);

	private static Map<String, WebSocketSession> sessions = new HashMap<String, WebSocketSession>();

	private static Map<String, Method> webSocketMethods = new HashMap<String, Method>();

	@Autowired private RoomMapper roomMap;
	@Autowired private PlayerMapper playerMap;
	@Autowired private UserMapper userMap;
	
	@Override //@Transactional
	public void connectPro(WebSocketSession session) {
		log.info(session.getId() + "님이 접속했습니다.");
		log.info("연결 IP : " + session.getRemoteAddress().getHostName() );
		
		sessions.put(session.getId(), session);
	}

	
	@Override
	public void disconnectPro(WebSocketSession session) {
		log.info(session.getId() + "님이 퇴장했습니다.");

		playerMap.setStateBySid(session.getId(), Player.ST_LOST);
		sessions.remove(session.getId());
	}
	
	@Data
	public static class WSMsg {

		@SerializedName("type") @Expose
		public String type;
		@SerializedName("cont") @Expose
		public Object cont;
		
		public static WSMsg convert(String source) {
			return new Gson().fromJson(source, WSMsg.class);
		}
	}
	
	@Override
	public void msgPro(WebSocketSession session, TextMessage message) throws Exception{
		log.info(session.getId() + " -> " + message.getPayload());
		WSMsg raw = WSMsg.convert(message.getPayload());

		if (webSocketMethods.isEmpty()) {
			Method target[] = this.getClass().getMethods();
			for (Method m : target) {
				if (m.isAnnotationPresent(WSReqeust.class)) {
					webSocketMethods.put(m.getName(),m);
				}
			}
		}

		if (webSocketMethods.containsKey(raw.getType())) {
			Method m = webSocketMethods.get(raw.getType());
			m.invoke(this, session.getId(), raw.cont+"");
		}
	}

	@Override @Transactional @WSReqeust
	public void auth(String sId, String msg) {
		log.info(sId + "님이 인증 시도 중");
		
		Auth auth = Auth.convert(msg);
		if (auth != null) {
			int uid = auth.getUid();
			if (playerMap.checkCode(uid, auth.getCode()) > 0) {
				playerMap.setSession(uid, sId);
				log.info(sId + "님이 인증했습니다.");
			}
		}
	}

	@Override @Transactional @WSReqeust
	public void chat(String sId, String msg) throws Exception {
		Player pl = playerMap.readBySid(sId);
		UserCore user = userMap.read(pl.getId());
		log.info("chat: "+pl.getRoomId() + "/"+user.getNickname()+": "+msg);

		int roomId = playerMap.readBySid(sId).getRoomId();
		List<Player> pls = playerMap.getPlayers(roomId);
		for (Player cur : pls) {
			if (cur.getRoomId() == roomId && cur.getState() > 0) {
				String sid = cur.getChatSessionId();
				send(sid, msg);
			}
		}
	}

	@Override @Transactional @WSReqeust
	public void request(String sId, String msg) throws Exception {
		if (msg.equals("roomList")) {
			log.info("방목록 요청됨");
			List<Room> rooms = roomMap.getCurrentRooms();
			log.info("전송함1");
			send(sId, "init", "room");
			log.info("전송함2");
			for (Room cur : rooms) {
				send(sId, "room", cur);
			}
			log.info("전송함3");
		} else if (msg.equals("readyList")) {
			
		}
	}

	@Override
	public void send(String sId, String type, Object cont) throws Exception {
		Gson gson = new Gson();
		WSMsg wsmsg = new WSMsg();
		wsmsg.setType(type);
		wsmsg.setCont(cont);
		String msg = gson.toJson(wsmsg);
		sessions.get(sId).sendMessage(new TextMessage(msg));
	}
	
	@Override
	public void send(String sId, String msg) throws Exception {
		sessions.get(sId).sendMessage(new TextMessage(msg));
	}


	@Override
	public void sendAll(String msg) throws Exception {
		List<String> sids = playerMap.getActiverSid();
		for (String cur : sids) {
			sessions.get(cur).sendMessage(new TextMessage(msg));
		}
	}

}
