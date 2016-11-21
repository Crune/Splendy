package org.kh.splendy.service;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.kh.splendy.annotation.WSReqeust;
import org.kh.splendy.mapper.*;
import org.kh.splendy.vo.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;

@Service
@EnableTransactionManagement
public class StreamServiceImpl implements StreamService {
	
	private Logger log = LoggerFactory.getLogger(StreamServiceImpl.class);
	
	private List<Card> deck_lev1 = null;
	private List<Card> deck_lev2 = null;
	private List<Card> deck_lev3 = null;
	private List<Card> deck_levN = null;
	
	private int deck_levN_iter;
		
	private static Map<String, WebSocketSession> sessions = new HashMap<String, WebSocketSession>();
	private static Map<String, Method> webSocketMethods = new HashMap<String, Method>();

	@Autowired private RoomMapper roomMap;
	@Autowired private PlayerMapper playerMap;
	@Autowired private UserMapper userMap;
	@Autowired private UserInnerMapper innerMap;
	@Autowired private CardService cardServ;
	
	@Override
	public void connectPro(WebSocketSession session) {
		log.info(session.getId() + "님이 접속했습니다.");
		log.info("연결 IP : " + session.getRemoteAddress().getHostName() );
		
		sessions.put(session.getId(), session);
	}

	@Override @Transactional
	public void close(int uid) {
		UserInner inner = innerMap.read(uid);
		inner.setConnect(0);
		inner.setWsSession(null);
		inner.setWsAuthCode(null);
		innerMap.update(inner);
		sessions.remove(inner.getWsSession());
		log.info(inner.getWsSession() + "님이 퇴장했습니다.");
	}
	
	@Override
	public void disconnectPro(WebSocketSession session) {
		if (sessions.containsKey(session.getId())) {
			sessions.remove(session.getId());
			UserInner inner = innerMap.readByWSId(session.getId());
			if (inner != null){
				close(inner.getId());
			}
		}
	}

	@Override @Async
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

			log.info("msgProType: "+raw.getType());
			m.invoke(this, session.getId(), raw.cont+"");
		}
	}

	@Override @WSReqeust @Transactional
	public void auth(String sId, String msg) throws Exception {
		log.info(sId + "님이 인증 시도 중");
		
		Auth auth = Auth.convert(msg);
		if (auth != null) {
			int uid = auth.getUid();
			if (innerMap.checkWSCode(uid, auth.getCode()) > 0) {
				innerMap.setWSId(uid, sId);
				innerMap.setConnect(uid, 1);
				updateIp(uid, sId);
				log.info(sId + "님이 인증했습니다.");
				
				WSPlayer me = playerMap.getWSPlayer(uid).CanSend();
				sendWithoutSender(sId, "player.join", me);
			}
		}
	}

	private void updateIp(int uid, String sid) {
		int rid = 0; // 프로필 읽어온다음엔 수정할것
		int nuid = (uid<1)?innerMap.readByWSId(sid).getId():uid;
		String nsid = (sid==null)?innerMap.getWSId(uid):sid;
		String ip = sessions.get(nsid).getRemoteAddress().getHostName();
		playerMap.setIp(nuid, rid, ip);
		
	}

	private UserTotal getTUserBySid(String sId) {
		UserTotal user = new UserTotal();
		
		user.setInner(innerMap.readByWSId(sId));
		user.setPl(playerMap.read(user.getInner().getId()));
		user.setUser(userMap.read(user.getPl().getId()));
		user.setRoom(roomMap.read(user.getPl().getRoom()));
		//user.setProf(profMap.read(user.getInner().getId()));
		return user;
	}
	
	@Override @WSReqeust
	public void chat(String sId, String msg) throws Exception {

		List<WSPlayer> pls = playerMap.getInRoomPlayer(sId);
		
		String nick = "";
		for (WSPlayer cur : pls) {
			if (cur.role.equals(sId)) {
				nick = cur.getNick();
			}
		}

		WSChat rst = new WSChat();
		rst.setNick(nick);
		rst.setCont(msg);

		TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		df.setTimeZone(tz);
		rst.setTime(df.format(new Date()));
		
		for (WSPlayer cur : pls) {
			if (cur.role.equals(sId)) {
				rst.setType("me");
			} else {
				rst.setType("o");
			}
			send(cur.getRole(), "chat", rst);
		}
		
		log.info("send_chat: "+rst);
	}
	


	@Override @WSReqeust
	public void request(String sId, String msg) throws Exception {
		if (msg.equals("roomList")) {
			List<Room> rooms = roomMap.getCurrentRooms();
			send(sId, "room.init", "{}");
			if (!rooms.isEmpty()) 
			for (Room cur : rooms) {
				if (cur.getId() != 0) {
					send(sId, "room.add", cur);
				}
			}
		}
		if (msg.equals("playerList")) {
			List<WSPlayer> users = playerMap.getAllWSPlayer();
			send(sId, "player.init", "{}");
			for (WSPlayer cur : users) {
				send(sId, "player.add", cur.CanSend());
			}
		}
	}

	@Override
	public String cvMsg(String type, Object cont) {
		Gson gson = new Gson();
		WSMsg wsmsg = new WSMsg();
		wsmsg.setType(type);
		wsmsg.setCont(cont);
		return gson.toJson(wsmsg);
	}

	@Override
	public void send(String sId, String type, Object cont) throws Exception {
		send(sId, cvMsg(type, cont));
	}
	@Override
	public void send(String sId, String msg) throws Exception {
		sessions.get(sId).sendMessage(new TextMessage(msg));
	}

	@Override
	public void sendWithoutSender(String sId, String type, Object cont) throws Exception {
		sendWithoutSender(sId, cvMsg(type, cont));
	}
	@Override
	public void sendWithoutSender(String sId, String msg) throws Exception {
		List<String> sids = playerMap.getActiverSid();
		if (sids.size() > 0) {
			for (String cur : sids) {
				if (cur.equals(sId)) {
					sessions.get(cur).sendMessage(new TextMessage(msg));
				}
			}
		}
	}
	
	@Override
	public void sendAll(String type, Object cont) throws Exception {
		sendAll(cvMsg(type, cont));
	}
	
	@Override
	public void sendAll(String msg) throws Exception {
		List<String> sids = playerMap.getActiverSid();
		for (String cur : sids) {
			sessions.get(cur).sendMessage(new TextMessage(msg));
		}
	}
	
	@Override
	public void sendR(String sId, String type, Object cont) throws Exception {
		send(sId, type, cont);
	}
	
	@Override @WSReqeust
	public void cardRequest(String sId, String msg) throws Exception {
		
		
		/*
		 *  처음 카드를 세팅하는 조건문
		 */
		if(msg.equals("init_levN")){			
			List<Card> initHeroCard = new ArrayList<Card>();
			deck_levN = cardServ.getLevel_noble(); //히어로카드 덱
			for(int i = 0; i < deck_levN.size(); i++){
				log.info(deck_levN.get(i)+"");
			}
			for(int i = 0; i < 5; i++){
				initHeroCard.add(deck_levN.get(i));
			}				

			sendR(sId, "init_levN", initHeroCard);			
		} else if(msg.equals("init_lev1")){
			List<Card> initLev1Card = new ArrayList<Card>();
			deck_lev1 = cardServ.getLevel_1();  //1레벨 덱
			
			for(int i = 0; i < 5; i++){
				initLev1Card.add(deck_lev1.get(i));
			}
			sendR(sId, "init_lev1", initLev1Card);			
		} else if(msg.equals("init_lev2")){
			List<Card> initLev2Card = new ArrayList<Card>();
			deck_lev2 = cardServ.getLevel_2();  //2레벨 덱
			
			for(int i = 0; i < 5; i++){
				initLev2Card.add(deck_lev2.get(i));
			}
			sendR(sId, "init_lev2", initLev2Card);			
		} else if(msg.equals("init_lev3")){
			List<Card> initLev3Card = new ArrayList<Card>();
			deck_lev3 = cardServ.getLevel_3();  //3레벨 덱
			
			for(int i = 0; i < 5; i++){
				
				initLev3Card.add(deck_lev3.get(i));	
			}
			sendR(sId, "init_lev3", initLev3Card);			
		}
		
		if(msg.equals("getHeroCard")){	
			
			if(deck_levN_iter < deck_levN.size()){
				sendR(sId, "getHeroCard", deck_levN.get(deck_levN_iter));
				deck_levN_iter++;
			} else {
				return;
			} 
			
		}
			
	}

	@Override @WSReqeust
	public void cardCount(String sId, String msg) throws Exception {
		Gson gson = new Gson();
		GameLog gameLog = gson.fromJson(msg, GameLog.class);
		sendR(sId, "cardCountPro", gameLog);		
	}
	
}
