package org.kh.splendy.mapper;

import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.kh.splendy.vo.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;

import java.util.List;
/**
 * 
 * 어드민 테이블 관리
 * @author 진규
 *
 */
public interface AdminMapper {
	
	// BASIC CRUD
	String TABLE = "KH_ADMIN";
	
	String COLUMNS = "U_ID, U_ROLE";
	String C_VALUES = "#{id}, #{role}";
	String UPDATES = "U_ROLE=#{role}";
	
	String KEY = "U_ID";
	
	@Insert("insert into "+TABLE+" ( "+COLUMNS+" ) values ( "+C_VALUES+" )")
	public void create(Role role);
	
	@Results(id = "adminResult", value = {
		@Result(property = "id", column = "U_ID"),
		@Result(property = "role", column = "U_ROLE"),
	})
	@Select("select * from "+TABLE+" where "+KEY+"=#{id}")
	public Role read(int id);
	
	@Update("update "+TABLE+" set "+ UPDATES +" where "+KEY+"=#{id}")
	public void update(Role role);
	
	@Delete("delete from "+TABLE+" where "+KEY+"=#{id}")
	public void delete(int id);
	
	// Custom
	@ResultMap("adminResult")
	@Select("select * from "+TABLE)
	public List<Role> readAll() throws Exception;
	
}
