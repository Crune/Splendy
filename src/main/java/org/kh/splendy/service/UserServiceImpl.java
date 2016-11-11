package org.kh.splendy.service;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.kh.splendy.mapper.*;
import org.kh.splendy.vo.*;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableTransactionManagement
@MapperScan(basePackages = { "org.kh.splendy.dao" })
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMap;

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override //???
	public UserCore get(@Param("email") String email) throws Exception {
		List<UserCore> userList = userMap.searchEmail(email);
		UserCore lastId = null;
		for (UserCore user : userList) {
			if (user.getEnabled() == 1) {
				if (lastId == null) {
					lastId = user;
				} else {
					lastId = new UserCore();
					lastId.setId(-1);
				}
			}
		}
		log.info("card info : "+lastId.toString());
		return lastId;
	}

	@Transactional
	@Override
	public UserCore join(UserCore newUser) throws Exception {
		/*userMap.disabling(newUser.getEmail());*/
		userMap.createUser(newUser);
		return get(newUser.getEmail());
	}

	@Override
	public int checkPassword(HashMap<String, String> map) throws Exception {
		int result = userMap.checkPassword(map);
		return result;
	}

	@Override
	public List<UserCore> searchEmail(String email) throws Exception {
		List<UserCore> list = userMap.searchEmail(email);
		return list;
	}

	@Override
	public void updateUser(HashMap<String, String> map) throws Exception {
		userMap.updateUser(map);
	}

	@Override
	public void deleteUser(String email) throws Exception {
		userMap.deleteUser(email);
	}

	@Override
	public UserCore checkEmail(String email) throws Exception {
		UserCore user = userMap.checkEmail(email);
		return user;
	}

	@Override
	public int checkCredent(HashMap<String, String> map) throws Exception {
		int check = userMap.checkCredent(map);
		return check;
	}

	@Override
	public void credentUser(String code) throws Exception {
		userMap.credentUser(code);
	}

	@Override
	public void insertCredent(String credent_code) throws Exception {
		userMap.insertCredent(credent_code);
	}
}
