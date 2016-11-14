package org.kh.splendy.vo;

import com.google.gson.Gson;

import lombok.Data;

@Data
public class GameLog {
	private String userId;
	private String roomId;
	private String action;	
	private String actionVaule; //액션을 사용한 값
	
	
	public static GameLog convert(String source){
		GameLog log = new GameLog();
		Gson gson = new Gson();
		log = gson.fromJson(source, GameLog.class);
		return log;
	}
}
