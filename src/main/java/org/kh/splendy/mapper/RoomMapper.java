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
import org.kh.splendy.vo.Player;
import org.kh.splendy.vo.Room;

/** 게임방 정보 테이블을 관리하는 MyBatis Mapper
 * @author 최윤 ('16 11.11) */
public interface RoomMapper {

	// BASIC CRUD
	
	String TABLE = "KH_ROOM";
	
	String COLUMNS = "RM_ID, RM_TITLE, RM_PW, RM_INFO, RM_PL_LIMITS, RM_HOST";
	String C_VALUES = "KH_ROOM_SEQ.NEXTVAL, #{title, jdbcType=VARCHAR}, #{password, jdbcType=VARCHAR}, #{info, jdbcType=CLOB}, #{playerLimits, jdbcType=INTEGER}, #{host, jdbcType=INTEGER}";
	String UPDATES = "RM_TITLE=#{title, jdbcType=VARCHAR}, RM_PW=#{password, jdbcType=VARCHAR}, RM_INFO=#{info, jdbcType=CLOB}"
			+ ", RM_PL_LIMITS=#{playerLimits, jdbcType=INTEGER}, RM_HOST=#{host, jdbcType=INTEGER}"
			+ ", RM_WINNER=#{winner, jdbcType=INTEGER}, RM_START=#{start, jdbcType=INTEGER}, RM_END=#{end, jdbcType=INTEGER}";
	
	String KEY = "RM_ID";

	@Insert("insert into "+TABLE+" ( "+COLUMNS+" ) values ( "+C_VALUES+" )")
	public void create(Room room); // 수정: 클래스 명

	/* 첫 실행 구문이 아닐경우 사용 불가능 하기에 xml의 resultMap 사용요망
	@Results(id = "roomResult", value = {
		@Result(property = "id", column = "RM_ID"),
		@Result(property = "title", column = "RM_TITLE"),
		@Result(property = "playerLimits", column = "RM_PL_LIMITS"),
		@Result(property = "hostId", column = "RM_HOST"),
		@Result(property = "password", column = "RM_PW"),
		@Result(property = "info", column = "RM_INFO")
	})*/
	@ResultMap("roomResult")
	@Select("select * from "+TABLE+" where "+KEY+"=#{id}")
	public Room read(int id); // 수정: 클래스 명

	@Update("update "+TABLE+" set "+UPDATES+" where "+KEY+"=#{id} ")
	public void update(Room room);
	
	@Delete("delete from "+TABLE+" where "+KEY+"=#{id}")
	public void delete(int id);


	// BASIC MAPPER ( count , set )
	
	@Select("select count(*) from "+TABLE+" where "+KEY+"=#{id}")
	public int count(int id);

	@Update("update "+TABLE+" set RM_TITLE=#{title} where "+KEY+"=#{id} ")
	public void setTitle(@Param("id") int id, @Param("title") int value);

	@Update("update "+TABLE+" set RM_PW=#{password} where "+KEY+"=#{id} ")
	public void setPassword(@Param("id") int id, @Param("password") int value);
	
	@Update("update "+TABLE+" set RM_INFO=#{info} where "+KEY+"=#{id} ")
	public void setInfo(@Param("id") int id, @Param("info") int value);
	
	@Update("update "+TABLE+" set RM_PL_LIMITS=#{playerLimits} where "+KEY+"=#{id} ")
	public void setPlayerLimits(@Param("id") int id, @Param("playerLimits") int value);

	@Update("update "+TABLE+" set RM_HOST=#{host} where "+KEY+"=#{id} ")
	public void setHostId(@Param("id") int id, @Param("hostId") int value);


	// Another
	@ResultMap("roomResult")
	@Select("select * from "+TABLE+" where RM_END is null and RM_ID <> 0")
	public List<Room> getCurrentRooms();

	@Select("select RM_ID from "+TABLE+" where RM_END is null and RM_HOST = #{uid}")
	public int getMyRoom(@Param("uid") int uid);

	// SQL query is in xml
	public List<Integer> getNotEmptyRoom();

	@Update("update "+TABLE+" set RM_END=sysdate where "+KEY+"=#{id} ")
	public void close(@Param("id") int id);

}
