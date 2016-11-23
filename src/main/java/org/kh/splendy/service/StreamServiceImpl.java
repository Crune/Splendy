package org.kh.splendy.service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.RandomStringUtils;
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

import lombok.Getter;

@Service
@EnableTransactionManagement
public class StreamServiceImpl implements StreamService {

	private static final String cWasId = SplendyAdvice.WAS_ID;

	private Logger log = LoggerFactory.getLogger(StreamServiceImpl.class);
	
	private List<Card> deck_lev1 = null;
	private List<Card> deck_lev2 = null;
	private List<Card> deck_lev3 = null;
	private List<Card> deck_levN = null;
	
	private static Map<Integer, GameRoom> rooms = new HashMap<Integer, GameRoom>();
	
	private static Map<String, WebSocketSession> sessions = new HashMap<String, WebSocketSession>();
	private static Map<String, WSPlayer> wsplayers = new HashMap<String, WSPlayer>();
	private static Map<String, Queue<String>> msgs = new HashMap<String, Queue<String>>();
	private static Map<String, Method> webSocketMethods = new HashMap<String, Method>();
	
	@Autowired private RoomMapper roomMap;
	@Autowired private PlayerMapper playerMap;
	@Autowired private UserProfileMapper profMap;
	@Autowired private UserMapper userMap;
	@Autowired private MsgMapper msgMap;
	
	@Autowired private UserInnerMapper innerMap;
	
	@Autowired private CardService cardServ;

	@Override
	public void createRoom(int rid) {
		GameRoom room = new GameRoom();
		room.setRoom(rid);
		/*PLCard[][] cards = {(PLCard[]) cardServ.getLevel_1().toArray()};
		room.setCards(cards);*/
		rooms.put(room.getRoom(), room);
	}
	
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
	
	@Override
	public String findSid(int uid) {
		String rst = null;
		for (String cur : wsplayers.keySet()) {
			WSPlayer curPl = wsplayers.get(cur);
			if (curPl.getUid() == uid) {
				rst = cur;
			}
		}
		return rst;
	}
	
	@Override
	public int findUid(String sid) {
		return wsplayers.get(sid).getUid();
	}
	
	@Override
	public void kick(String sid) {
		if (wsplayers.get(sid) != null) {
			WSPlayer curPl = wsplayers.get(sid);
			int uid = curPl.getUid();
			String nick = curPl.getNick();
			int rid = curPl.getRoom();
			
			try {
				// 비회원이 아닐경우 DB정보 불러옴
				UserInner inner = (uid > 0)?innerMap.read(uid):null;
				
				// 현재 사용자가 접속중으로 되어 있을 경우
				if (inner != null) {
					if (inner.getConnect() > 0) {
						// DB상 사용자를 비접속으로 변경한다.
						inner.setConnect(0);
						inner.setWsAuthCode(null);
						inner.setWsSession(null);
						innerMap.update(inner);
						sendWithoutSender(sid, "player.leave", curPl);
					}
				}
				
				// 접속 상태일 경우 연결 끊음.
				if (sessions.get(sid).isOpen()) {
					sessions.get(sid).close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 무슨일이 벌어지건 목록에서 삭제
				wsplayers.remove(sid);
				sessions.remove(sid);
				log.info(rid+"/"+nick+"(uid:"+uid+"/sid:"+sid + ") 님이 퇴장했습니다.");
			}
		}
	}

	@Override
	public void refreshConnector() {
		// 접속 상태를 확인후 끊어졌을 경우 추방
		for (String cur : sessions.keySet()) {
			if (!sessions.get(cur).isOpen()) {
				kick(cur);
			}
		}
		// 접속이 끊어졌지만 참가자목록에 남아있을 경우 추방
		for (String cur : wsplayers.keySet()) {
			if (sessions.get(cur) == null) {
				kick(cur);
			}
		}
		// 서버 비정상 종료로 잔여할 경우 DB수정
		for (String cur : innerMap.getConnector()) {
			if (!wsplayers.containsKey(cur)) {
				innerMap.setConnectBySid(cur, 0);
			}
		}
	}
	
	@Override
	public void disconnectPro(WebSocketSession session) {
		kick(session.getId());
	}

	@Override @Async
	public void msgPro(WebSocketSession session, TextMessage message) throws Exception{
		log.info(cWasId+"/"+session.getId() + " -> " + message.getPayload());
		
		// 입력받은 메시지를 JSON -> WSMsg로 변경
		WSMsg raw = WSMsg.convert(message.getPayload());

		// 메시지를 처리할 메서드 목록이 비어있을 경우 목록 생성
		if (webSocketMethods.isEmpty()) {
			// 현재 클래스에서 메서드 추출
			for (Method m : this.getClass().getMethods()) {
				// WSRequest 어노테이션이 붙은 메서드만 대상으로 함
				if (m.isAnnotationPresent(WSReqeust.class)) {
					webSocketMethods.put(m.getName(),m);
				}
			}
		}

		// 입력받은 메시지의 type과 일치하는 메서드가 있을 경우
		if (webSocketMethods.containsKey(raw.getType())) {
			// 해당 메서드를 꺼내서
			Method m = webSocketMethods.get(raw.getType());

			log.info("msgProType: "+raw.getType());
			
			// 해당 메서드를 실행함
			m.invoke(this, session.getId(), raw.cont+"");
		}
		
		
	}

	@Override @WSReqeust @Transactional
	public void auth(String sId, String msg) throws Exception {
		Auth auth = Auth.convert(msg);
		if (auth != null) {
			int uid = auth.getUid();
			UserInner inner = innerMap.read(uid);
			// 해당 유저의 인증값이 DB와 일치하는지 확인
			if (inner != null) {
				if (inner.getWsAuthCode().equals(auth.getCode())) {
					
					// 현재 세션ID를 DB에 입력
					inner.setWsSession(sId);
					inner.setConnect(1);
					inner.setWas(cWasId);
					innerMap.update(inner);
					
					// 접속 세션의 사용자 정보를 서버에 저장
					WSPlayer me = playerMap.getWSPlayer(uid).CanSend();
					wsplayers.remove(sId);
					wsplayers.put(sId, me);

					// 현재 IP를 해당 플레이어 정보에 기입
					String ip = sessions.get(sId).getRemoteAddress().getHostName();
					playerMap.setIp(uid, me.room, ip);
					
					// 입장 알림
					if (me.room > 0) {
						sendWithoutSender(sId, "player.enter", wsplayers.get(sId));
					} else {
						sendWithoutSender(sId, "player.join", me);
					}
					
					send(sId, "auth", "ok");
					
				}
			}
		}
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
	public void join(String sId, String msg) {
		Room input = new Gson().fromJson(msg, Room.class);
		int rid = input.getId();
		int uid = findUid(sId);
		String pw = input.getPassword();
		String ip = sessions.get(sId).getRemoteAddress().getHostName();
		boolean canJoin = false;
		
		// 비밀번호가 일치하거나 없을 경우만 참가가능
		Room reqRoom = roomMap.read(rid);
		if (reqRoom.getPassword() == null) {
			canJoin = true;
		} else if (reqRoom.getPassword().equals(pw)) {
			canJoin = true;
		}

		// 인원제한 보다 참가자 수가 적을 경우만 참가가능
		int countLimits = playerMap.getInRoomPlayerByRid(rid).size();
		canJoin = (canJoin && reqRoom.getPlayerLimits() > countLimits)?true:false;
		
		// 참가하고 있는 방이 없을 경우만 참가가능
		int countIsIn = playerMap.countIsIn(findUid(sId));
		canJoin = (canJoin && countIsIn == 0)?true:false;
		
		if (canJoin) {
			// DB에 접속 정보 입력
			Player player = null;
			if (playerMap.count(uid, rid) == 0) {
				player = new Player();
				player.setId(uid);
				player.setRoom(rid);
				player.setIsIn(1);
				player.setIp(ip);
				playerMap.create(player);
			} else {
				player = playerMap.read(uid);
				player.setIp(ip);
				player.setIsIn(1);
				playerMap.update(player);
			}
			playerMap.setIsIn(findUid(sId), 0, 0);
			profMap.setLastRoom(findUid(sId), rid);
			
			try {
				send(sId, "room.accept", rid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override @WSReqeust
	public void left(String sId, String msg) {
		int uid = findUid(sId);
		int rid = Integer.parseInt(msg);
		playerMap.setIsIn(uid, rid, 0);
		profMap.setLastRoom(uid, 0);
	}
	
	@Override @WSReqeust
	public void request(String sId, String msg) throws Exception {
		WSPlayer reqUser = wsplayers.get(sId);
		if (msg.equals("roomList")) {
			List<Room> rooms = roomMap.getCurrentRooms();
			send(sId, "room.init", "{}");
			if (!rooms.isEmpty()) 
			for (Room cur : rooms) {
				if (cur.getId() != 0) {
					if (cur.getPassword() != null) {
						if (!cur.getPassword().isEmpty()) {
							cur.setPassword("true");
						}
					}
					send(sId, "room.add", cur);
				}
			}
		}
		if (msg.equals("playerList")) {
			send(sId, "player.init", "{}");
			for (String cur : wsplayers.keySet()) {
				if (sessions.containsKey(cur)) {
					WSPlayer curPl = wsplayers.get(cur);
					if (curPl != null) {
						if (curPl.getRoom() == 0) {
							send(sId, "player.add", curPl);
						} else {
							send(sId, "player.enter", curPl);
						}
					}
				}
			}
		}
		if (msg.equals("prevMsg")) {
			
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
			log.info(cWasId+"/"+sId+" <- "+msg);
		} else {
			log.info(cWasId+"/"+sId+" <-/- "+msg);
		}
	}

	@Override
	public void sendWithoutSender(String sId, String type, Object cont) {
		for (String cur : wsplayers.keySet()) {
			if (!cur.equals(sId) && sessions.get(cur) != null) {
				try {
					send(cur, type, cont);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void sendAll(String type, Object cont){
		sendWithoutSender("", type, cont);
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
