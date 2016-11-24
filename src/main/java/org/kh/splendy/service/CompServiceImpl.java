package org.kh.splendy.service;

import java.util.*;
import org.kh.splendy.vo.*;
import org.kh.splendy.mapper.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Service
@EnableTransactionManagement
public class CompServiceImpl implements CompService {

	@Autowired private CardMapper cardMap;
	@Autowired private CoinMapper coinMap;
	@Autowired private RoomMapper roomMap;

	private static final Logger log = LoggerFactory.getLogger(CompServiceImpl.class);

	private static List<Card> cardAll = new ArrayList<Card>();
	private static List<Coin> coinAll = new ArrayList<Coin>();

	private void init() {
		if (cardAll.isEmpty()) {
			try {
				cardAll = cardMap.selectAll();
				for (Card cur : cardAll) {
					cur.parse();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (coinAll.isEmpty()) {
			try {
				coinAll = coinMap.selectAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Card getCard(int cid) {
		for (Card card : cardAll) {
			if (card.getId() == cid) {
				return card;
			}
		}
		return null;
	}

	@Override
	public Coin getCoin(int cid) {
		for (Coin coin : coinAll) {
			if (coin.getId() == cid) {
				return coin;
			}
		}
		return null;
	}
	
	@Override
	public List<PLCoin> getNewCoins(int rid, int uid) {
		init();

		int startAmount = 0;
		if (uid < 1) {
			Room room = roomMap.read(rid);
			int plLimits = room.getPlayerLimits();
			startAmount = plLimits+2;
			if (plLimits == 4) {
				startAmount++;
			}
		}
		
		List<PLCoin> coins = new ArrayList<PLCoin>();

		int count =0;
		for (Coin coin : coinAll) {
			PLCoin rst = new PLCoin();
			rst.setRm_id(rid);
			rst.setCn_id(coin.getId());
			rst.setU_id(uid);
			rst.setCn_count(startAmount);

			coins.add(rst);
		}

		return coins;
	}
	
	@Override
	public List<PLCard> getNewDeck(int rid) {
		init();

		List<PLCard> cards = new ArrayList<PLCard>();

		int count =0;
		for (Card card : cardAll) {
			PLCard rst = new PLCard();
			rst.setRm_id(rid);
			rst.setCd_id(card.getId());
			rst.setN_hold(0);
			rst.setU_id(0);

			cards.add(rst);
		}
		Collections.shuffle(cards);

		return cards;
	}

	@Override
	public void initCompDB(int rid) {
		Room room = roomMap.read(rid);
		int plLimits = room.getPlayerLimits();
	}
}
