package org.kh.splendy.mapper;

import java.util.List;
import org.apache.ibatis.annotations.*;
import org.kh.splendy.vo.Player;
import org.kh.splendy.vo.WSPlayer;
import org.springframework.stereotype.Component;

/** 플레이어 참가 정보 테이블을 관리하는 MyBatis Mapper
 * @author 최윤 ('16 11.11) */
@Component
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
	@Select("select * from "+TABLE+" where "+KEY+"=#{uid} and RM_ID = #{rid}")
	public Player read(@Param("uid") int uid, @Param("rid") int rid);

	@Update("update "+TABLE+" set "+UPDATES+" where "+KEY+"=#{id} ")
	public void update(Player player);
	
	@Delete("delete from "+TABLE+" where "+KEY+"=#{uid} and RM_ID = #{rid}")
	public void delete(@Param("uid") int uid, @Param("rid") int rid);


	// BASIC MAPPER ( count , set )
	
	@Select("select count(*) from "+TABLE+" where "+KEY+"=#{uid} and RM_ID = #{rid}")
	public int count(@Param("uid") int uid, @Param("rid") int rid);

	@Update("update "+TABLE+" set PL_IS_IN=#{value, jdbcType=INTEGER} where U_ID=#{uid} and RM_ID=#{rid}")
	public void setIsIn(@Param("uid") int uid, @Param("rid") int rid, @Param("value") int value);

	@Update("update "+TABLE+" set PL_IP=#{value, jdbcType=VARCHAR} where U_ID=#{uid} and RM_ID=#{rid}")
	public void setIp(@Param("uid") int uid, @Param("rid") int rid, @Param("value") String value);

	// Another

	// SQL query in xml
	public void createInitial(@Param("uid") int uid, @Param("rid") int rid);

	// SQL query in xml
	public WSPlayer getWSPlayer(@Param("uid") int uid);

	// SQL query in xml
	public List<WSPlayer> getAllWSPlayer();
	
	// SQL query in xml
	public List<WSPlayer> getInRoomPlayerByRid(@Param("rid") int rid);

	
	@ResultMap("player")
	@Select("select * from "+TABLE+" where RM_ID=#{room} AND PL_IS_IN=1 AND U_ID <> 0")
	public List<Player> getPlayers(int room);


	// SQL query in xml
	public int countIsIn(@Param("uid") int uid);
}
