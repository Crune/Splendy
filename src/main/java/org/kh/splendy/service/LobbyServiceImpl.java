package org.kh.splendy.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.kh.splendy.assist.WSReqeust;
import org.kh.splendy.mapper.*;
import org.kh.splendy.trash.StreamService;
import org.kh.splendy.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

@Service
@EnableTransactionManagement
public class LobbyServiceImpl implements LobbyService {

	@Autowired private RoomMapper roomMap;
	@Autowired private PlayerMapper playerMap;
	@Autowired private UserInnerMapper innerMap;
	@Autowired private UserMapper userMap;
	@Autowired private UserProfileMapper profMap;
	@Autowired private CardMapper cardMap;
	@Autowired private MsgMapper msgMap;
	
	@Autowired private StreamService stream;
	@Autowired private CompService compServ;
	//@Autowired private InGame ingame;

	private static final Logger log = LoggerFactory.getLogger(LobbyServiceImpl.class);

	@Override
	public List<Room> getList() {
		return roomMap.getCurrentRooms();
	}

	@Override @Transactional
	public UserCore initPlayer(UserCore inUser, int rid) {
		if (inUser != null) {
			int uid = inUser.getId();
			
			UserInner inner = innerMap.read(uid);
			if (inner.getWsSession() != null) {
				stream.close(uid);
			}
			innerMap.setWSCode(uid, RandomStringUtils.randomAlphanumeric(9));
			playerMap.setIsIn(uid, rid, 1);
			
			UserCore user = userMap.read(uid);
			return user;
		} else {
			return null;
		}
	}

	@Override
	public Auth getAuth(int uid) {
		Auth auth = new Auth();
		auth.setUid(uid);
		auth.setCode(innerMap.getWSCode(uid));
		log.info("Auth Info: "+auth);
		return auth;
	}

	//여기서 부터 방생성
	@Override @Transactional
	public int createRoom(Room reqRoom, UserCore user) {
		
		int rst = -1;
		
		reqRoom.setHost(user.getId());
		
		boolean isTitle = !reqRoom.getTitle().isEmpty();
		boolean isInfo = !reqRoom.getInfo().isEmpty();
		boolean isPlLimit = (reqRoom.getPlayerLimits() >= 2 && reqRoom.getPlayerLimits() <= 4);
		
		boolean isNotHaveRoom = true;
		for (Room cur : roomMap.getCurrentRooms()) {
			if (cur.getHost() == user.getId()) {
				isNotHaveRoom = false;
			}
		}
		
		if (isTitle && isInfo && isPlLimit && isNotHaveRoom) {
			roomMap.create(reqRoom);
			rst = roomMap.getMyRoom(user.getId());
			
			stream.sendAll("room.add", roomMap.read(rst));
		}
		
		return rst;
	}

	@Override
	public int getLastRoom(int uid) {
		int rid = profMap.getLastRoom(uid);
		if (roomMap.read(rid).getEnd() == null) {
			return rid;
		} else {
			playerMap.setIsIn(uid, rid, 0);
			profMap.setLastRoom(uid, 0);
			return 0;
		}
	}

	@Override @WSReqeust
	public void request(String sId, String msg) {
		log.info("LobbyServiceImpl.request");
		WSPlayer reqUser = stream.getWsplayers().get(sId);
		if (msg.equals("roomList")) {
			List<Room> rooms = roomMap.getCurrentRooms();
			stream.send(sId, "room.init", "{}");
			if (!rooms.isEmpty()) 
			for (Room cur : rooms) {
				if (cur.getId() != 0 && cur.getPassword() != null) {
					if (!cur.getPassword().isEmpty()) {
						cur.setPassword("true");
					}
				}
			}
			stream.send(sId, "room.prev", rooms);
		}
		if (msg.equals("playerList")) {
			stream.send(sId, "player.init", "{}");
			stream.refreshConnector();
			List<WSPlayer> pls = new ArrayList<WSPlayer>();
			for (String cur : stream.getWsplayers().keySet()) {
				if (stream.getSessions().containsKey(cur)) {
					WSPlayer curPl = stream.getWsplayers().get(cur);
					if (curPl != null) {
						pls.add(curPl);
					}
				}
			}
			stream.send(sId, "player.prev", pls);
		}
		if (msg.equals("prevMsg")) {
			
			int rid = reqUser.getRoom();
			List<Msg> msgs = msgMap.readPrevChat(rid, 31);
			List<WSChat> chats = new ArrayList<WSChat>();
			stream.send(sId, "chat.init", "{}");
			for (Msg cur : msgs) {
				WSChat curMsg = WSChat.convert(cur.getCont());
				if (curMsg.getUid() == reqUser.getUid()) {
					curMsg.setType("me");
				} else {
					curMsg.setType("o");
				}
				chats.add(curMsg);
			}
			stream.send(sId, "chat.prev", chats);
			log.info("msgs:"+msgs.toString());
		}
	}

	@Override @WSReqeust
	public void join(String sId, String msg) {
		Room input = new Gson().fromJson(msg, Room.class);
		int rid = input.getId();
		int uid = stream.findUid(sId);
		String pw = input.getPassword();
		String ip = stream.getSessions().get(sId).getRemoteAddress().getHostName();
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
		int countIsIn = playerMap.countIsIn(uid);
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
				player = playerMap.read(uid, rid);
				player.setIp(ip);
				player.setIsIn(1);
				playerMap.update(player);
			}
			
			// 로비 접속 불가능 설정
			playerMap.setIsIn(uid, 0, 0);
			profMap.setLastRoom(uid, rid);
			
			try {
				stream.send(sId, "room.accept", rid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override @WSReqeust
	/** uid 사용시 sid는 미기입 필요 */
	public void left(String sId, String uId) {
		log.info("나가기테스트");
		int uid = -1;
		int rid = -1;
		
		if (!(sId == null || sId.length() == 0)) {
			uid = stream.findUid(sId);
			rid = stream.getWsplayers().get(sId).getRoom();
		} else if (!(uId == null || uId.length() == 0)) {
			try {
				uid = Integer.parseInt(uId);
				rid = profMap.getLastRoom(uid);
			} finally { }
		}
		if (uid>0 && rid>0) {
			playerMap.setIsIn(uid, rid, 0);
			profMap.setLastRoom(uid, 0);
			
			List<Integer> notEmpty = roomMap.getNotEmptyRoom();
			if (!notEmpty.contains(rid)) {
				roomMap.close(rid);
				Room tempRoom = new Room();
				tempRoom.setId(rid);
				stream.sendAll("room.remove", tempRoom);
			}
			
			try {
				stream.send(sId, "left", "");
			} finally { }
		}
	}
}
