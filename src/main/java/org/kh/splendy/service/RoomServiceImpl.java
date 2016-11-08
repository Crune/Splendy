package org.kh.splendy.service;

import java.util.List;

import org.kh.splendy.mapper.RoomMapper;
import org.kh.splendy.vo.Article;
import org.kh.splendy.vo.Room;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Service
@EnableTransactionManagement
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomMapper roomMap;

	private static final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

	@Override
	public List<Room> getList() {
		// TODO 윤.로비: 리스트 서비스 구현
		return null;
	}
	
}
