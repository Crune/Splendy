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
	
	String TABLE = "KH_MSG";
	
	String COLUMNS = "M_ID, RM_ID, M_TYPE, M_AUTHOR, M_CONT, M_REG";
	String C_VALUES = "KH_MSG_SEQ.NEXTVAL, #{roomId}, #{type}, #{author}, #{cont}, sysdate";
	String UPDATES = "RM_ID=#{roomId}, M_TYPE=#{type}, M_AUTHOR=#{author}"
			+ ", M_CONT=#{cont}, M_REG=#{reg}";
	
	String KEY = "M_ID";

	@Insert("insert into "+TABLE+" ( "+COLUMNS+" ) values ( "+C_VALUES+" )")
	public void create(Msg msg); // 수정: 클래스 명

	@Results(id = TABLE, value = { // 수정: 컬럼 명, 프로퍼티 명
		@Result(property = "msgId", column = "M_ID"),
		@Result(property = "roomId", column = "RM_ID"),
		@Result(property = "type", column = "M_TYPE"),
		@Result(property = "author", column = "M_AUTHOR"),
		@Result(property = "cont", column = "M_CONT"),
		@Result(property = "reg", column = "M_REG")
	})
	@Select("select * from "+TABLE+" where "+KEY+"=#{id}")
	public Msg read(int id);

	@Update("update "+TABLE+" set "+UPDATES+" where "+KEY+"=#{id} ")
	public void update(Msg msg); // 수정: 클래스 명
	
	@Delete("delete from "+TABLE+" where "+KEY+"=#{id}")
	public void delete(int id);


	// BASIC MAPPER ( count , set )
	
	@Select("select count(*) from "+TABLE+" where "+KEY+"=#{id}")
	public int count(int id);

	@Update("update "+TABLE+" set RM_ID=#{roomId} where "+KEY+"=#{id} ")
	public void setRoomId(@Param("id") int id, @Param("roomId") int value);

	@Update("update "+TABLE+" set M_TYPE=#{type} where "+KEY+"=#{id} ")
	public void setType(@Param("id") int id, @Param("type") int value);
	
	@Update("update "+TABLE+" set M_AUTHOR=#{author} where "+KEY+"=#{id} ")
	public void setAuthor(@Param("id") int id, @Param("author") int value);
	
	@Update("update "+TABLE+" set M_CONT=#{cont} where "+KEY+"=#{id} ")
	public void setCont(@Param("id") int id, @Param("cont") int value);
	
	@Update("update "+TABLE+" set RM_STATE=#{state} where "+KEY+"=#{id} ")
	public void setState(@Param("id") int id, @Param("state") int value);


	// Another

	@ResultMap(TABLE)
	@Select("select * from "+TABLE+" where RM_ID=${rId} and rownum < ${num} order by M_ID DESC")
	public List<Msg> readPrev(@Param("rId") int roomId, @Param("num") int count);

	@ResultMap(TABLE)
	@Select("select * from "+TABLE+" where RM_ID=${rId}")
	public List<Msg> readAll(@Param("rId") int roomId);

	
}
