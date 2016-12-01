package org.kh.splendy.service;

import java.util.*;

import lombok.Getter;
import org.kh.splendy.vo.*;
import org.kh.splendy.mapper.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableTransactionManagement
public class CompServiceImpl implements CompService {

    @Autowired private SocketService sock;

	@Autowired private CompMapper compMap;
	@Autowired private RoomMapper roomMap;

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(CompServiceImpl.class);

	@Getter private List<Card> cardAll = new ArrayList<>();
    @Getter private List<Coin> coinAll = new ArrayList<>();

    @Override
	public CompService initialize() {
        if (cardAll.isEmpty()) {
            try {
                cardAll = compMap.getDeck();
                for (Card cur : cardAll) {
                    cur.parse();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (coinAll.isEmpty()) {
            try {
                coinAll = compMap.getToken();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
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
	
	@Override public List<PLCoin> getCoinsInDB(int rid) { return compMap.getCoins(rid); }
	@Override public void setCoinsInDB(List<PLCoin> coins) {
		compMap.updateCoins(coins);
	}
	@Override public List<PLCard> getCardsInDB(int rid) {
		return compMap.getCards(rid);
	}
	@Override public void setCardsInDB(List<PLCard> cards) { compMap.updateCards(cards); }

    @Override
    public List<PLCard> reqPickCard(PLCard reqGetCard, int uid, GameRoom room) {
		WSComp result = null;

        // TODO 해당 카드를 가져올 조건 검증
        PLCard resultCard = checkPickCard(room, reqGetCard);

        if (resultCard != null) {
            // 해당 카드 증정
            result.getCards().add(room.pickCard(reqGetCard));

            // TODO 귀족카드 조건 검증
            Map<Integer, Integer> supplyCoin = new HashMap<>();
            List<PLCard> nobles = checkNobleCard(room, supplyCoin);
            if (nobles != null) {
                // 만족하는 귀족카드 증정
                result.getCards().addAll(room.pickCard(nobles));
            }

            // TODO 홀딩시 잔여 골드 코인 증정
            PLCoin gold = room.pickGold(uid);
            result.getCoins().add(gold);
            sock.send("/comp/coin/" + room.getRoom(), result.getCoins());
        }

        setCardsInDB(result.getCards());
        setCoinsInDB(result.getCoins());
        // 결과 카드변경 전체 전송
	    return result.getCards();
    }

    @Override
    public List<PLCard> checkNobleCard(GameRoom room, Map<Integer, Integer> supplyCoin) {
	    return null;
    }

    @Override
    public PLCard checkPickCard(GameRoom room, PLCard reqGetCard) {
        Map<Integer, Integer> score = scoring(compMap.getCards(room.getRoom()));
        return null;
    }

    @Override
    public boolean checkEnding(List<PLCard> reqGetCard) {
        Map<Integer, Integer> score  = scoring(reqGetCard);
        boolean isGameEnd = false;
        for (int curScore : score.keySet()) {
            if (curScore >= 15) {
                isGameEnd = true;
            }
        }
        return isGameEnd;
    }

    @Override @Transactional
	public List<PLCoin> reqPickCoin(List<PLCoin> reqGetCoins, List<PLCoin> reqDrawCoins, int uid, GameRoom room) {
        List<PLCoin> result = new ArrayList<>();

		boolean rst = false;
        boolean isOnlyOwnValid = false;
		boolean isAct1Valid = false, isAct2Valid = false;
		boolean isHasCoinValid = false;

		// 해당방에 있는 자기 자신만의 코인을 사용해야 함
        result.addAll(reqGetCoins);
        result.addAll(reqDrawCoins);
        boolean tempValid = true;
        for (PLCoin cur : result) {
            if (cur.getU_id() != uid && cur.getRm_id() != room.getRoom()) {
                tempValid = false;
            }
        }
        if (tempValid) {
            isOnlyOwnValid = true;
        }
        result.clear();

		if (isOnlyOwnValid && room.isMyTurn(uid) && room.isPlaying()) {
			Map<Integer, Integer> reqCoinAmount = room.getCoinCount(reqGetCoins, uid, room.getRoom());

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
					Map<Integer, Integer> drawCoinAmount = room.getCoinCount(reqGetCoins, uid, room.getRoom());
					for (int curCid : drawCoinAmount.keySet()) {
					    if (drawCoinAmount.get(curCid) > 0) {
                            amountCoin -= drawCoinAmount.get(curCid);
                        }
					}
					if (amountCoin <= 10) {
						isHasCoinValid = true;
					}
				}
			} else {
			    // 10개보다 많지 않다면 검증은 통과하지만, 버림은 무시합니다.
				isHasCoinValid = true;
                reqDrawCoins.clear();
			}

			// 검증 결과를 산출 하고 통과시 수행
			rst = ((isAct1Valid || isAct2Valid) && isHasCoinValid);
			if (rst) {
                result = room.pickCoins(reqGetCoins, reqDrawCoins);
                setCoinsInDB(result);
			}
		}
		return result;
	}

	@Override
	public Map<Integer, Integer> scoring(List<PLCard> cards) {
        Map<Integer, Integer> result = new HashMap<>();

        for (PLCard cur : cards) {
            int uid = cur.getU_id();
            int cdid = cur.getCd_id();

            Card thisCard = null;
            for (Card curCard : cardAll) {
                if (curCard.getId() == cdid) {
                    thisCard = curCard;
                }
            }

            int prevScore = 0;
            if (result.containsKey(uid)) {
                prevScore = result.get(uid);
                result.remove(uid);
            }
            result.put(uid, prevScore + thisCard.getPoint());
        }

		return result;
	}


    @Override
    public Map<Integer, Integer> calcYield(int reqUid, List<PLCard> cards) {
        Map<Integer, Integer> result = new HashMap<>();

        for (PLCard cur : cards) {
            if (cur.getU_id() == reqUid) {
                Card curCardInfo = null;
                for (Card curCard : cardAll) {
                    if (curCard.getId() == cur.getCd_id()) {
                        curCardInfo = curCard.parse();
                    }
                }

                int prevYeild = 0;
                int yieldType = curCardInfo.getYieldType();
                if (result.containsKey(yieldType)) {
                    prevYeild = result.get(curCardInfo.getYieldType());
                }
                result.remove(yieldType);
                result.put(yieldType, ++prevYeild);
            }
        }

        return result;
    }
}
