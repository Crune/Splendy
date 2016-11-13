package org.kh.splendy.vo;

import com.google.gson.Gson;

import lombok.Data;

@Data
public class GameLog {
	private String mode;
	private String score;
	
	
	public static GameLog convert(String source){
		GameLog log = new GameLog();
		Gson gson = new Gson();
		log = gson.fromJson(source, GameLog.class);
		return log;
	}
}
