package org.kh.splendy.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
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
			@Result(property = "at_pass", column = "AT_PASS"),
			@Result(property = "at_subject", column = "AT_SUBJECT"),
			@Result(property = "at_content", column = "AT_CONTENT"),
			@Result(property = "at_ip", column = "AT_IP"),
			@Result(property = "at_reply", column = "AT_REPLY"),
			@Result(property = "at_re_step", column = "AT_RE_STEP"),
			@Result(property = "at_re_level", column = "AT_RE_LEVEL"),
			@Result(property = "at_reg_date", column = "AT_REG_DATE"),
			@Result(property = "at_readcount", column = "AT_READCOUNT"),
			@Result(property = "at_category", column = "AT_CATEGORY"),			
		})
	*/
		@Select("select count(*) from KH_ARTICLE")
		public int boardCount() throws Exception;
				
		public String writePro() throws Exception;
		
		@Update("update ARTICLE set AT_RE_STEP=AT_RE_LEVEL+1 where AT_REPLY= #{reply} and AT_RE_STEP> #{re_step}")
		public void reply(HashMap<String, String> map) throws Exception;
		
		@Select("select max(AT_ID) from ARTICLE")
		public int max() throws Exception;
		
		
		
		
			
}
