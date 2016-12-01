package org.kh.splendy.vo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.kh.splendy.config.assist.Utils;
import org.kh.splendy.service.SocketService;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class GameRoom {
	
	@SerializedName("room") @Expose
	private int room;

	@SerializedName("limit") @Expose
	private int limit = 4;
	
	@SerializedName("logs") @Expose
	private List<Msg> logs = new ArrayList<>();
	
	@SerializedName("pls") @Expose
	private List<WSPlayer> pls = new ArrayList<>();
	@SerializedName("cards") @Expose
	private List<PLCard> cards = new ArrayList<>();
	@SerializedName("coins") @Expose
	private List<PLCoin> coins = new ArrayList<>();
	
	@SerializedName("turn") @Expose
	private int turn = 0;
	@SerializedName("currentPl") @Expose
	private int currentPl = 0;

    @SerializedName("isHalted") @Expose
    private boolean isHalted = false;

    public String getJson() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(this);
	}

	public boolean isPlaying() {
		return (turn > 0 && isHalted == false);
	}

	public boolean isMyTurn(int uid) {
		boolean rst = false;
		for (int i=0; i<getPls().size(); i++) {
		    if (uid == getPls().get(i).getUid()) {
                rst = (currentPl == i);
            }
		}
		return rst;
	}
	public void nextTurn() {
        if (turn >= 0) {
            this.turn += 1;
            this.currentPl = getPls().get(0).getUid();
        }
    }

    public int nextActor() {
        boolean isValid = false;
        int myCursor = 0;
        for (int i = 0; i < getPls().size(); i++) {
            if (myCursor != 0) {
                this.currentPl = i;
                isValid = true;
            }
            if (currentPl == getPls().get(i).getUid()) {
                myCursor = i;
            }
        }
        if (!isValid) {
            nextTurn();
        }
        return this.currentPl;
    }

    public Map<Integer, Integer> getCoinAmount(int uid) {
        Map<Integer, Integer> fCoins = new HashMap<>();
        for (PLCoin cur : coins) {
            if (cur.getU_id() == uid) {
                fCoins.put(cur.getCn_id(), cur.getCn_count());
            }
        }
        return fCoins;
    }


    public Map<Integer, Integer> getCoinCount(List<PLCoin> reqCoins, int uid, int rid) {
        Map<Integer, Integer> rstCoins = new HashMap<>();
        for (PLCoin cur : reqCoins) {
            if (cur.getU_id() == uid) {
                rstCoins.put(cur.getCn_id(), cur.getCn_count());
            }
        }
        return rstCoins;
    }

    /** 코인 목록 통합 */
    public Map<Integer, PLCoin> getCoinMap(List<PLCoin> reqCoins) {
        Map<Integer, PLCoin> rst = new HashMap<>();
        for (PLCoin cur : reqCoins) {
            if (rst.get(cur.getCn_id()) != null) {
                PLCoin temp = rst.get(cur.getCn_id());
                rst.get(cur.getCn_id()).setCn_count(temp.cn_count + cur.getCn_count());
            } else {
                rst.put(cur.getCn_id(), cur);
            }
        }
        return rst;
    }

    public List<PLCard> pickCard(List<PLCard> reqCard) {
        List<PLCard> rst = new ArrayList<>();
        for (PLCard cur : reqCard) {
            rst.add(pickCard(cur));
        }
        return rst;
    }

    public PLCard pickCard(PLCard reqCard) {
        PLCard rst = null;
        // 해당 카드를 가져옴
        for (PLCard card : cards) {
            if (card.getCd_id() == reqCard.getCd_id()) {
                card.setN_hold(reqCard.getN_hold());
                card.setU_id(reqCard.getU_id());
                rst = card;
            }
        }
        return rst;
    }

    public List<PLCoin> pickCoins(List<PLCoin> reqGetCoins, List<PLCoin> reqDrawCoins) {
        // 버리는 코인이 있을 경우
        if (!reqDrawCoins.isEmpty()) {
            List<PLCoin> uChange = null, fChange = null;
            try {
                uChange = (List<PLCoin>) Utils.copy(reqGetCoins);
                fChange = (List<PLCoin>) Utils.copy(reqDrawCoins);

                // 버림 코인은 음수로 변환
                reqDrawCoins.forEach(cn -> {
                    if (cn.getCn_count() > 0) {
                        cn.setCn_count(cn.getCn_count() * -1);
                    }
                });
                uChange.addAll(reqDrawCoins);

                // 같은 종류의 코인 갯수 합산 처리
                Map<Integer, PLCoin> rst = getCoinMap(uChange);
                uChange.clear();
                for (int k : rst.keySet()) {
                    PLCoin temp = rst.get(k);
                    temp.setRm_id(room);
                    uChange.add(temp);
                }

                // 버리는 코인 양수로 변환, 필드의 변경값 산정
                fChange.forEach(cn -> {
                    cn.setU_id(0);
                    if (cn.getCn_count() < 0) {
                        cn.setCn_count(cn.getCn_count() * -1);
                    }
                });

                // 통합된 목록에 필드 변경값 합산
                uChange.addAll(fChange);

                return uChange;
            } catch (Exception e) {
                e.printStackTrace();
                return reqGetCoins;
            }
        } else {
            return reqGetCoins;
        }
    }

    public PLCoin pickGold(int uid) {
        boolean isCan = false;
        PLCoin rst = null;
        for (PLCoin cur : coins) {
            if (cur.getU_id() == 0 && cur.getCn_id() == 6 && cur.getCn_count() > 0) {
                cur.setCn_count(cur.getCn_count() -1);
                isCan = true;
            }
        }
        if (isCan) {
            for (PLCoin cur : coins) {
                if (cur.getU_id() == uid && cur.getCn_id() == 6) {
                    cur.setCn_count(cur.getCn_count() +1);
                    rst = cur;
                }
            }
        }
        return rst;
    }

    public void halt() {
        this.isHalted = true;
    }
}
