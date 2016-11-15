package org.kh.splendy.vo;

import java.sql.Date;

import com.google.gson.Gson;

import lombok.Data;

@Data
public class Msg {

	public static final int T_AUTH = -2;
	public static final int T_SYSTEM = -1;
	public static final int T_NONE = 0;
	public static final int T_WHISPER = 1;
	public static final int T_EMOTION = 2;
	
	private int msgId=0;
	private int roomId=0;
	private int type;
	private String author="";
	private String cont;
	private Date reg;
}
