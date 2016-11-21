package org.kh.splendy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.kh.splendy.vo.PropInDB;
/**
 * 
 * 서비스 테이블 관리
 * @author 진규
 *
 */
public interface ServMapper {

	// BASIC CRUD
	
	String TABLE = "KH_SERV";
	
	String COLUMNS = "S_KEY, S_VALUE";
	String C_VALUES = "#{key}, #{value}";
	String UPDATES = "S_VALUE=#{value}";
	
	String KEY = "S_KEY";
	
	@Insert("insert into "+TABLE+" ( "+COLUMNS+" ) values ( "+C_VALUES+" )")
	public void create(PropInDB prop);
	
	@Results(id = "servResult", value = {
		@Result(property = "key", column = "S_KEY"),
		@Result(property = "value", column = "S_VALUE"),
	})
	@Select("select * from "+TABLE+" where "+KEY+"=#{key}")
	public PropInDB read(String key);
	
	@Update("update "+TABLE+" set "+ UPDATES +" where "+KEY+"=#{key}")
	public void update(PropInDB prop);
	
	@Delete("delete from "+TABLE+" where "+KEY+"=#{key}")
	public void delete(String key);
	
	// Custom
	@ResultMap("servResult")
	@Select("select * from "+TABLE)
	public List<PropInDB> readAll() throws Exception;
	
}
