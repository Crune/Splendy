package org.kh.splendy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.kh.splendy.vo.UserInner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/** 내부 사용 용도의 개인 정보 테이블을 관리하는 MyBatis Mapper
 * @author 최윤 ('16 11.20) */
public interface UserInnerMapper {

	// BASIC CRUD
	
	String TABLE = "KH_USER_INNER";
	
	String COLUMNS = "U_ID, "
			+ "U_WS_ID, "
			+ "U_WS_AUTH, "
			+ "U_REG_CODE, "
			+ "U_CONNECT, "
			+ "U_ROLE";
	String C_VALUES = "#{id}, "
			+ "#{wsSession, jdbcType=VARCHAR}, "
			+ "#{wsAuthCode, jdbcType=VARCHAR}, "
			+ "#{regCode, jdbcType=VARCHAR}, "
			+ "#{connect, jdbcType=INTEGER}, "
			+ "#{role, jdbcType=VARCHAR}";
	
	String UPDATES = "U_WS_ID=#{wsSession, jdbcType=VARCHAR}, "
			+ "U_WS_AUTH=#{wsAuthCode, jdbcType=VARCHAR}, "
			+ "U_REG_CODE=#{regCode, jdbcType=VARCHAR}, "
			+ "U_CONNECT=#{connect, jdbcType=INTEGER}, "
			+ "U_ROLE=#{role, jdbcType=VARCHAR}";
	
	String KEY = "U_ID";

	@Insert("insert into "+TABLE+" ( "+COLUMNS+" ) values ( "+C_VALUES+" )")
	public void create(UserInner inner); // 수정: 클래스 명

	@ResultMap("userInnerResult")
	@Select("select * from "+TABLE+" where "+KEY+"=#{id}")
	public UserInner read(int id); // 수정: 클래스 명

	@Update("update "+TABLE+" set "+UPDATES+" where "+KEY+"=#{id} ")
	public void update(UserInner inner);
	
	@Delete("delete from "+TABLE+" where "+KEY+"=#{id}")
	public void delete(int id);


	// BASIC MAPPER ( count , set )
	
	@Select("select count(*) from "+TABLE+" where "+KEY+"=#{id}")
	public int count(int id);

	
	@Update("update "+TABLE+" set U_WS_ID=#{value, jdbcType=VARCHAR} where "+KEY+"=#{id} ")
	public void setWSId(@Param("id") int id, @Param("value") String value);

	@Select("select U_WS_ID from "+TABLE+" where "+KEY+"=#{id}")
	public String getWSId(int id);
	
	
	@Update("update "+TABLE+" set U_WS_AUTH=#{value, jdbcType=VARCHAR} where "+KEY+"=#{id} ")
	public void setWSCode(@Param("id") int id, @Param("value") String value);

	@Select("select U_WS_AUTH from "+TABLE+" where "+KEY+"=#{id}")
	public String getWSCode(int id);

	@Select("select count(*) from "+TABLE+" where "+KEY+"=#{id} and U_WS_AUTH=#{value, jdbcType=VARCHAR}")
	public int checkWSCode(@Param("id") int id, @Param("value") String value);

	
	@Update("update "+TABLE+" set U_REG_CODE=#{value, jdbcType=VARCHAR} where "+KEY+"=#{id} ")
	public void setRegCode(@Param("id") int id, @Param("value") String value);

	@Select("select U_REG_CODE from "+TABLE+" where "+KEY+"=#{id}")
	public String getRegCode(int id);

	@Select("select count(*) from "+TABLE+" where "+KEY+"=#{id} and U_REG_CODE=#{value, jdbcType=VARCHAR}")
	public int checkRegCode(@Param("id") int id, @Param("value") String value);

	
	@Update("update "+TABLE+" set U_CONNECT=#{value, jdbcType=INTEGER} where "+KEY+"=#{id} ")
	public void setConnect(@Param("id") int id, @Param("value") int value);

	@Select("select U_CONNECT from "+TABLE+" where "+KEY+"=#{id}")
	public int getConnect(int id);

	
	@Update("update "+TABLE+" set U_ROLE=#{value, jdbcType=VARCHAR} where "+KEY+"=#{id} ")
	public void setRole(@Param("id") int id, @Param("value") String value);

	@Select("select U_ROLE from "+TABLE+" where "+KEY+"=#{id}")
	public String getRole(int id);
	

	// Another

	@ResultMap("userInnerResult")
	@Select("select * from "+TABLE+" where U_WS_ID=#{wsid, jdbcType=VARCHAR}")
	public UserInner readByWSId(String wsid); // 수정: 클래스 명
	
	@ResultMap("userInnerResult")
	@Select("select * from "+TABLE+" order by U_ID asc")
	public List<UserInner> readAdmin();
	
	@Select("select U_ROLE from "+TABLE+" where "+KEY+"=#{id}")
	public List<String> readAuthority(int id);
}
