package org.kh.splendy.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.kh.splendy.vo.UserCore;

public interface UserService {

	UserCore get(String email) throws Exception;
		
	int checkPassword(String email, String password) throws Exception;
	
	int checkCredent(String email, String password) throws Exception;
	
	List<UserCore> searchEmail(String email) throws Exception;
	
	void updatePassword(String email, String password) throws Exception;
	
	void updateNickname(String email, String nickname) throws Exception;
	
	void deleteUser(String email) throws Exception;
	
	void credentUser(String code) throws Exception;
	
	public void createUser(UserCore user) throws Exception;
	
	UserCore checkEmail(String email) throws Exception;
	
	List<UserCore> selectAll() throws Exception;
	
	UserCore selectOne(String email) throws Exception;
	
	void sendEmail(UserCore user, String credent_code) throws Exception;
	
	void sendPw(String email, String new_pw) throws Exception;
	
	void joinUser(UserCore user, String credent_code) throws Exception;

	int findPw(String email, String new_pw) throws Exception;
	
	void adminMF(UserCore user) throws Exception;

	void updateUser(UserCore user, String email) throws Exception;
}
