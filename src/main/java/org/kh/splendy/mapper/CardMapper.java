package org.kh.splendy.mapper;

import org.apache.ibatis.annotations.*;
import org.kh.splendy.vo.*;

public interface CardMapper {

	@Insert("insert into KH_CARD("
			+ "CD_ID, CD_TYPE, CD_NAME, CD_IMG, CD_INFO"
			+ ") values ("
			+ "KH_CARD_SEQ.next, #{type}, #{name}, #{img}, #{info}"
			+ ")")
	public void insert(Card card) throws Exception;

	@Select("select * from KH_CARD")
	@Results(value = { @Result(property = "testColumn", column = "test_column") })
	public void getAllCard() throws Exception;

}
