package org.kh.splendy.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class UserProfile {
	private int userId=-1;

	// 전체 플레이 정보
	private Date totalTime=new Date(0);
	
	private int win=0;
	private int lose=0;
	private int draw=0;
	
	private int rate=0;

	// 마지막 게임 플레이 정보
	private int lastRId=-1;
	private Date lastTime=new Date(0);
	private int lastScore=0;
	private int lastRate=0;

	// 사용자 정보
	private String icon="unnamed.png";
	private String info="";
	
	public final static int WIN = 1;
	public final static int DRAW = 0;
	public final static int LOSE = -1; 
}
