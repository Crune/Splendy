package org.kh.splendy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.kh.splendy.vo.*;
import org.springframework.stereotype.Component;

@Component
public interface CardMapper {

	
	@Insert("insert into KH_CARD(CD_ID, CD_TYPE, CD_NAME, CD_IMG, CD_INFO"
			+ ") values (KH_CARD_SEQ.NEXTVAL, #{type}, #{name}, #{img}, #{info})")
	public void insert(Card card) throws Exception;

	@Results(id = "cardResult", value = {
		@Result(property = "id", column = "CD_ID"),
		@Result(property = "code", column = "CD_TYPE"),
		@Result(property = "name", column = "CD_NAME"),
		@Result(property = "img", column = "CD_IMG"),
		@Result(property = "info", column = "CD_INFO")
	})
	@Select("select * from KH_CARD")
	public List<Card> selectAll() throws Exception;
	
	@Select("select * from KH_CARD where cd_type like 'CD1%'")
	@ResultMap("cardResult")
	public List<Card> selectLevel_1() throws Exception;
	
	@Select("select * from KH_CARD where cd_type like 'CD2%'")
	@ResultMap("cardResult")
	public List<Card> selectLevel_2() throws Exception;
	
	@Select("select * from KH_CARD where cd_type like 'CD3%'")
	@ResultMap("cardResult")
	public List<Card> selectLevel_3() throws Exception;
	
	@Select("select * from KH_CARD where cd_type like 'NOBL%'")
	@ResultMap("cardResult")
	public List<Card> selectLevel_noble() throws Exception;

	@Select("select * from KH_CARD where CD_ID=#{id}")
	@ResultMap("cardResult")
	public Card select(int id) throws Exception;

	@Update("update KH_CARD"
			+ "set CD_TYPE=#{type}, CD_NAME=#{name}, CD_IMG=#{img}, CD_INFO=#{info}"
			+ "where CD_ID=#{id}")
	public void update(Card card) throws Exception;
		
	@Delete("delete from KH_CARD where CD_ID=#{id}")
	public void delete(int id) throws Exception;
}
