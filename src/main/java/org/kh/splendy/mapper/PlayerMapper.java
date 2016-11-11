package org.kh.splendy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.kh.splendy.vo.Player;

/** 플레이어 참가 정보 테이블을 관리하는 MyBatis Mapper
 * @author 최윤 ('16 11.11) */
public interface PlayerMapper {

	// BASIC CRUD
	
	String TABLE = "KH_PLAYER";

	String COLUMNS = "U_ID, RM_ID, PL_STATE, PL_REG, PL_SOCK_ID";
	String C_VALUES = "#{id}, #{roomId}, #{state}, sysdate, #{chatSessionId}";
	String UPDATES = "RM_ID=#{roomId}, PL_STATE=#{state}, PL_SOCK_ID=#{chatSessionId}";
	
	String KEY = "U_ID";

	@Insert("insert into "+TABLE+" ( "+COLUMNS+" ) values ( "+C_VALUES+" )")
	public void create(Player player);
	
	@Results(id = TABLE, value = {
		@Result(property = "id", column = "U_ID"),
		@Result(property = "roomId", column = "RM_ID"),
		@Result(property = "state", column = "PL_STATE"),
		@Result(property = "joinDate", column = "PL_REG"),
		@Result(property = "chatSessionId", column = "PL_SOCK_ID"),
	})
	@Select("select * from "+TABLE+" where "+KEY+"=#{id}")
	public Player read(int id);

	@Update("update "+TABLE+" set "+UPDATES+" where "+KEY+"=#{id} ")
	public Player update(Player player);
	
	@Delete("delete from "+TABLE+" where "+KEY+"=#{id}")
	public void delete(int id);


	// BASIC MAPPER ( count , set )
	
	@Select("select count(*) from "+TABLE+" where "+KEY+"=#{id}")
	public int count(int id);

	@Update("update "+TABLE+" set RM_ID=#{roomId} where "+KEY+"=#{id} ")
	public void setRoom(@Param("id") int id, @Param("roomId") int value);

	@Update("update "+TABLE+" set PL_SOCK_ID=#{chatSessionId} where "+KEY+"=#{id} ")
	public void setSession(@Param("id") int id, @Param("chatSessionId") String value);

	@Update("update "+TABLE+" set PL_STATE=#{state} where "+KEY+"=#{id} ")
	public void setState(@Param("id") int id, @Param("state") int value);


	// Another

	@ResultMap(TABLE)
	@Select("select * from "+TABLE+" where RM_ID=#{roomId}")
	public List<Player> getPlayers(int roomId);	

	
}
