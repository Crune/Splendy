package org.kh.splendy.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Card {
	private int id;
	private String code="";	
	private String name="";
	private String img="";
	private String info="";

    private int yield = 0;
    private int lv=0;
    private int score=0;
    private int[] price = new int[5];

    public Card parse() {
        String cur = this.code.toUpperCase().substring(2,3);
        this.lv = cur.equals("B")?4:Integer.parseInt(cur);
        this.yield = 0;
        cur = this.code.toUpperCase().substring(3,4)+"";
        yield = cur.equals("W")?1:yield;
        yield = cur.equals("B")?2:yield;
        yield = cur.equals("G")?3:yield;
        yield = cur.equals("R")?4:yield;
        yield = cur.equals("K")?5:yield;
        yield = cur.equals("L")?6:yield;

        score =  code(4);
        for (int i=5; i<10; i++) {
            price[i-5] =  code(i);
        }

        return this;
    }
    private int code(int i) {
        String cur = this.code.toUpperCase().substring(i, i+1);
        return Integer.parseInt(cur);
    }

    public boolean isGetNoble(Map<Integer, Integer> allSupply) {
	    boolean rst = (lv == 4);
	    for (int i=0; i<price.length; i++) {
	        boolean isIn = allSupply.containsKey(i+1);
	        if (isIn) {
	            rst = rst && (price[i] >= allSupply.get(i+1));
            } else {
	            rst = rst && (price[i]==0);
            }
        }
        return rst;
    }

    /**
     *  카드 구매시 가능 여부와 잔금을 구하는 매서드
     * @param yields 해당 구매자의 생산량
     * @param coins 해당 구매자늬 보유 코인
     * @return 카드 구매 시 잔금 ( 만약 구매 불가능 시 null )
     */
    public Map<Integer, Integer> buyCardRCoins(Map<Integer, Integer> yields, Map<Integer, Integer> coins) {

        Map<Integer, Integer> resultCoins = new HashMap<>();

        int hasGold = coins.containsKey(6)?coins.get(6):0;
        for (int i=0; i<price.length; i++) {
            int hasCoin = coins.containsKey(i+1)?coins.get(i+1):0;
            int hasYield = yields.containsKey(i+1)?yields.get(i+1):0;
            int curPrice = price[i];

            hasCoin -= curPrice - hasYield;
            if (hasCoin <= 0) {
                // 구매력 보다 비싼걸 사면 대신 금으로 지불
                hasGold += hasCoin;
                hasCoin = 0;
            }
            resultCoins.put(i+1, hasCoin);
        }
        resultCoins.put(6, hasGold);

        if (hasGold < 0) { return null; }

        return resultCoins;
    }
}
