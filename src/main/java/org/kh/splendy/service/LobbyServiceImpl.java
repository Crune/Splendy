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
	@Autowired private UserMapper userMap;

	private static final Logger log = LoggerFactory.getLogger(LobbyServiceImpl.class);

	@Override
	public List<Room> getList() {
		return roomMap.getCurrentRooms();
	}

	@Override @Transactional
	public void initPlayer(int id) {
		Player player = new Player();
		player.setAuthcode(RandomStringUtils.randomAlphanumeric(9));
		player.setChatSessionId("");
		player.setId(id);
		player.setRoomId(0);
		player.setState(Player.ST_CONNECT);
		if (playerMap.count(id)<1) {
			playerMap.create(player);
		} else {
			playerMap.update(player);
		}
	}

	@Override
	public Auth getAuth(int uid) {
		Auth auth = new Auth();
		auth.setUid(uid);
		auth.setCode(playerMap.getCode(uid));
		return auth;
	}
	
}
