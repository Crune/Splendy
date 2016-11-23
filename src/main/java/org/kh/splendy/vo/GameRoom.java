package org.kh.splendy.vo;

import java.util.*;

import com.google.gson.Gson;

import lombok.Data;

@Data
public class GameRoom {
	
	private int room;
	
	private List<Msg> logs = new ArrayList<Msg>();
	
	private List<Integer> pls = new ArrayList<Integer>();
	private List<PLCard> cards = new ArrayList<PLCard>();
	private List<PLCoin> coins = new ArrayList<PLCoin>();
	
	private int turn = 0;
	private int currentPl = 0;

	public static GameRoom convert(String source) {
		return new Gson().fromJson(source, GameRoom.class);
	}
}
