package org.kh.splendy.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.junit.runners.Parameterized.Parameters;
import org.kh.splendy.vo.Article;
import org.kh.splendy.vo.UserCore;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



public interface BoardMapper {
	/*
	String TABLE = "KH_ARTICLE";
	
	String COLUMNS = "AT_ID, AT_SUBJECT, AT_PASS, AT_CONTENT, AT_IP, AT_REPLY,"
			+ "AT_RE_LEVEL, AT_RE_STEP";
	
	String C_VALUES = "KH_BOARD_SEQ.NEXTVAL, #{at_subject}, #{at_pass},"
			+ "	#{at_content}, #{at_ip}, #{at_reply},"
			+ "#{at_re_level}, #{at_re_step}";
	
	//String UPDATES = "U_NICK=#{nickname}, U_EMAIL=#{email}, U_PW=#{password}"
	//		+ ", U_ENABLED=#{enabled}, U_N_LOCKED=#{notLocked}, U_N_EXPIRED=#{notExpired}"
	//		+ ", U_N_CREDENT=#{notCredential}";
	
	//String KEY = "U_ID";

	@Insert("insert into "+TABLE+" ( "+COLUMNS+" ) values ( "+C_VALUES+" )")
	public void writePro(Article article); // 수정: 클래스 명
	
	@Results(id = TABLE, value = { // 수정: 컬럼 명, 프로퍼티 명		
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
	// SQL query in xml
		
		public List<Article> getList(String bName) throws Exception;		
/*
 		@Results(id ="article", value = {
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
		
		@Update("update KH_ARTICLE set AT_RE_STEP=AT_RE_LEVEL+1 where AT_REPLY= #{reply} and AT_RE_STEP> #{re_step}")
		public void reply(HashMap<String, String> map) throws Exception;
		
		@Select("select max(AT_ID) from KH_ARTICLE")
		public int max() throws Exception;		
		
		public void writePro(@ModelAttribute("BoardVO") Article article) throws Exception;
		
		public Article getDetail(int at_Id) throws Exception;
		
		public void readCount(int at_Id) throws Exception; 
		
		public void updateBoard(int at_Id) throws Exception;
			
}
