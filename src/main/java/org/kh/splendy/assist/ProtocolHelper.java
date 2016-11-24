package org.kh.splendy.assist;

import org.kh.splendy.mapper.CardMapper;
import org.kh.splendy.mapper.MsgMapper;
import org.kh.splendy.mapper.PlayerMapper;
import org.kh.splendy.mapper.RoomMapper;
import org.kh.splendy.mapper.UserInnerMapper;
import org.kh.splendy.mapper.UserMapper;
import org.kh.splendy.mapper.UserProfileMapper;
import org.kh.splendy.service.StreamService;

import org.springframework.beans.factory.annotation.Autowired;

@WSController
public abstract class ProtocolHelper {
	
	@Autowired protected StreamService stream;

	@Autowired protected RoomMapper roomMap;
	@Autowired protected MsgMapper msgMap;
	
	@Autowired protected PlayerMapper playerMap;
	
	@Autowired protected UserInnerMapper innerMap;
	@Autowired protected UserMapper userMap;
	@Autowired protected UserProfileMapper profMap;

}
