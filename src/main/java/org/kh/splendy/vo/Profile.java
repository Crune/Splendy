package org.kh.splendy.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class Profile {
	private int userId=-1;
	private Date totalTime=new Date(0);
	private Date lastTime=new Date(0);
	private int win=0;
	private int lose=0;
	private int draw=0;
	private int lastScore=0;
	private int gameRate=0;
	private int lastRate=0;

	public final static int WIN = 1;
	public final static int DRAW = 0;
	public final static int LOSE = -1; 
}
