package org.kh.splendy.service;

import org.kh.splendy.vo.UserCore;

public interface UserService {

	UserCore get(String email) throws Exception;

}
