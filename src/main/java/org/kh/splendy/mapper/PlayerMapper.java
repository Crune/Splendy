package org.kh.splendy.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;
import org.kh.splendy.vo.Player;
import org.kh.splendy.vo.WSChat;
import org.kh.splendy.vo.WSPlayer;

/** 플레이어 참가 정보 테이블을 관리하는 MyBatis Mapper
 * @author 최윤 ('16 11.11) */
public interface PlayerMapper {

	// BASIC CRUD
	
	String TABLE = "KH_PLAYER";

	String COLUMNS = "U_ID, RM_ID, PL_REG, PL_IS_IN, PL_IP";
	String C_VALUES = "#{id}, #{room}, sysdate, #{isIn, jdbcType=INTEGER}, #{ip, jdbcType=VARCHAR}";
	String UPDATES = "PL_IS_IN=#{isIn, jdbcType=INTEGER}, PL_IP=#{ip, jdbcType=VARCHAR}";
	
	String KEY = "U_ID";

	@Insert("insert into "+TABLE+" ( "+COLUMNS+" ) values ( "+C_VALUES+" )")
	public void create(Player player);
	
	@ResultMap("player")
	@Select("select * from "+TABLE+" where "+KEY+"=#{id}")
	public Player read(int id);

	@Update("update "+TABLE+" set "+UPDATES+" where "+KEY+"=#{id} ")
	public void update(Player player);
	
	@Delete("delete from "+TABLE+" where "+KEY+"=#{id}")
	public void delete(int id);


	// BASIC MAPPER ( count , set )
	
	@Select("select count(*) from "+TABLE+" where "+KEY+"=#{id}")
	public int count(int id);

	@Update("update "+TABLE+" set PL_IS_IN=#{value, jdbcType=INTEGER} where U_ID=#{uid} and RM_ID=#{rid}")
	public void setIsIn(@Param("uid") int uid, @Param("rid") int rid, @Param("value") int value);

	@Update("update "+TABLE+" set PL_IP=#{value, jdbcType=VARCHAR} where U_ID=#{uid} and RM_ID=#{rid}")
	public void setIp(@Param("uid") int uid, @Param("rid") int rid, @Param("value") String value);

	// Another

	// SQL query in xml
	public WSPlayer getWSPlayer(@Param("uid") int uid);

	// SQL query in xml
	public List<WSPlayer> getAllWSPlayer();

	// SQL query in xml
	public List<WSPlayer> getInRoomPlayer(@Param("sid") String sid);

	// SQL query in xml
	public WSPlayer getWSPlayerBySid(@Param("sid") String sid);
	
	@ResultMap("player")
	@Select("select * from "+TABLE+" where RM_ID=#{room} AND PL_IS_IN=1 AND U_ID <> 0")
	public List<Player> getPlayers(int room);

	@Select("select inn.u_ws_id"
			+ " from kh_player pl"
			+ " inner join kh_user_inner inn on inn.u_id = pl.u_id"
			+ " where"
			+ " 	inn.u_ws_id is not null"
			+ " 	and inn.u_connect  > 0"
			+ " 	and pl.pl_is_in   is not null"
			+ " 	and inn.u_id      <> 0")
	public List<String> getActiverSid();
}
