package org.kh.splendy.mapper;

import org.apache.ibatis.annotations.*;
import org.kh.splendy.vo.Card;
import org.kh.splendy.vo.Coin;
import org.kh.splendy.vo.PLCard;
import org.kh.splendy.vo.PLCoin;
import org.springframework.stereotype.Component;

import java.util.List;

/** KH_PL_ 테이블을 관리하는 MyBatis Mapper
 * @author 최윤 ('16 12.01) */
@Component
public interface CompMapper {

    @Results(id = "cardResult", value = {
            @Result(property = "id", column = "CD_ID"),
            @Result(property = "code", column = "CD_TYPE"),
            @Result(property = "name", column = "CD_NAME"),
            @Result(property = "img", column = "CD_IMG"),
            @Result(property = "info", column = "CD_INFO")
    })
    @Select("select * from KH_CARD")
    List<Card> getDeck();

    @Results(id = "coin", value = {
            @Result(property = "id", column = "CN_ID"),
            @Result(property = "img", column = "CN_IMG"),
            @Result(property = "info", column = "CN_INFO"),
            @Result(property = "name", column = "CN_NAME"),
            @Result(property = "type", column = "CN_TYPE"),
    })
    @Select("select * from KH_COIN")
    List<Coin> getToken();

    @ResultMap("pl_coin")
    @Select("select * from KH_PL_COIN where rm_id = #{rid, jdbcType=INTEGER}")
    List<PLCoin> getCoins(@Param("rid") int rid);

    @ResultMap("pl_card")
    @Select("select * from KH_PL_CARD where rm_id = #{rid, jdbcType=INTEGER}")
    List<PLCard> getCards(@Param("rid") int rid);

    // @Query( in xml )
    void updateCoins(@Param("list") List<PLCoin> list);

    // @Query( in xml )
    void updateCards(@Param("list") List<PLCard> list);
}
