package org.kh.splendy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.kh.splendy.vo.UserCore;

public interface UserMapper {

	// BASIC CRUD
	
	String TABLE = "KH_USER";
	
	String COLUMNS = "U_ID, U_NICK, U_EMAIL, U_PW, U_ENABLED, U_N_LOCKED, U_N_EXPIRED, U_N_CREDENT, U_REG";
	
	String C_VALUES = "KH_USER_SEQ.NEXTVAL, #{nickname}, #{email}, #{password}, #{enabled}, #{notLocked}, #{notExpired}, #{notCredential}, sysdate";
	
	String UPDATES = "U_NICK=#{nickname}, U_EMAIL=#{email}, U_PW=#{password}"
			+ ", U_ENABLED=#{enabled}, U_N_LOCKED=#{notLocked}, U_N_EXPIRED=#{notExpired}"
			+ ", U_N_CREDENT=#{notCredential}";
	
	String KEY = "U_ID";

	@Insert("insert into "+TABLE+" ( "+COLUMNS+" ) values ( "+C_VALUES+" )")
	public void create(UserCore user); // 수정: 클래스 명

	@Results(id = TABLE, value = { // 수정: 컬럼 명, 프로퍼티 명
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
	@Select("select * from "+TABLE+" where "+KEY+"=#{id}")
	public UserCore read(int id); // 수정: 클래스 명

	@Update("update "+TABLE+" set "+UPDATES+" where "+KEY+"=#{id} ")
	public void update(UserCore user);
	
	@Delete("delete from "+TABLE+" where "+KEY+"=#{id}")
	public void delete(int id);
	
	
	// etc
	
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

	@ResultMap("userResult")
	@Select("select * from KH_USER where U_EMAIL=#{email} and U_ENABLED=1")
	public UserCore checkEmail(String email) throws Exception;

	@Select("select count(*) from KH_USER where U_EMAIL=#{email} and U_PW=#{password} and U_ENABLED=1 and U_N_CREDENT=1")
	public int checkPassword(@Param("email") String email, @Param("password") String password) throws Exception;
	
	@Select("select count(*) from KH_USER where U_EMAIL=#{email} and U_PW=#{password} and U_ENABLED=1 and U_N_CREDENT=0")
	public int checkCredent(@Param("email") String email, @Param("password") String password) throws Exception;
	
	@Update("update KH_USER set U_ENABLED=0 where U_EMAIL=#{email}")
	public void disabling(String email) throws Exception;
	
	@Update("update KH_USER set U_PW=#{password} where U_EMAIL=#{email} and U_ENABLED=1")
	public void updatePassword(@Param("email") String email, @Param("password") String password) throws Exception;
	
	@Update("update KH_USER set U_NICK=#{nickname} where U_EMAIL=#{email} and U_ENABLED=1")
	public void updateNickname(@Param("email") String email, @Param("nickname") String nickname) throws Exception;
	
	@Update("update KH_USER set U_ENABLED=0 where U_EMAIL=#{email}")
	public void deleteUser(String email) throws Exception;
	
	// SQL query in xml
	public void credentUser(String code) throws Exception;
	
	@Update("update KH_USER_INNER set U_REG_CODE=#{credent_code} where U_ID =("
			+ "select KH_USER_INNER.U_ID from KH_USER inner join KH_USER_INNER on KH_USER.U_ID = KH_USER_INNER.U_ID "
			+ "where KH_USER_INNER.U_REG_CODE is null and KH_USER.U_EMAIL = #{email})")
	public void updateCredent(@Param("email") String email, @Param("credent_code") String credent_code) throws Exception;
	
	// SQL query in xml
	public void createUser(UserCore user) throws Exception;

	// SQL query in xml
	public void modifyUserWithEmail(UserCore user) throws Exception;
	
	@ResultMap("userResult")
	@Select("select * from KH_USER order by U_ID asc")
	public List<UserCore> selectAll() throws Exception;
	
	@ResultMap("userResult")
	@Select("select * from KH_USER where U_EMAIL=#{email}")
	public UserCore selectOne(@Param("email") String email);
	
	@ResultMap("userResult")
	@Update("update KH_USER set U_NICK=#{nickname}, U_ENABLED=#{enabled}"
			+ ", U_N_LOCKED=#{notLocked}, U_N_EXPIRED=#{notExpired}, U_N_CREDENT=#{notCredential} where "+KEY+"=#{id}")
	public void adminMF(UserCore user) throws Exception;
	
	@ResultMap("userResult")
	@Update("update KH_USER set U_NICK=#{nickname}, U_PW=#{password}, U_ENABLED=#{enabled}"
			+ ", U_N_LOCKED=#{notLocked}, U_N_EXPIRED=#{notExpired}, U_N_CREDENT=#{notCredential} where "+KEY+"=#{id}")
	public void adminPM(UserCore user) throws Exception;
	
	@Select("select U_PW from KH_USER where U_EMAIL=#{email}")
	public String selectPW(String email);
	
	@Select("select U_ID from KH_USER where U_EMAIL=#{email}")
	public int serchID(String email);
}
