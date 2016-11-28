package org.kh.splendy.vo;

import java.util.*;

import lombok.*;

import com.google.gson.*;
import com.google.gson.annotations.*;


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

	public boolean reqJoin(WSPlayer reqUser) {
	    boolean rst = false;
	    if (limit > pls.size()) {
	        pls.add(reqUser);
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


    public Map<Integer, Integer> getCoinCount(List<PLCoin> reqCoins) {
        Map<Integer, Integer> rstCoins = new HashMap<>();
        for (PLCoin cur : reqCoins) {
            rstCoins.put(cur.getCn_id(), cur.getCn_count());
        }
        return rstCoins;
    }

    public List<PLCoin> pickCoins(List<PLCoin> reqGetCoins, List<PLCoin> reqDrawCoins, int uid) {
        List<PLCoin> changed = new ArrayList<>();

        // TODO WORKME

        return changed;
    }

    public boolean reqCard(PLCard reqCard, int reqUser) {
        boolean rst = false;/*
        if (isMyTurn(reqUser) && couldBuy(reqUser)) {

            rst = true;
        }*/
        return rst;
    }
}
