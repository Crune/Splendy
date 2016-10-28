package org.kh.splendy;

import org.apache.ibatis.annotations.*;
import org.kh.splendy.vo.TestVO;

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