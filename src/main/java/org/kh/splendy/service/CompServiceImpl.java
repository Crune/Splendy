package org.kh.splendy.service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import lombok.Getter;
import org.kh.splendy.config.assist.Utils;
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
        try {
            if (cardAll.isEmpty()) {
                cardAll = compMap.getDeck();
                for (Card cur : cardAll) {
                    cur.parse();
                }
            }
            if (coinAll.isEmpty()) {
                coinAll = compMap.getToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    @Override @Transactional
    public void transCoin(int rid, int fromId, int toId, int cn_id, int amount) {
        List<PLCoin> coins = compMap.getCoins(rid);
        List<PLCoin> result = transCoinList(rid, fromId, toId, cn_id, amount, coins);
        compMap.updateCoins(result);
        sock.send("/comp/coin/"+rid, result);
    }

    @Override
    public void updateCoins(int rid, List<PLCoin> reqCoins) {
        List<PLCoin> result = new ArrayList<>();

        for (PLCoin cur : reqCoins) {
            if (cur.getRm_id() == rid) {
                result.add(cur);
            }
        }

        compMap.updateCoins(result);
        sock.send("/comp/coin/"+rid, result);
    }

    @Override
    public void updateCards(int rid, List<PLCard> reqCards) {
        List<PLCard> result = new ArrayList<>();

        for (PLCard cur : reqCards) {
            if (cur.getRm_id() == rid) {
                result.add(cur);
            }
        }

        compMap.updateCards(result);
        sock.send("/comp/card/"+rid, result);
    }

    @Override @Transactional
    public void cardPresent(int rid, int toUid, int lv, int amount) {
        List<PLCard> cards = compMap.getCards(rid);
        List<PLCard> result = cardPresentList(toUid, lv, amount, cards);

        compMap.updateCards(result);
        sock.send("/comp/card/"+rid, result);
    }

    @Override @Transactional
    public void startPresent(int rid) {
        List<PLCard> cards = compMap.getCards(rid);
        List<PLCard> result = new ArrayList<>();

        int limits = roomMap.read(rid).getPlayerLimits();
        for (int lv=1; lv<=4; lv++) {
            int amount = 4;
            if (lv==4) {
                amount = limits + 1;
            }
            cardPresentList(0, lv, amount, cards);
        }

        if (result.size() > 0) {
            compMap.updateCards(result);
        }
        sock.send("/comp/card/"+rid, result);
    }

    @Transactional
    private List<PLCard> cardPresentList(int toUid, int lv, int amount, List<PLCard> cards) {
        List<PLCard> temp = new ArrayList<>();
        List<PLCard> result = new ArrayList<>();

        for (PLCard cur : cards) {
            if (cur.getU_id() == 0 && cur.getN_hold() == 0 && getCard(cur.getCd_id()).getLv() == lv) {
                temp.add(cur);
            }
        }

        for (PLCard card : temp) {
        }

        Collections.shuffle(temp);
        int canAmount = (temp.size() < amount) ? temp.size() : amount;
        for (int i=0; i<canAmount; i++) {
            PLCard cur = temp.get(i);
            cur.setU_id(toUid);
            result.add(cur);
        }

        return result;
    }

    @Override @Transactional
    public void holdByDeck(int uid, int rid, int lv) {
        cardPresent(rid, 0, lv, 1);
        transCoin(rid, 0, uid,6,1);
    }

    @Override
    public boolean reqPickCard(PLCard reqGetCard, int uid, int rid) {
        boolean rst = false;

        List<PLCard> result = new ArrayList<>();
        PLCard reqCard = reqGetCard;
        try {
            reqCard = (PLCard) Utils.copy(reqGetCard);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        int reqLv = getCard(reqCard.getCd_id()).getLv();

        List<PLCard> cards = compMap.getCards(rid); // DB에서 해당 방 카드 정보를 가져옴

        PLCard card = pickCard(reqCard.getCd_id(), cards);
        boolean isField = (card.getU_id() == 0 && card.getN_hold() == 1);
        if (reqCard.getN_hold() == 0 && isField) {
            reqCard.setU_id(uid);
            result.add(reqCard);
            result.addAll(cardPresentList(0, reqLv, 1, cards));
            updateCards(rid, result); // 해당 카드 증정
            transCoin(rid, 0, uid,6,1); // 잔여 골드 코인 증정
            rst = true;
        } else {
            List<PLCoin> coins = compMap.getCoins(rid); // DB에서 해당 방 코인 정보를 가져옴

            // 개발카드 생산량과 보유 코인수를 계산함
            Map<Integer, Integer> yield = calcYield(uid, cards);
            Map<Integer, Integer> hasCoins = getCoinAmount(uid, coins);
            Map<Integer, Integer> remaind = getCard(reqCard.getCd_id()).buyCardRCoins(yield, hasCoins);

            if (remaind != null) {
                // 해당 카드 증정
                result.add(reqGetCard);
                result.addAll(cardPresentList(0, reqLv, 1, cards));

                List<PLCoin> resultCoins = new ArrayList<>();

                // 사용자 코인 변동
                for (int cn_id : remaind.keySet()) {
                    PLCoin cur = new PLCoin();
                    cur.setCn_count(remaind.get(cn_id));
                    cur.setRm_id(rid);
                    cur.setU_id(uid);
                    cur.setCn_id(cn_id);
                    resultCoins.add(cur);
                }

                // 필드 코인 변동
                for (PLCoin rstCur : resultCoins) {
                    int beforeAmount = pickCoin(uid, rstCur.getCn_id(), coins).getCn_count();
                    int afterAmount = rstCur.getCn_count();
                    int fieldAmount = pickCoin(0, rstCur.getCn_id(), coins).getCn_count();

                    PLCoin cur = new PLCoin();
                    cur.setCn_count(fieldAmount + beforeAmount - afterAmount);
                    cur.setRm_id(rid);
                    cur.setU_id(0);
                    cur.setCn_id(rstCur.getCn_id());
                    resultCoins.add(cur);
                }
                // 소모 코인 삭감
                updateCoins(rid, resultCoins);

                // 귀족카드 조건 검증
                List<PLCard> afterCards = cards;
                for (PLCard cur : afterCards) {
                    if (cur.getCd_id() == reqCard.getCd_id()) {
                        afterCards.remove(cur);
                    }
                }
                afterCards.add(reqCard);
                Map<Integer, Integer> afterYield = calcYield(uid, afterCards);

                for (PLCard cur : afterCards) {
                    Card curCard = getCard(cur.getCd_id());
                    if (cur.getN_hold() == 1 && curCard.isGetNoble(afterYield)) {
                        cur.setU_id(uid);
                        result.add(cur);
                        afterCards.remove(cur);
                        result.addAll(cardPresentList(0, 4, 1, afterCards));
                    }
                }

                updateCards(rid, result);
                rst = true;
            }


        }

        // 결과 카드변경 전체 전송
        //sock.send("/comp/card/"+rid, result);
	    return rst;
    }

    private PLCard pickCard(int cd_id, List<PLCard> cards) {
        for (PLCard cur : cards) {
            if (cur.getCd_id() == cd_id) {
                return cur;
            }
        }
        return null;
    }

    private PLCoin pickCoin(int uid, int cn_id, List<PLCoin> coins) {
        for (PLCoin cur : coins) {
            if (cur.getU_id() == uid && cur.getCn_id() == cn_id) {
                return cur;
            }
        }
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
	public boolean reqPickCoin(List<PLCoin> reqGetCoins, List<PLCoin> reqDrawCoins, int uid, int rid) {
        List<PLCoin> coins = compMap.getCoins(rid);
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
            if (cur.getU_id() != uid && cur.getRm_id() != rid) {
                tempValid = false;
            }
        }
        if (tempValid) {
            isOnlyOwnValid = true;
        }
        result.clear();

		if (isOnlyOwnValid) {
			Map<Integer, Integer> reqCoinAmount = getCoinAmount(uid, reqGetCoins);

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
			if (getCoinAmount(0, reqGetCoins).get(lastCurId) >= 4) {
				isAct2Valid = (amountTwoCount == 1);
			}

            /* 플레이어는 자신의 차례 끝에 10개보다 많은 토큰을 가질 수 없습니다.(조커 포함.)
             만약 많다면, 오직 10개가 될때까지 반납해야 합니다. */
			int amountCoin = 0;
			for (int curCid : getCoinAmount(uid, coins).keySet()) {
				amountCoin += getCoinAmount(uid, coins).get(curCid);
			}
			for (int curCid : reqCoinAmount.keySet()) {
				amountCoin += reqCoinAmount.get(curCid);
			}
			if (amountCoin > 10) {
				if (reqDrawCoins != null) {
					Map<Integer, Integer> drawCoinAmount = getCoinAmount(uid, reqDrawCoins);
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

			    for (PLCoin getCoin : reqGetCoins) {
                    result.addAll(
                            transCoinList(rid, 0, uid, getCoin.getCn_id(), getCoin.getCn_count(), coins)
                    );
                }
                for (PLCoin getCoin : reqDrawCoins) {
                    result.addAll(
                            transCoinList(rid, uid, 0, getCoin.getCn_id(), getCoin.getCn_count(), coins)
                    );
                }

                compMap.updateCoins(result);
			}
		}
		return rst;
	}

    private List<PLCoin> transCoinList(int rid, int fromId, int toId, int cn_id, int amount, List<PLCoin> coins) {
        List<PLCoin> result = new ArrayList<>();

        PLCoin fromCoin = pickCoin(fromId, cn_id, coins);
        PLCoin toCoin = pickCoin(toId, cn_id, coins);
        if (fromCoin.getCn_count() >= amount) {
            fromCoin.setCn_count(fromCoin.getCn_count() - amount);
            result.add(fromCoin);
            toCoin.setCn_count(toCoin.getCn_count() + amount);
            result.add(toCoin);
        }
        return  result;
    }

    @Override
    public Map<Integer, Integer> scoring(int rid) {
        return scoring(getCardsInDB(rid));
    }

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
            result.put(uid, prevScore + thisCard.getScore());
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
                int yieldType = curCardInfo.getYield();
                if (result.containsKey(yieldType)) {
                    prevYeild = result.get(curCardInfo.getYield());
                }
                result.remove(yieldType);
                result.put(yieldType, ++prevYeild);
            }
        }

        return result;
    }


    @Override
    public Map<Integer, Integer> getCoinAmount(int uid, List<PLCoin> coins) {
        Map<Integer, Integer> fCoins = new HashMap<>();
        for (PLCoin cur : coins) {
            if (cur.getU_id() == uid) {
                fCoins.put(cur.getCn_id(), cur.getCn_count());
            }
        }
        return fCoins;
    }
}
