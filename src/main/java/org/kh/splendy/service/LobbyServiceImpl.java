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
	
	@Autowired private StreamService stream;

	private static final Logger log = LoggerFactory.getLogger(LobbyServiceImpl.class);

	@Override
	public List<Room> getList() {
		return roomMap.getCurrentRooms();
	}

	@Override @Transactional
	public UserCore initPlayer(UserCore inUser) {
		int uid = inUser.getId();
		
		UserInner inner = innerMap.read(uid);
		if (inner.getWsSession() != null) {
			stream.close(uid);
		}
		innerMap.setWSCode(uid, RandomStringUtils.randomAlphanumeric(9));
		playerMap.setIsIn(uid, 0, 1);
		
		UserCore user = userMap.read(uid);
		return user;
	}

	@Override
	public Auth getAuth(int uid) {
		Auth auth = new Auth();
		auth.setUid(uid);
		auth.setCode(innerMap.getWSCode(uid));
		log.info("Auth Info: "+auth);
		return auth;
	}
	
}
