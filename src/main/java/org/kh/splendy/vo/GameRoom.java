package org.kh.splendy.vo;

import java.util.*;

import com.google.gson.Gson;

import lombok.Data;

@Data
public class GameRoom {
	
	private int room;
	
	private List<Msg> logs;
	
	private int[] pls = new int[4];
	private PLCard[][] cards;
	private int[][] coins = new int[6][7];
	
	private int turn = 0;
	private int currentPl = 0;

	public static GameRoom convert(String source) {
		return new Gson().fromJson(source, GameRoom.class);
	}
}
