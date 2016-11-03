package org.kh.splendy.dao;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.kh.splendy.vo.*;

public interface UserDAO {

	public void createUser(UserCore user) throws Exception;
	
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
	
	public void modifyUserWithEmail(UserCore user) throws Exception;
	
	@Update("update KH_USER set U_ENABLED=0 where U_EMAIL=#{email}")
	public void disabling(UserCore user) throws Exception;
}
