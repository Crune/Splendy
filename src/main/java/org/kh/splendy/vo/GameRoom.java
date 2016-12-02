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

    public void halt() {
        this.isHalted = true;
    }
}
