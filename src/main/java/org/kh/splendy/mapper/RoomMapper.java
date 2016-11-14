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
	
	String COLUMNS = "RM_ID, RM_TITLE, RM_PW, RM_INFO, RM_PL_LIMITS, RM_STATE, RM_HOST";
	String C_VALUES = "#{id}, #{title}, #{password}, #{info}, #{playerLimits}, #{state}, #{hostId}";
	String UPDATES = "RM_TITLE=#{title}, RM_PW=#{password}, RM_INFO=#{info}"
			+ ", RM_PL_LIMITS=#{playerLimits}, RM_STATE=#{state}, RM_HOST=#{hostId}";
	
	String KEY = "RM_ID";

	@Insert("insert into "+TABLE+" ( "+COLUMNS+" ) values ( "+C_VALUES+" )")
	public void create(Room room); // 수정: 클래스 명

	@Results(id = TABLE, value = { // 수정: 컬럼 명, 프로퍼티 명
		@Result(property = "id", column = "RM_ID"),
		@Result(property = "title", column = "RM_TITLE"),
		@Result(property = "password", column = "RM_PW"),
		@Result(property = "info", column = "RM_INFO"),
		@Result(property = "playerLimits", column = "RM_PL_LIMITS"),
		@Result(property = "state", column = "RM_STATE"),
		@Result(property = "hostId", column = "RM_HOST")
	})
	@Select("select * from "+TABLE+" where "+KEY+"=#{id}")
	public Player read(int id); // 수정: 클래스 명

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
	
	@Update("update "+TABLE+" set RM_STATE=#{state} where "+KEY+"=#{id} ")
	public void setState(@Param("id") int id, @Param("state") int value);

	@Update("update "+TABLE+" set RM_HOST=#{hostId} where "+KEY+"=#{id} ")
	public void setHostId(@Param("id") int id, @Param("hostId") int value);


	// Another

	@ResultMap(TABLE)
	@Select("select * from "+TABLE+" where RM_STATE="+Room.ST_READY+" or RM_STATE ="+Room.ST_PROGRESS)
	public List<Room> getCurrentRooms();

}
