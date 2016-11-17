package org.kh.splendy.service;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.kh.splendy.vo.UserCore;

public interface UserService {

	UserCore get(String email) throws Exception;

	UserCore join(UserCore newUser) throws Exception;
	
	void insertCredent(String credent_code) throws Exception;
	
	int checkPassword(HashMap<String, String> map) throws Exception;
	
	int checkCredent(HashMap<String, String> map) throws Exception;
	
	List<UserCore> searchEmail(String email) throws Exception;
	
	void updateUser(String email, String password, String nickname) throws Exception;
	
	void deleteUser(String email) throws Exception;
	
	void credentUser(String code) throws Exception;
	
	UserCore checkEmail(String email) throws Exception;
	
	void updatePassword(String email, String password) throws Exception;
	
	List<UserCore> selectAll() throws Exception;
	
	UserCore selectOne(String email) throws Exception;
	

}
