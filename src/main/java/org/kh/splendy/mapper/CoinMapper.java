package org.kh.splendy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.kh.splendy.vo.*;
import org.springframework.stereotype.Component;

@Component
public interface CoinMapper {

	@Results(id = "coin", value = {
		@Result(property = "id", column = "CN_ID"),
		@Result(property = "img", column = "CN_IMG"),
		@Result(property = "info", column = "CN_INFO"),
		@Result(property = "name", column = "CN_NAME"),
		@Result(property = "type", column = "CN_TYPE"),
	})
	@Select("select * from KH_COIN")
	public List<Coin> selectAll() throws Exception;
	
}
