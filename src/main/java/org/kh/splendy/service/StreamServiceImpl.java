package org.kh.splendy.service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.kh.splendy.annotation.WSReqeust;
import org.kh.splendy.aop.SplendyAdvice;
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
	
	
		
	private static Map<String, WebSocketSession> sessions = new HashMap<String, WebSocketSession>();
	private static Map<String, WSPlayer> wsplayers = new HashMap<String, WSPlayer>();
	private static Map<String, Method> webSocketMethods = new HashMap<String, Method>();

	@Autowired private RoomMapper roomMap;
	@Autowired private PlayerMapper playerMap;
	@Autowired private UserMapper userMap;
	@Autowired private MsgMapper msgMap;
	
	@Autowired private UserInnerMapper innerMap;
	
	@Autowired private CardService cardServ;
	
	@Override
	public void connectPro(WebSocketSession session) {
		log.info(session.getId() + "님이 접속했습니다.");
		log.info("연결 IP : " + session.getRemoteAddress().getHostName() );
		
		sessions.put(session.getId(), session);
		
		WSPlayer me = new WSPlayer();
		me.setIcon("unnamed.png");
		me.setNick("비회원"+session.getId());
		me.setRating(0);
		me.setRoom(-1);
		me.setUid(-1);
		wsplayers.put(session.getId(), me);
	}

	@Override @Transactional
	public void close(int uid) {
		kick(findSid(uid));
	}
	
	private String findSid(int uid) {
		String rst = null;
		for (String cur : wsplayers.keySet()) {
			WSPlayer curPl = wsplayers.get(cur);
			if (curPl.getUid() == uid) {
				rst = cur;
			}
		}
		return rst;
	}
	
	private void kick(String sid) {
		if (sid != null) {
			WSPlayer curPl = wsplayers.get(sid);
			int uid = wsplayers.get(sid).getUid();
			String nick = wsplayers.get(sid).getNick();
			int rid = wsplayers.get(sid).getRoom();
			
			boolean isReconnected = false;
			
			// 비회원이 아닐경우 DB의 접속해제 처리
			if (uid > 0) {
				UserInner inner = innerMap.read(uid);
				if (inner.getWsSession().equals(sid)) {
					// 재접속이 아닐경우의 처리
					inner.setConnect(0);
					inner.setWsAuthCode(null);
					inner.setWsSession(null);
					innerMap.update(inner);
				} else {
					// 재접속일 경우의 처리
					isReconnected = true;
				}
			}
			
			if (!isReconnected) {
				try {
					sendWithoutSender(sid, "player.leave", curPl);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			try {
				if (sessions.get(sid).isOpen()) {
					sessions.get(sid).close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				wsplayers.remove(sid);
				sessions.remove(sid);
				log.info(rid+"/"+nick+"(uid:"+uid+"/sid:"+sid + ") 님이 퇴장했습니다.");
			}
		}
	}
	
	@Override
	public void disconnectPro(WebSocketSession session) {
		kick(session.getId());
	}

	@Override @Async
	public void msgPro(WebSocketSession session, TextMessage message) throws Exception{
		log.info(session.getId() + " -> " + message.getPayload());
		WSMsg raw = WSMsg.convert(message.getPayload());

		if (webSocketMethods.isEmpty()) {
			for (Method m : this.getClass().getMethods()) {
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
		Auth auth = Auth.convert(msg);
		if (auth != null) {
			int uid = auth.getUid();
			if (innerMap.checkWSCode(uid, auth.getCode()) > 0) {
				innerMap.setWSId(uid, sId);
				innerMap.setConnect(uid, 1);
				String ip = sessions.get(sId).getRemoteAddress().getHostName();
				playerMap.setIp(uid, 0, ip);
				
				send(sId, "auth", "ok");
				
				WSPlayer me = playerMap.getWSPlayer(uid).CanSend();
				wsplayers.remove(sId);
				wsplayers.put(sId, me);
				
				sendWithoutSender(sId, "player.join", me);
			}
		}
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
	private String getCurrentTime() {
		TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		df.setTimeZone(tz);
		return df.format(new Date());
	}
	
	private void sendChat(int rid, WSPlayer sender, String msg) throws Exception {
		WSChat rst = new WSChat();
		rst.setUid(sender.getUid());
		rst.setNick(sender.getNick());
		rst.setCont(msg);
		rst.setTime(getCurrentTime());		
		rst.setType("o");
		
		for (String cur : wsplayers.keySet()) {
			WSPlayer target = wsplayers.get(cur);
			if (sessions.get(cur) != null) {
				if (target.getRoom() == rid) {
					if (target.uid == sender.getUid()) {
						rst.setType("me");
					} else if (sender.getUid() == 0) {
						rst.setType("sys");
					}
					send(cur, "chat.new", rst);
				}
			}
		}

		Msg newMsg = new Msg();
		newMsg.setRid(rid);
		newMsg.setUid(sender.getUid());
		newMsg.setType("chat.new");
		newMsg.setCont(new Gson().toJson(rst));
		msgMap.create(newMsg);
		
		log.info("send_chat: "+rst);
	}
	
	@Override @WSReqeust
	public void chat(String sId, String msg) throws Exception {
		sendChat(0, wsplayers.get(sId), msg);
	}

	public void newMsg(String sId, String type, String msg) throws Exception {
		UserInner inner = innerMap.readByWSId(sId);
		Player player =  playerMap.read(inner.getId());

		Msg newMsg = new Msg();
		newMsg.setRid(player.getRoom());
		newMsg.setUid(player.getId());
		newMsg.setType(type);
		newMsg.setCont(msg);
		msgMap.create(newMsg);
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
			send(sId, "player.init", "{}");
			for (String cur : wsplayers.keySet()) {
				if (sessions.containsKey(cur)) {
					WSPlayer curPl = wsplayers.get(cur);
					if (curPl.getRoom() == 0 && curPl != null) {
						send(sId, "player.add", curPl);
					}
				}
			}
		}
		if (msg.equals("prevMsg")) {
			WSPlayer reqUser = playerMap.getWSPlayerBySid(sId);
			int rid = reqUser.getRoom();
			List<Msg> msgs = msgMap.readPrevChat(rid, 31);
			send(sId, "chat.init", "{}");
			for (Msg cur : msgs) {
				WSChat curMsg = WSChat.convert(cur.getCont());
				if (curMsg.getUid() == reqUser.getUid()) {
					curMsg.setType("me");
				} else {
					curMsg.setType("o");
				}
				send(sId, "chat.new", curMsg);
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
		if (sessions.get(sId) != null) {
			sessions.get(sId).sendMessage(new TextMessage(msg));
			log.info(sId+" <- "+msg);
		} else {
			log.info(sId+" <-/- "+msg);
		}
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
					send(cur, msg);
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
			send(cur, msg);
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
			
			List<Card> copyDeck = deck_levN;
			
			for(int i = 0; i < 5; i++){
				initHeroCard.add(copyDeck.remove(0));
			}
			sendR(sId, "init_levN", initHeroCard);			
		} else if(msg.equals("init_lev1")){
			List<Card> initLev1Card = new ArrayList<Card>();
			deck_lev1 = cardServ.getLevel_1();  //1레벨 덱
			List<Card> copyDeck = deck_lev1;
			
			for(int i = 0; i < 4; i++){
				initLev1Card.add(copyDeck.remove(0));
			}
			sendR(sId, "init_lev1", initLev1Card);			
		} else if(msg.equals("init_lev2")){
			List<Card> initLev2Card = new ArrayList<Card>();
			deck_lev2 = cardServ.getLevel_2();  //2레벨 덱
			List<Card> copyDeck = deck_lev1;
			
			for(int i = 0; i < 4; i++){
				initLev2Card.add(copyDeck.remove(0));
			}
			sendR(sId, "init_lev2", initLev2Card);			
		} else if(msg.equals("init_lev3")){
			List<Card> initLev3Card = new ArrayList<Card>();
			deck_lev3 = cardServ.getLevel_3();  //3레벨 덱
			List<Card> copyDeck = deck_lev1;
			
			for(int i = 0; i < 4; i++){			
				initLev3Card.add(copyDeck.remove(0));
			}
			sendR(sId, "init_lev3", initLev3Card);			
		}
		
		if(msg.equals("getHeroCard")){	
			
		
			
		}
			
	}

	@Override @WSReqeust
	public void cardCount(String sId, String msg) throws Exception {
		Gson gson = new Gson();
		GameLog gameLog = gson.fromJson(msg, GameLog.class);
		sendR(sId, "cardCountPro", gameLog);		
	}
	
}
