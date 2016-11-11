package org.kh.splendy.mapper;

import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Result;

import org.kh.splendy.vo.PropInDB;
import org.kh.splendy.vo.UserCore;

import java.util.List;

public interface AdminMapper {
	
	@Results(id = "adminResult", value = {
		@Result(property = "key", column = "S_KEY"),
		@Result(property = "value", column = "S_VALUE"),
	})
	@Select("select * from KH_SERV")
	public List<PropInDB> serchServ() throws Exception;
	
	@Update("update KH_SERV set S_VALUE = on where S_KEY = ${key}")
	public void servOn(String key) throws Exception;
	
	@Update("update KH_SERV set S_VALUE = off where S_KEY = ${key}")
	public void servOff(String key) throws Exception;
	

}
