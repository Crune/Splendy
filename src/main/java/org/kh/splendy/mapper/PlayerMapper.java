package org.kh.splendy.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;
import org.kh.splendy.vo.Player;
import org.kh.splendy.vo.PlayerListVo;
import org.kh.splendy.vo.WSPlayer;

/** 플레이어 참가 정보 테이블을 관리하는 MyBatis Mapper
 * @author 최윤 ('16 11.11) */
public interface PlayerMapper {

	// BASIC CRUD
	
	String TABLE = "KH_PLAYER";

	String COLUMNS = "U_ID, RM_ID, PL_STATE, PL_REG, PL_SOCK_ID, PL_AUTHCODE";
	String C_VALUES = "#{id}, #{roomId}, #{state}, sysdate, #{chatSessionId}, #{authcode}";
	String UPDATES = "RM_ID=#{roomId}, PL_STATE=#{state}, PL_SOCK_ID=#{chatSessionId}"
			+ ", PL_AUTHCODE=#{authcode}";
	
	String KEY = "U_ID";

	@Insert("insert into "+TABLE+" ( "+COLUMNS+" ) values ( "+C_VALUES+" )")
	public void create(Player player);
	
	@Results(id = TABLE, value = {
		@Result(property = "id", column = "U_ID"),
		@Result(property = "roomId", column = "RM_ID"),
		@Result(property = "state", column = "PL_STATE"),
		@Result(property = "joinDate", column = "PL_REG"),
		@Result(property = "chatSessionId", column = "PL_SOCK_ID"),
		@Result(property = "authcode", column = "PL_AUTHCODE")
	})
	@Select("select * from "+TABLE+" where "+KEY+"=#{id}")
	public Player read(int id);

	@Update("update "+TABLE+" set "+UPDATES+" where "+KEY+"=#{id} ")
	public void update(Player player);
	
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
	
	@Update("update "+TABLE+" set PL_AUTHCODE=#{authcode} where "+KEY+"=#{id} ")
	public void setAuthcode(@Param("id") int id, @Param("authcode") String value);


	// Another

	@ResultMap(TABLE)
	@Select("select * from "+TABLE+" where RM_ID=#{roomId} AND PL_SOCK_ID IS NOT NULL")
	public List<Player> getPlayers(int roomId);

	@Select("select PL_AUTHCODE from "+TABLE+" where "+KEY+"=#{id}")
	public String getCode(int uid);

	@Select("select count(*) from "+TABLE+" where "+KEY+"=#{id, jdbcType=INTEGER} and PL_AUTHCODE=#{authcode, jdbcType=VARCHAR}")
	public int checkCode(@Param("id") int id, @Param("authcode") String code);	

	@ResultMap(TABLE)
	@Select("select * from "+TABLE+" where PL_SOCK_ID=#{id}")
	public Player readBySid(String sid);
	

	@Update("update "+TABLE+" set PL_STATE=#{state} where PL_SOCK_ID=#{id} ")
	public void setStateBySid(@Param("id") String id, @Param("state") int value);
	

	@Select("select PL_SOCK_ID from "+TABLE+" where PL_STATE>0 and PL_SOCK_ID IS NOT NULL")
	public List<String> getActiverSid();

	@Select("SELECT u.U_ID, u.U_NICK, r.RM_ID, r.RM_HOST, pl.PL_STATE"
			+ " FROM KH_PLAYER pl"
			+ " INNER JOIN KH_ROOM r ON r.RM_ID = pl.RM_ID"
			+ " INNER JOIN KH_USER u ON pl.U_ID = u.U_ID"
			+ " WHERE pl.PL_STATE > 0 AND u.U_NICK IS NOT NULL")
	@ResultMap("playerList")
	public List<PlayerListVo> getActiver();

	@Select("SELECT u.U_ID, u.U_NICK, r.RM_ID, r.RM_HOST, pl.PL_STATE"
			+ " FROM KH_PLAYER pl"
			+ " INNER JOIN KH_ROOM r ON r.RM_ID = pl.RM_ID"
			+ " INNER JOIN KH_USER u ON pl.U_ID = u.U_ID"
			+ " WHERE pl.PL_STATE > 0 AND u.U_ID = #{uid}")
	@ResultMap("playerList")
	public PlayerListVo getMeJoinInfo(@Param("uid") int uid);
}
