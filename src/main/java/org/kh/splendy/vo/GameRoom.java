package org.kh.splendy.vo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.kh.splendy.config.assist.Utils;
import org.kh.splendy.service.CompService;
import org.kh.splendy.service.SocketService;

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



    public String getJson() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(this);
	}

	public boolean isPlaying() {
		return (turn > 0);
	}

	public boolean isMyTurn(int uid) {
		boolean rst = false;
		for (int i=0; i<pls.size(); i++) {
		    if (uid == pls.get(i).getUid()) {
                rst = (currentPl == i);
            }
		}
		return rst;
	}
	public void nextTurn(SocketService sock) {
        if (turn >= 0) {
            this.turn += 1;
            this.currentPl = pls.get(0).getUid();
        }
    }

    public int nextActor(SocketService sock) {
        boolean isValid = false;
        int myCursor = 0;
        for (int i = 0; i < pls.size(); i++) {
            if (myCursor != 0) {
                this.currentPl = i;
                isValid = true;
            }
            if (currentPl == pls.get(i).getUid()) {
                myCursor = i;
            }
        }
        if (!isValid) {
            nextTurn(sock);
        }
        return this.currentPl;
    }

	public boolean reqJoin(WSPlayer reqUser) {
	    boolean rst = false;
	    if (limit > pls.size()) {
            List<Integer> playerList = new ArrayList<>();
            for (WSPlayer curPl : pls) {
                playerList.add(curPl.getUid());
            }
	        if (!playerList.contains(reqUser.getUid())) {
                pls.add(reqUser);
            }
	        rst = true;
        }
        return rst;
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
    public WSComp pickCard(PLCard reqCard) {
        // TODO 해당 카드를 가져옴

        return null;
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

    public boolean reqCard(PLCard reqCard, int reqUser) {
        boolean rst = false;/*
        if (isMyTurn(reqUser) && couldBuy(reqUser)) {

            rst = true;
        }*/
        return rst;
    }

}
