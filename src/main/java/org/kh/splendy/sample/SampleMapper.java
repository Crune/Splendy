package org.kh.splendy.sample;

import org.apache.ibatis.annotations.*;

/**
 * MyBatis 사용법을 위한 샘플 인터페이스
 * 메서드 위에 어노테이션들이 붙지 않은 메서드들은 xml의 정보를 사용한다.
 * 한줄짜리 sql문은 어노테이션으로 사용하기 바라며
 * 샘플은 샘플로만 사용하고 따로 인터페이스를 생성하기 바람.
 * 
 * @author 최윤
 *
 */
public interface SampleMapper {

/*
	@Insert("insert into test_table (test_column) values (#{testColumn})")
	public void insert(DemoDTO demoDTO) throws Exception;

	@Select("select * from test_table where test_column = #{testColumn}")
	@Results(value = { @Result(property = "testColumn", column = "test_column") })
	public DemoDTO select(String testColumn) throws Exception;

	@Update("update test_table set test_column = #{testColumn}")
	public void update(DemoDTO demoDTO) throws Exception;

	@Delete("delete from test_table where test_column = #{testColumn}")
	public void delete(String testColumn) throws Exception;

	public void set(@Param("testColumn") String testColumn) throws Exception;

	public Test get(@Param("testColumn") String testColumn) throws Exception;
	*/
	public void set(TestVO test) throws Exception;
	
	public void update(TestVO test) throws Exception;

	public TestVO get(@Param("id") String id) throws Exception;
}