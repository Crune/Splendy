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
	
	private int msgId;
	private int roomId;
	private int type;
	private String author;
	private String cont;
	private Date reg;

	public static Msg convert(String source) {
		Msg messge = new Msg();
		Gson gson = new Gson();
		messge = gson.fromJson(source, Msg.class);
		return messge;
	}
}
