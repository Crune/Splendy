package org.kh.splendy.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.kh.splendy.vo.UserInner;

public interface UserInnerService {

	UserInner read(int id);
	
	void setRole(@Param("id") int id, @Param("value") String value) throws Exception;
	
	List<Integer> getUserRecord();
	
	void userDisconnect(List<String> list);
}
