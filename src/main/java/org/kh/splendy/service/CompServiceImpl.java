package org.kh.splendy.service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

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
        Map<Integer, PLCard> cards = compMap.getCards(rid).stream().collect(Collectors.toMap(PLCard::getCd_id, cur -> cur));
        List<PLCard> result = cardPresentList(toUid, lv, amount, cards);

        compMap.updateCards(result);
        sock.send("/comp/card/"+rid, result);
    }

    @Override @Transactional
    public void startPresent(int rid) {
        Map<Integer, PLCard> cards = compMap.getCards(rid).stream().collect(Collectors.toMap(PLCard::getCd_id, cur -> cur));
        List<PLCard> result = new ArrayList<>();

        int limits = roomMap.read(rid).getPlayerLimits();
        for (int lv=1; lv<=4; lv++) {
            int amount = 4;
            if (lv==4) {
                amount = limits + 1;
            }
            result.addAll(cardPresentList(0, lv, amount, cards));
        }

        if (result.size() > 0) {
            compMap.updateCards(result);
        }
        sock.send("/comp/card/"+rid, result);
    }

    @Transactional
    private List<PLCard> cardPresentList(int toUid, int lv, int amount, Map<Integer, PLCard> cards) {
        List<PLCard> targetList = new ArrayList<>();
        List<PLCard> changedList = new ArrayList<>();
        Map<Integer, PLCard> temp = new HashMap<>(cards);
        for (int cdid : temp.keySet()) {
            PLCard cur = cards.get(cdid);
            if (cur.getU_id() == 0 && cur.getN_hold() == 0 && getCard(cur.getCd_id()).getLv() == lv) {
                targetList.add(cur);
                cards.remove(cdid);
            }
        }
        Collections.shuffle(targetList);
        int canAmount = (targetList.size() < amount) ? targetList.size() : amount;
        for (int i=0; i<canAmount; i++) {
            targetList.get(i).setN_hold(1);
            targetList.get(i).setU_id(toUid);
            changedList.add(targetList.get(i));
            cards.put(targetList.get(i).getCd_id(), targetList.get(i));
        }
        return changedList;
    }

    @Override @Transactional
    public void holdByDeck(int uid, int rid, int lv) {
        cardPresent(rid, 0, lv, 1);
        transCoin(rid, 0, uid,6,1);
    }

    @Override
    public boolean reqPickCard(PLCard reqCard, int uid, int rid) {

        boolean rst = false; // 처리한 결과 수행 가능한지 여부
        List<PLCard> changedList = new ArrayList<>(); // 변경될 카드 목록

        // 요청한 카드의 레벨 확인
        int reqLv = getCard(reqCard.getCd_id()).getLv();

        // 전체 카드 목록 가져오기
        Map<Integer, PLCard> cardsMap = compMap.getCards(rid).stream().collect(Collectors.toMap(PLCard::getCd_id, cur -> cur));
        // 요청된 카드를 전체 카드 목록에서 가져오기
        PLCard prevCard = cardsMap.get(reqCard.getCd_id());


        if (prevCard.getU_id() == 0) {

            boolean isHolding = reqCard.getN_hold() == 0; // 대상 카드가 자기 카드인지 여부
            boolean isField = prevCard.getU_id() == 0; // 대상 카드가 현재 필드에 있는 카드인지 여부

            if (isHolding && isField) {
                /** 홀딩시 */

                prevCard.setU_id(uid); // 목록에서 해당 카드 소유자 변경
                changedList.add(prevCard); // 변경될 목록에 대상 카드 추가

                // 변경될 목록에 덱에서 한장 뽑아서 추가하며 목록에서 해당 카드 소유자및 표시됨으로 변경
                changedList.addAll(cardPresentList(0, reqLv, 1, cardsMap));

                // 잔여 골드 코인 증정
                transCoin(rid, 0, uid, 6, 1);

                rst = true; // 홀딩 성공 반영

            } else {
                /** 구매시 */

                List<PLCoin> coins = compMap.getCoins(rid); // DB에서 해당 방 코인 정보를 가져옴

                Map<Integer, Integer> remaind = null;
                { // 살 수 있는지 계산해서 살 수 있으면 잔금 계산
                    List<PLCard> tempList = new ArrayList<PLCard>(cardsMap.values());
                    Map<Integer, Integer> yield = calcYield(uid, tempList);
                    Map<Integer, Integer> hasCoins = getCoinAmount(uid, coins);
                    remaind = getCard(reqCard.getCd_id()).buyCardRCoins(yield, hasCoins);
                }

                // 구매 가능할 경우
                if (remaind != null) {

                    { /** 카드 처리 */

                        prevCard.setU_id(uid); // 목록에서 해당 카드 소유자 변경
                        prevCard.setN_hold(1); // 목록에서 해당 카드 소유자 변경
                        changedList.add(prevCard);  // 변경될 목록에 대상 카드 추가

                        // 변경될 목록에 덱에서 한장 뽑아서 추가하며 목록에서 해당 카드 소유자및 표시됨으로 변경
                        changedList.addAll(cardPresentList(0, reqLv, 1, cardsMap));

                    }

                    { /** 코인 처리 */

                        List<PLCoin> changedCoins = new ArrayList<>();

                        { // STEP 1. '코인변경목록'에 / 사용자 코인 : 기존 -> 잔금
                            for (int cn_id : remaind.keySet()) {
                                changedCoins.add(new PLCoin(rid, uid, cn_id, remaind.get(cn_id)));
                            }
                        }
                        { // STEP 2. '코인변경목록'에 / 필드 코인 : 기존 -> 기존 + 잔금
                            // 사용자가 지불한 코인을 필드에 추가하기 위해 임시 목록 생성
                            List<PLCoin> temp = new ArrayList<>(changedCoins);
                            for (PLCoin curCoin : temp) {
                                int beforeUC = pickCoin(uid, curCoin.getCn_id(), coins).getCn_count();
                                int afterUC = curCoin.getCn_count();
                                int beforeFC = pickCoin(0, curCoin.getCn_id(), coins).getCn_count();

                                changedCoins.add(new PLCoin(rid, 0, curCoin.getCn_id(), beforeFC + beforeUC - afterUC));
                            }
                            // DB에서 소모 코인 삭감
                            updateCoins(rid, changedCoins);
                        }

                    }

                    { /** 귀족카드 처리 */

                        // 귀족카드 검증을 위한 구매를 반영한 '귀족카드검증목록' 생성
                        Map<Integer, PLCard> afterCards = new HashMap<>(cardsMap);
                        for (PLCard cur : changedList) {
                            afterCards.remove(cur.getCd_id());
                            afterCards.put(cur.getCd_id(), cur);
                        }
                        List<PLCard> tempList = new ArrayList<>(cardsMap.values());
                        Map<Integer, Integer> afterYield = calcYield(uid, tempList);

                        // '귀족카드검증목록'에서 조건에 부합하는 귀족카드 산정
                        for (int cdid : afterCards.keySet()) {
                            Card curInfo = getCard(cdid);
                            PLCard curCard =  afterCards.get(cdid);
                            boolean isInField = (curCard.getU_id() == 0);
                            boolean isNotHold = (curCard.getN_hold() == 1);
                            boolean isCanGet = curInfo.isGetNoble(afterYield);
                            if (isInField && isNotHold && isCanGet) {
                                // 필드에 있으며, 깔려 있고, 가져올 수 있으면
                                afterCards.remove(cdid); // 대상 목록에서 삭제
                                curCard.setU_id(uid); // 소유권 이전
                                changedList.add(curCard); // 변경 목록에 추가
                            }
                        }

                    }

                    rst = true; // 구매 성공 반영
                }


            }

            if (!changedList.isEmpty()) {
                updateCards(rid, changedList); // 변경되는 카드 증정을 DB에서 처리
            }
        }
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
            if (cur.getU_id() == reqUid && cur.getN_hold() == 1) {
                // 자기 카드이면서 홀딩 상태가 아닌 카드일 경우

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
