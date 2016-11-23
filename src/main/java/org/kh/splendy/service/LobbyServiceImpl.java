package org.kh.splendy.service;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.kh.splendy.mapper.*;
import org.kh.splendy.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableTransactionManagement
public class LobbyServiceImpl implements LobbyService {

	@Autowired private RoomMapper roomMap;
	@Autowired private PlayerMapper playerMap;
	@Autowired private UserInnerMapper innerMap;
	@Autowired private UserMapper userMap;
	@Autowired private UserProfileMapper profMap;
	@Autowired private CardMapper cardMap;
	
	@Autowired private StreamService stream;

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
			stream.createRoom(rst);
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
}
