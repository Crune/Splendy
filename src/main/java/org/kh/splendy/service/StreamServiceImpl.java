package org.kh.splendy.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kh.splendy.SplendyHandler;
import org.kh.splendy.mapper.PlayerMapper;
import org.kh.splendy.mapper.RoomMapper;
import org.kh.splendy.mapper.UserMapper;
import org.kh.splendy.vo.Auth;
import org.kh.splendy.vo.Chat;
import org.kh.splendy.vo.Msg;
import org.kh.splendy.vo.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;

import lombok.Data;

@Service
@EnableTransactionManagement
public class StreamServiceImpl implements StreamService {
	
	private Logger log = LoggerFactory.getLogger(StreamServiceImpl.class);

	private static Map<String, WebSocketSession> sessions = new HashMap<String, WebSocketSession>();

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
		
		sessions.remove(session.getId());
	}

	@Override
	public void msgPro(WebSocketSession session, TextMessage message) {
		log.info(session.getId() + " -> " + message.getPayload());
		Msg msgVO = Msg.convert(message.getPayload());
		if(msgVO.getType() == Msg.T_AUTH) {
			shake(session.getId(), msgVO); // 사용자 인증 전송
		} else if (msgVO.getType() == Msg.T_WHISPER) {			
			sendWhisper(session.getId(), msgVO); // 귓속말 전송
		} else if(msgVO.getType() == Msg.T_EMOTION) {
			sendEmotion(session.getId(), msgVO); // 감정표현 전송
		} else {
			send(session.getId(), msgVO); // 일반 채팅 전송
		}
	}

	@Override @Transactional
	public void shake(String sId, Msg msg) {		
		Auth auth = Auth.convert(msg.getCont());		
		if (auth != null) {
			int uid = auth.getUid();
			if (playerMap.checkCode(uid, auth.getCode()) > 0) {
				playerMap.setSession(uid, sId);
				log.info(sId + "님이 인증했습니다.");
			}
		}
	}

	@Override //@Transactional
	public void send(String sId, Msg msg) {
		// TODO Auto-generated method stub
		
	}

	@Override //@Transactional
	public void sendEmotion(String sId, Msg msg) {
		// TODO Auto-generated method stub
		
	}

	@Override //@Transactional
	public void sendWhisper(String sId, Msg msg) {
		// TODO Auto-generated method stub
		
	}

	@Override //@Transactional
	public void sendAll(Msg msg) {
		
	}

}
