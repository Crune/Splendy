package org.kh.splendy.dao;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.kh.splendy.vo.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

public interface CardDAO {

	@Insert("insert into KH_CARD("
			+ "CD_ID, CD_TYPE, CD_NAME, CD_IMG, CD_INFO"
			+ ") values ("
			+ "KH_CARD_SEQ.next, #{type}, #{name}, #{img}, #{info}"
			+ ")")
	public void insert(Card card) throws Exception;

	@Select("select * from KH_CARD")
	public List<Card> getAllCard() throws Exception;

}
