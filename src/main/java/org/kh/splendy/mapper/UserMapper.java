package org.kh.splendy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.kh.splendy.vo.UserCore;

public interface UserMapper {

	@Results(id = "userResult", value = {
		@Result(property = "id", column = "U_ID"),
		@Result(property = "nickname", column = "U_NICK"),
		@Result(property = "email", column = "U_EMAIL"),
		@Result(property = "password", column = "U_PW"),
		@Result(property = "enabled", column = "U_ENABLED"),
		@Result(property = "notLocked", column = "U_N_LOCKED"),
		@Result(property = "notExpired", column = "U_N_EXPIRED"),
		@Result(property = "notCredential", column = "U_N_CREDENT"),
		@Result(property = "reg", column = "U_REG"),
	})
	@Select("select * from KH_USER where U_EMAIL=#{email}")
	public List<UserCore> searchEmail(String email) throws Exception;

	@Select("select count(*) from KH_USER where U_EMAIL=#{email} and U_PW=#{password}")
	public int checkPassword(String email, String password) throws Exception;
	
	@Update("update KH_USER set U_ENABLED=0 where U_EMAIL=#{email}")
	public void disabling(String email) throws Exception;

	// SQL query in xml
	public void createUser(UserCore user) throws Exception;

	// SQL query in xml
	public void modifyUserWithEmail(UserCore user) throws Exception;
}
