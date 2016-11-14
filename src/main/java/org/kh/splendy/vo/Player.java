package org.kh.splendy.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class Player {
	private int id;
	private int roomId;
	private int state = ST_LOST;
	private String chatSessionId= "";
	private Date joinDate;
	private String authcode= "";

	public final static int ST_LOST=0;
	public final static int ST_CONNECT=1;
	public final static int ST_INGAME=2;
	public final static int ST_GHOST=3;
	public final static int ST_PLAYING=4;
}
