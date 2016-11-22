package org.kh.splendy.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.kh.splendy.vo.UserInner;

/** 개인 정보 테이블을 관리하는 MyBatis Mapper
 * @author 최윤 ('16 11.20) */
public interface UserProfileMapper {

	// BASIC CRUD
	
	String TABLE = "KH_USER_PROFILE";
	
	String COLUMNS = "U_ID, "
			+ "U_LAST_RID, "
			+ "U_ICON, "
			+ "U_TOTAL_TIME, "
			+ "U_WIN, "
			+ "U_LOSE, "
			+ "U_DRAW, "
			+ "U_RATE, "
			+ "U_INFO";
	String C_VALUES = "#{id}, "
			+ "#{lastRId, jdbcType=INTEGER}, "
			+ "#{icon, jdbcType=VARCHAR}, "
			+ "#{totalTime, jdbcType=DATE}, "
			+ "#{win, jdbcType=INTEGER}, "
			+ "#{lose, jdbcType=INTEGER}, "
			+ "#{draw, jdbcType=INTEGER}, "
			+ "#{rate, jdbcType=INTEGER}, "
			+ "#{info, jdbcType=VARCHAR}";
	
	String UPDATES = "U_LAST_RID=#{lastRId, jdbcType=INTEGER}, "
			+ "U_ICON=#{icon, jdbcType=VARCHAR}, "
			+ "U_TOTAL_TIME=#{totalTime, jdbcType=DATE}, "
			+ "U_WIN=#{win, jdbcType=INTEGER}, "
			+ "U_LOSE=#{lose, jdbcType=INTEGER}, "
			+ "U_DRAW=#{draw, jdbcType=INTEGER}, "
			+ "U_RATE=#{rate, jdbcType=INTEGER}, "
			+ "U_INFO=#{info, jdbcType=VARCHAR}";
	
	String KEY = "U_ID";

	@Insert("insert into "+TABLE+" ( "+COLUMNS+" ) values ( "+C_VALUES+" )")
	public void create(UserProfileMapper inner); // 수정: 클래스 명

	@ResultMap("userProfile")
	@Select("select * from "+TABLE+" where "+KEY+"=#{id}")
	public UserProfileMapper read(int id); // 수정: 클래스 명

	@Update("update "+TABLE+" set "+UPDATES+" where "+KEY+"=#{id} ")
	public void update(UserProfileMapper var);
	
	@Delete("delete from "+TABLE+" where "+KEY+"=#{id}")
	public void delete(int id);


	// BASIC MAPPER ( count , set )
	
	@Select("select count(*) from "+TABLE+" where "+KEY+"=#{id}")
	public int count(int id);


	@Update("update "+TABLE+" set U_LAST_RID=#{value} where "+KEY+"=#{id} ")
	public void setLastRoom(@Param("id") int id, @Param("value") int value);
	
	@Select("select U_LAST_RID from "+TABLE+" where "+KEY+"=#{id}")
	public int getLastRoom(int id);
	
	@Select("select count(*) from "+TABLE+" where "+KEY+"=#{id} and U_LAST_RID=#{value}")
	public int checkLastRoom(@Param("id") int id, @Param("value") int value);


	@Update("update "+TABLE+" set U_ICON=#{value} where "+KEY+"=#{id} ")
	public void setIcon(@Param("id") int id, @Param("value") String value);
	
	@Select("select U_ICON from "+TABLE+" where "+KEY+"=#{id}")
	public String getIcon(int id);
	
	@Select("select count(*) from "+TABLE+" where "+KEY+"=#{id} and U_ICON=#{value}")
	public int checkIcon(@Param("id") int id, @Param("value") String value);


	@Update("update "+TABLE+" set U_INFO=#{value} where "+KEY+"=#{id} ")
	public void setInfo(@Param("id") int id, @Param("value") String value);
	
	@Select("select U_INFO from "+TABLE+" where "+KEY+"=#{id}")
	public String getInfo(int id);
	
	@Select("select count(*) from "+TABLE+" where "+KEY+"=#{id} and U_INFO=#{value}")
	public int checkInfo(@Param("id") int id, @Param("value") String value);

	

	// Another

}
