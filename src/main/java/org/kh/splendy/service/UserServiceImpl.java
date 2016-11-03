package org.kh.splendy.service;

import java.util.List;

import org.kh.splendy.dao.*;
import org.kh.splendy.vo.*;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@MapperScan(basePackages = { "org.kh.splendy.dao" })
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public UserCore get(String email) throws Exception {
		List<UserCore> userList = userDAO.searchEmail(email);
		UserCore lastId = null;
		for (UserCore user : userList) {
			if (user.getEnabled() == 1)
				lastId = user;
		}
		log.info("card info : "+lastId.toString());
		return lastId;
	}
}
