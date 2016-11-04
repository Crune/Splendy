package org.kh.splendy.service;

import org.kh.splendy.mapper.BoardMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@MapperScan(basePackages = { "org.kh.splendy.dao" })
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMap;

	private static final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);
	
}
