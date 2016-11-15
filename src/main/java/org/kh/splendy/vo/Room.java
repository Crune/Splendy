package org.kh.splendy.vo;

import lombok.Data;

@Data
public class Room {
	private int id;
	private String title;
	private String password="";
	private String info="";
	private int playerLimits=0;
	private int state=0;
	private int hostId=0;

	public final static int ST_INVALID=0;
	public final static int ST_READY=1;
	public final static int ST_PROGRESS=2;
	public final static int ST_END=3;
}
