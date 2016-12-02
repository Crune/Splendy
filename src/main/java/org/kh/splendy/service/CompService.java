package org.kh.splendy.service;

import java.util.List;
import java.util.Map;

import org.kh.splendy.vo.Card;
import org.kh.splendy.vo.Coin;
import org.kh.splendy.vo.PLCard;
import org.kh.splendy.vo.PLCoin;
import org.springframework.transaction.annotation.Transactional;

public interface CompService {

    CompService initialize();

    Card getCard(int cid);
	Coin getCoin(int cid);

    List<Card> getCardAll();
    List<Coin> getCoinAll();

	List<PLCoin> getCoinsInDB(int rid);
    void setCoinsInDB(List<PLCoin> coins);
    List<PLCard> getCardsInDB(int rid);
    void setCardsInDB(List<PLCard> cards);

	boolean reqPickCoin(List<PLCoin> reqGetCoins, List<PLCoin> reqDrawCoins, int uid, int rid);

    @Transactional
    void transCoin(int rid, int fromId, int toId, int cn_id, int amount);

    void updateCoins(int rid, List<PLCoin> reqCoins);

    @Transactional
    void updateCards(int rid, List<PLCard> reqCard);

    void cardPresent(int rid, int toUid, int lv, int amount);

    @Transactional
    void startPresent(int rid);

    void holdByDeck(int uid, int rid, int lv);

    boolean reqPickCard(PLCard reqGetCard, int uid, int rid);

    boolean checkEnding(List<PLCard> reqGetCard);

    Map<Integer, Integer> scoring(int rid);

    Map<Integer, Integer> calcYield(int reqUid, List<PLCard> cards);

    Map<Integer, Integer> getCoinAmount(int uid, List<PLCoin> coins);
}
