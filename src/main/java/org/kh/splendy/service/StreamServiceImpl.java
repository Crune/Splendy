package org.kh.splendy.service;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import org.kh.splendy.annotation.WSReqeust;
import org.kh.splendy.mapper.*;
import org.kh.splendy.vo.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;
import com.google.gson.annotations.*;

import lombok.Data;

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
	@Autowired private CardService cardServ;
	
	@Override
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

	@Override @WSReqeust
	public void auth(String sId, String msg) throws Exception {
		log.info(sId + "님이 인증 시도 중");
		
		Auth auth = Auth.convert(msg);
		if (auth != null) {
			int uid = auth.getUid();
			if (playerMap.checkCode(uid, auth.getCode()) > 0) {
				playerMap.setSession(uid, sId);
				playerMap.setStateBySid(sId, Player.ST_CONNECT);
				log.info(sId + "님이 인증했습니다.");
				PlayerListVo me = playerMap.getMeJoinInfo(uid);
				sendWithoutSender(sId, "player.join", cvPlayer(me));
			}
		}
	}

	@Override @WSReqeust
	public void chat(String sId, String msg) throws Exception {
		Player pl = playerMap.readBySid(sId);
		UserCore user = userMap.read(pl.getId());
		log.info("send_chat:start: "+pl.getRoomId() + "/"+user.getNickname()+": "+msg);

		int roomId = pl.getRoomId();
		
		WSChat rst = new WSChat();
		rst.setNick(user.getNickname());
		rst.setCont(msg);
		rst.setTime(new SimpleDateFormat("hh:mm").format(new Date()));
		rst.setType("o");
		
		List<Player> pls = playerMap.getPlayers(roomId);
		for (Player cur : pls) {
			if (cur.getRoomId() == roomId && cur.getState() > 0) {
				String cur_sid = cur.getChatSessionId();
				if (cur_sid.equals(sId)) {
					rst.setType("me");
				}
				send(cur_sid, "chat", rst);
			}
		}
		log.info("send_chat:end: "+pl.getRoomId() + "/"+user.getNickname()+": "+msg);
	}
	


	@Override @WSReqeust
	public void request(String sId, String msg) throws Exception {
		if (msg.equals("roomList")) {
			List<Room> rooms = roomMap.getCurrentRooms();
			send(sId, "room.init", "{}");
			for (Room cur : rooms) {
				if (cur.getId() != 0) {
					send(sId, "room.add", cur);
				}
			}
		}
		if (msg.equals("playerList")) {
			List<PlayerListVo> users = playerMap.getActiver();
			send(sId, "player.init", "{}");
			for (PlayerListVo cur : users) {
				send(sId, "player.add", cvPlayer(cur));
			}
		}
	}
	
	public WSPlayer cvPlayer(PlayerListVo p) {
		WSPlayer rst = new WSPlayer();
		rst.setIcon("unnamed.png");
		rst.setNick(p.getNick());
		rst.setRating(1500);
		rst.setRole((p.getUid() == p.getHost()?"host":""));
		rst.setRoom(p.getRid());
		rst.setUid(p.getUid());		
		return rst;		
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
		log.info("sendeee: "+sId + "/"+type+cont);
		send(sId, cvMsg(type, cont));
	}
	@Override
	public void send(String sId, String msg) throws Exception {
		log.info("sendkkk: "+sId + "/"+msg);
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
