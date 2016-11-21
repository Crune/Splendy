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
import org.kh.splendy.vo.Msg;
import org.kh.splendy.vo.Player;
import org.kh.splendy.vo.Room;

/** 메시지 테이블을 관리하는 MyBatis Mapper
 * @author 최윤 ('16 11.14) */
public interface MsgMapper {

	// BASIC CRUD
	
	String TABLE = "KH_PL_MSG";
	
	String COLUMNS = "M_ID, "
			+ "RM_ID, "
			+ "U_ID, "
			+ "M_TYPE, "
			+ "M_CONT, "
			+ "M_REG";
	String C_VALUES = "KH_MSG_SEQ.NEXTVAL, "
			+ "#{rid}, "
			+ "#{uid}, "
			+ "#{type}, "
			+ "#{cont}, "
			+ "sysdate";
	String UPDATES = "RM_ID=#{rid}, "
			+ "U_ID=#{uid}, "
			+ "M_TYPE=#{type}, "
			+ "M_CONT=#{cont}";
	
	String KEY = "M_ID";

	@Insert("insert into "+TABLE+" ( "+COLUMNS+" ) values ( "+C_VALUES+" )")
	public void create(Msg msg); // 수정: 클래스 명

	@ResultMap("msgRst")
	@Select("select * from "+TABLE+" where "+KEY+"=#{id}")
	public Msg read(int id);

	@Update("update "+TABLE+" set "+UPDATES+" where "+KEY+"=#{id} ")
	public void update(Msg msg); // 수정: 클래스 명
	
	@Delete("delete from "+TABLE+" where "+KEY+"=#{id}")
	public void delete(int id);


	// BASIC MAPPER ( count , set )
	
	@Select("select count(*) from "+TABLE+" where "+KEY+"=#{id}")
	public int count(int id);
	
	@Update("update "+TABLE+" set M_TYPE=#{type} where "+KEY+"=#{id} ")
	public void setType(@Param("id") int id, @Param("type") int value);
	
	@Update("update "+TABLE+" set M_CONT=#{cont} where "+KEY+"=#{id} ")
	public void setCont(@Param("id") int id, @Param("cont") int value);


	// Another

	@ResultMap("msgRst")
	@Select("select * from "+TABLE+" where RM_ID=#{rId} and RM_ID=#{rId} and rownum < #{num} order by M_ID DESC")
	public List<Msg> readPrev(@Param("rId") int roomId, @Param("num") int count);

	@ResultMap("msgRst")
	@Select("select * from "+TABLE+" where RM_ID=#{rId}")
	public List<Msg> readAll(@Param("rId") int roomId);

	@ResultMap("msgRst")
	@Select("select * from (select * from "+TABLE+" where RM_ID=#{rId} and M_TYPE = 'chat.new' order by M_ID DESC) where rownum < #{num} order by M_ID ASC")
	public List<Msg> readPrevChat(@Param("rId") int roomId, @Param("num") int count);

	
}
