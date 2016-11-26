package org.kh.splendy.service;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.kh.splendy.vo.UserInner;

public interface UserInnerService {

	List<UserInner> readAdmin() throws Exception;
	
	void setRole(@Param("id") int id, @Param("value") String value) throws Exception;
}
