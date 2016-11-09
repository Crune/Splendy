package org.kh.splendy.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.kh.splendy.vo.Article;
import org.kh.splendy.vo.UserCore;



public interface BoardMapper {
	
	// SQL query in xml
		
		public List<Article> getList(String bName) throws Exception;
		
/*
		@Results(id ="boardResult", value = {
			@Result(property = "u_id", column = "U_ID"),
			@Result(property = "at_id", column = "AT_ID"),
			@Result(property = "bd_id", column = "BD_ID"),
			@Result(property = "pass", column = "AT_PASS"),
			@Result(property = "subject", column = "AT_SUBJECT"),
			@Result(property = "content", column = "AT_CONTENT"),
			@Result(property = "ip", column = "AT_IP"),
			@Result(property = "reply", column = "AT_REPLY"),
			@Result(property = "re_step", column = "AT_RE_STEP"),
			@Result(property = "re_level", column = "AT_RE_LEVEL"),
			@Result(property = "reg_date", column = "AT_REG_DATE"),
			@Result(property = "readcount", column = "AT_READCOUNT"),
			@Result(property = "category", column = "AT_CATEGORY"),			
		})
		*/
		@Select("select count(*) from KH_ARTICLE")
		public int boardCount() throws Exception;
	
		
		
			
}
