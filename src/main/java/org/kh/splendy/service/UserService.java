package org.kh.splendy.service;

import java.util.HashMap;
import java.util.List;

import org.kh.splendy.vo.UserCore;

public interface UserService {

	UserCore get(String email) throws Exception;

	UserCore join(UserCore newUser) throws Exception;
	
	int checkPassword(HashMap<String, String> map) throws Exception;
	
	List<UserCore> searchEmail(String email) throws Exception;
}
