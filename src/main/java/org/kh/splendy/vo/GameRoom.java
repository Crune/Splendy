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
	private List<Msg> logs = new ArrayList<Msg>();
	
	@SerializedName("pls") @Expose
	private List<WSPlayer> pls = new ArrayList<WSPlayer>();
	@SerializedName("cards") @Expose
	private List<PLCard> cards = new ArrayList<PLCard>();
	@SerializedName("coins") @Expose
	private List<PLCoin> coins = new ArrayList<PLCoin>();
	
	@SerializedName("turn") @Expose
	private int turn = 0;
	@SerializedName("currentPl") @Expose
	private int currentPl = 0;
	
	public static GameRoom convert(String source) {
		return new Gson().fromJson(source, GameRoom.class);
	}
	
	public String getJson() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(this);
	}
}
