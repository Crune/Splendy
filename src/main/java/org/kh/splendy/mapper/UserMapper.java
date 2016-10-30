package org.kh.splendy.mapper;

import org.apache.ibatis.annotations.Insert;
import org.kh.splendy.vo.UserInfo;

public interface UserMapper {

	@Insert("insert into kh_user (u_nick, u_email, u_pw) values (#{nickname},#{email},#{password})")
	public void insert(UserInfo user) throws Exception;

}
