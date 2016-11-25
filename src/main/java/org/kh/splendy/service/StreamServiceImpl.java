package org.kh.splendy.service;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletContext;

import org.apache.tomcat.jni.Global;
import org.kh.splendy.SplendyApplication;
import org.kh.splendy.aop.SplendyAdvice;
import org.kh.splendy.assist.ProtocolHelper;
import org.kh.splendy.assist.SplendyProtocol;
import org.kh.splendy.assist.WSController;
import org.kh.splendy.assist.WSReqeust;
import org.kh.splendy.mapper.*;
import org.kh.splendy.protocol.InGame;
import org.kh.splendy.vo.*;
import org.reflections.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.*;

@Service
@EnableTransactionManagement
public class StreamServiceImpl implements StreamService {

	private static final String cWasId = SplendyAdvice.WAS_ID;

	private Logger log = LoggerFactory.getLogger(StreamServiceImpl.class);
	
	//private static Map<Integer, GameRoom> rooms = new HashMap<Integer, GameRoom>();
	private static Map<String, WebSocketSession> sessions = new HashMap<String, WebSocketSession>();
	private static Map<String, WSPlayer> wsplayers = new HashMap<String, WSPlayer>();
	
	@Override public String getCwasid() { return cWasId; }
	//@Override public Map<Integer, GameRoom> getRooms() { return rooms; }
	@Override public Map<String, WebSocketSession> getSessions() { return sessions; }
	@Override public Map<String, WSPlayer> getWsplayers() { return wsplayers; }
	
	private static Map<String, Queue<String>> msgs = new HashMap<String, Queue<String>>();
	
	@Autowired private RoomMapper roomMap;
	@Autowired private PlayerMapper playerMap;
	@Autowired private UserProfileMapper profMap;
	@Autowired private UserMapper userMap;
	@Autowired private MsgMapper msgMap;
	
	@Autowired private UserInnerMapper innerMap;
	
	private InGame inGame;
	
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
						if (curPl.getRoom() > 0) {
							inGame.notiOut(curPl);
						}
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
				UserInner targetWAS = innerMap.readByWSId(cur);
				boolean isCurWas = false;
				if (targetWAS != null) targetWAS.getWas().equals(cWasId);
				if (isCurWas) {
					innerMap.setConnectBySid(cur, 0);
				}
			}
		}
	}
	
	@Override
	public void disconnectPro(WebSocketSession session) {
		kick(session.getId());
	}

	private static Map<String, Method> mByP = new HashMap<String, Method>();
	private static Map<String, String> helpTByP = new HashMap<String, String>();
	private static Map<String, Object> tByP = new HashMap<String, Object>();
	private static int pBuild = 0;
	
	private void initProtocol() {
		// @WSController를 선언한 모든 클래스를 대상으로 함
		String packageName = "org.kh.splendy";
		Reflections reflections = new Reflections(packageName);
		Set<Class<? extends Object>> allClasses = reflections.getTypesAnnotatedWith(WSController.class);
		allClasses.add(this.getClass());
		
		for (Class<?> cls : allClasses) {
			for (Method m : cls.getMethods()) {
				// WSRequest 어노테이션이 붙은 메서드만 대상으로 함
				if (m.isAnnotationPresent(WSReqeust.class)) {
					log.info("Protocol.Request.Add : "+m.getName());
					WSReqeust anno = m.getAnnotation(WSReqeust.class);
					String protocolName = m.getName();
					if (!anno.value().isEmpty()) {
						protocolName = anno.value();
					}
					mByP.put(protocolName, m);
				}
			}
		}

		// @Autowired 지원을 위해서 대상은 빈에서 꺼냄
		Map<String, Object> beans = SplendyApplication.ctx.getBeansWithAnnotation(WSController.class);
		beans.put("", this);

		// 대상은 빈에서 꺼내서 집어 넣음
		for (String bName : beans.keySet()) {
			// 해당 빈을 대상으로 집어넣음.
			Object pTarget = beans.get(bName);
			tByP.put(bName, pTarget);
			
			if (bName.equals("inGame")) {
				inGame = (InGame) pTarget;
			}
			// 프로토콜의 메서드를 수행할 대상 설정 부분 
			
			// 대상의 모든 메서드를 검사
			for (Method m : pTarget.getClass().getMethods()) {
				String bMName = m.getName();
				// 모든 프로토콜을 검사
				for (String pName : mByP.keySet()) {
					String pMName = mByP.get(pName).getName();
					// 프로토콜.메서드.이름 == 대상빈.메서드.이름
					if (pMName.equals(bMName)) {
						// 프로토콜을 수행할 객체명을 추가
						helpTByP.put(pName, bName);
					}
				}
			}
			
			
		}
	}
	
	@Override @Async
	public void msgPro(WebSocketSession session, TextMessage message) throws Exception{
		log.info(cWasId+"/"+session.getId() + " -> " + message.getPayload());
		
		// 입력받은 메시지를 JSON -> WSMsg로 변경
		WSMsg raw = WSMsg.convert(message.getPayload());

		String type = raw.getType();
		type = (type == null)?"":type;
		String sid = session.getId();
		
		/** !! 중요 - 프로토콜을 변경할 경우 숫자를 변경할것 !! */
		int nowBuild = 10;
		
		// 메시지를 처리할 메서드 목록이 비어있을 경우 목록 생성
		if (mByP.isEmpty() || pBuild != nowBuild) {
			pBuild = nowBuild;
			initProtocol();
		}

		// 입력받은 메시지의 type과 일치하는 메서드 명이 있을 경우
		if (mByP.containsKey(type)) {
			// 해당 메서드와 수행할 객체를 꺼냄
			Method m = mByP.get(type);
			Object target = tByP.get(helpTByP.get(type));
			
			log.info("msgProType: "+type);
			
			// 인증된 사용자가 아닐경우 인증만 가능하게 함
			boolean isAuthed = (wsplayers.get(sid) != null)?true:false;
			if (type.equals("auth") || isAuthed) {
				// 해당 메서드를 실행함
				m.invoke(target, session.getId(), raw.cont+"");
					
			// 권한없는 요청시 추방
			} else {
				log.info("msgProType: AccessDenied! Session will be close!");
				session.close();
			}
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
					
					// 현재 사용자의 세션ID를 설정
					inner.setWsSession(sId);
					// 현재 사용자를 접속중으로 설정
					inner.setConnect(1);
					inner.setWas(cWasId);
					
					// 현재 사용자 내부정보를 DB에 반영
					innerMap.update(inner);
					
					// 접속 세션의 사용자 정보를 서버에 저장
					WSPlayer me = playerMap.getWSPlayer(uid).CanSend();
					wsplayers.remove(sId);
					wsplayers.put(sId, me);

					// 현재 IP를 해당 플레이어 정보에 기입
					String ip = sessions.get(sId).getRemoteAddress().getHostName();
					playerMap.setIp(uid, me.getRoom(), ip);
					
					// 사용자의 접속지가 로비인지 게임방인지 구분
					String joinType = "player";
					if (me.getRoom() > 0) {
						joinType += ".enter";
						
						// 누군가 인증했음을 알림
						inGame.notiAuth(me);
					} else {
						joinType += ".join";
					}

					// 입장 알림
					sendWithoutSender(sId, joinType, me);
					
					// 허가 알림
					send(sId, "auth", "ok");
					
				}
			}
		}
	}

	/** 현재 시간을 문자열로 반환
	 * @return 현재 시간의 HH:mm:ss 형식의 문자열 반환 */
	private String getCurrentTime() {
		TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		df.setTimeZone(tz);
		return df.format(new Date());
	}
	
	@Override
	public void sendChat(int rid, WSPlayer sender, String msg) {
		WSChat rst = new WSChat();
		rst.setUid(sender.getUid());
		rst.setNick(sender.getNick());
		rst.setCont(msg);
		rst.setTime(getCurrentTime());		
		rst.setType("o");
		
		// 현재 접속중인 모든 사용자에게
		for (String cur : wsplayers.keySet()) {
			WSPlayer target = wsplayers.get(cur);
			// 해당 사용자가 존재할 경우
			if (sessions.get(cur) != null) {
				// 해당 사용자가 해당 방에 존재할 경우
				if (target.getRoom() == rid) {
					// 해당 사용자가 발언자일 경우
					if (target.getUid() == sender.getUid()) {
						rst.setType("me");
					// 해당 사용자가 시스템일 경우
					} else if (sender.getUid() == 0) {
						rst.setType("sys");
					}
					// 채팅 내용을 전송
					send(cur, "chat.new", rst);
				}
			}
		}

		// 전송한 메시지 DB에 저장
		logToDB(findSid(sender.getUid()), "chat.new", rst);
		
		log.info("send_chat: "+rst);
	}
	
	@Override @WSReqeust
	public void chat(String sId, String msg) throws Exception {
		WSPlayer sender = wsplayers.get(sId);
		sendChat(sender.getRoom(), sender, msg);
	}
	
	@Override
	public void logToDB(String sid, String type, Object msg) {
		WSPlayer actor = wsplayers.get(sid);
		
		Msg newMsg = new Msg();
		newMsg.setRid(actor.getRoom());
		newMsg.setUid(actor.getUid());
		newMsg.setType(type);
		newMsg.setCont(new Gson().toJson(msg));
		msgMap.create(newMsg);
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
	public void send(String sId, String type, Object cont) {
		send(sId, cvMsg(type, cont));
	}
	@Override
	public void send(String sId, String msg) {
		if (sessions.get(sId) != null) {
			try {
				sessions.get(sId).sendMessage(new TextMessage(msg));
			} catch (IOException e) {
				e.printStackTrace();
			}
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
	

/*
	// 제거 요망 : 프로토콜 작성 후 해당 서비스로 이관 필요.
	@Autowired private CardService cardServ;
	
	private List<Card> deck_lev1 = null;
	private List<Card> deck_lev2 = null;
	private List<Card> deck_lev3 = null;
	private List<Card> deck_levN = null;
	
	@Override @WSReqeust
	public void cardRequest(String sId, String msg) throws Exception {
			
		// 처음 카드를 세팅하는 조건문
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
*/

}
