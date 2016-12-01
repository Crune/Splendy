package org.kh.splendy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import org.kh.splendy.vo.Card;
import org.kh.splendy.vo.Coin;
import org.kh.splendy.vo.GameRoom;
import org.kh.splendy.vo.PLCard;
import org.kh.splendy.vo.PLCoin;

public interface CompService {

    void initialize();

    Card getCard(int cid);
	Coin getCoin(int cid);

    List<Card> getCardAll();
    List<Coin> getCoinAll();

	List<PLCoin> getCoinsInDB(int rid);
    void setCoinsInDB(List<PLCoin> coins);
    List<PLCard> getCardsInDB(int rid);
    void setCardsInDB(List<PLCard> cards);

	List<PLCoin> reqPickCoin(List<PLCoin> reqGetCoins, List<PLCoin> reqDrawCoins, int uid, GameRoom room);
	List<PLCard> reqPickCard(PLCard reqGetCard, int uid, GameRoom room);

	List<PLCard> checkNobleCard(GameRoom room, Map<Integer, Integer> supplyCoin);
	PLCard checkPickCard(GameRoom room, PLCard reqGetCard);

	boolean checkEnding(List<PLCard> reqGetCard);
    Map<Integer,Integer> scoring(List<PLCard> cards);
}
