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

	@SuppressWarnings("unused")
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
		for (Coin coin : coinAll) {
			PLCoin rst = new PLCoin();
			rst.setRm_id(rid);
			rst.setCn_id(coin.getId());
			rst.setU_id(uid);
			if (coin.getId() == 6) {
				rst.setCn_count(5);
			} else {
				rst.setCn_count(startAmount);
			}
			coins.add(rst);
		}

		return coins;
	}
	
	@Override
	public List<PLCard> getNewDeck(int rid) {
		init();

		List<PLCard> cards = new ArrayList<PLCard>();

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
	public List<PLCoin> reqPickCoin(List<PLCoin> reqGetCoins, List<PLCoin> reqDrawCoins, int uid, GameRoom room) {
        List<PLCoin> result = new ArrayList<>();

		boolean rst = false;
		boolean isAct1Valid = false, isAct2Valid = false;
		boolean isHasCoinValid = false;

		if (room.isMyTurn(uid) && room.isPlaying()) {
			Map<Integer, Integer> reqCoinAmount = room.getCoinCount(reqGetCoins);

			// 서로 다른 색의 보석 토큰 3개 가져가기
			int amountOneCount = 0;
			for (int curCid : reqCoinAmount.keySet()) {
				if (reqCoinAmount.get(curCid) == 1) {
					amountOneCount++;
				}
			}
			isAct1Valid = (amountOneCount == 3);

			// 같은 색의 보석 토큰 2개 가져가기.
			int amountTwoCount = 0;
			int lastCurId = -1;
			for (int curCid : reqCoinAmount.keySet()) {
				if (reqCoinAmount.get(curCid) == 2) {
					amountTwoCount++;
					lastCurId = curCid;
				}
			}
            /* 이 액션은 플레이어가 보석 토큰을 가져올때,
             선택한 색상의 토큰이 적어도 4개가 있어야만 가능합니다. */
			if (room.getCoinAmount(0).get(lastCurId) >= 4) {
				isAct2Valid = (amountTwoCount == 1);
			}

            /* 플레이어는 자신의 차례 끝에 10개보다 많은 토큰을 가질 수 없습니다.(조커 포함.)
             만약 많다면, 오직 10개가 될때까지 반납해야 합니다. */
			int amountCoin = 0;
			for (int curCid : room.getCoinAmount(uid).keySet()) {
				amountCoin += room.getCoinAmount(uid).get(curCid);
			}
			for (int curCid : reqCoinAmount.keySet()) {
				amountCoin += reqCoinAmount.get(curCid);
			}
			if (amountCoin > 10) {
				if (reqDrawCoins != null) {
					Map<Integer, Integer> drawCoinAmount = room.getCoinCount(reqGetCoins);
					for (int curCid : drawCoinAmount.keySet()) {
						amountCoin -= drawCoinAmount.get(curCid);
					}
					if (amountCoin <= 10) {
						isHasCoinValid = true;
					}
				}
			} else {
				isHasCoinValid = true;
			}

			// 검증 결과를 산출 하고 통과시 수행
			rst = ((isAct1Valid || isAct2Valid) && isHasCoinValid);
			if (rst) {
                result = room.pickCoins(reqGetCoins, reqDrawCoins, uid);
			}
		}
		return result;
	}

	@Override
	public void initCompDB(int rid) {
		Room room = roomMap.read(rid);
		int plLimits = room.getPlayerLimits();
	}

	@Override
	public List<Card> getCards() {
		init();
		return cardAll;
	}
}
