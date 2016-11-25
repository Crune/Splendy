package org.kh.splendy.service;

import java.util.List;

import org.kh.splendy.mapper.UserInnerMapper;
import org.kh.splendy.vo.UserInner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Service
@EnableTransactionManagement
public class UserInnerServiceImpl implements UserInnerService {
	
	@Autowired
	private UserInnerMapper innerMap;
	
	private static final Logger log = LoggerFactory.getLogger(UserInnerServiceImpl.class);

	@Override
	public List<UserInner> readAdmin() throws Exception {
		List<UserInner> list = innerMap.readAdmin();
		log.info("admin list read");
		return list;
	}

	@Override
	public void setRole(int id, String value) throws Exception {
		innerMap.setRole(id, value);
	}
}
