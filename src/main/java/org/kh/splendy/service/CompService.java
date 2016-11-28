package org.kh.splendy.service;

import java.util.List;
import java.util.Map;

import org.kh.splendy.vo.Card;
import org.kh.splendy.vo.Coin;
import org.kh.splendy.vo.GameRoom;
import org.kh.splendy.vo.PLCard;
import org.kh.splendy.vo.PLCoin;

public interface CompService {
	
	List<PLCoin> getNewCoins(int rid, int uid);
	List<PLCard> getNewDeck(int rid);

	List<PLCard> reqPickCard(PLCard reqGetCard, int uid, GameRoom room);

	List<PLCoin> reqPickCoin(List<PLCoin> reqGetCoins, List<PLCoin> reqDrawCoins, int uid, GameRoom room);

    void initCompDB(int rid);
	
	Card getCard(int cid);
	Coin getCoin(int cid);

	List<Card> getCards();

    Map<Integer,Integer> scoring(List<PLCard> cards);
}
