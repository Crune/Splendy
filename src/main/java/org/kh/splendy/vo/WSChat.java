package org.kh.splendy.vo;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class WSChat {

	@SerializedName("uid") @Expose
	public int uid;
	@SerializedName("nick") @Expose
	public String nick;
	@SerializedName("cont") @Expose
	public String cont;
	@SerializedName("time") @Expose
	public String time;
	@SerializedName("type") @Expose
	public String type;
	
	public static WSChat convert(String source) {
		return new Gson().fromJson(source, WSChat.class);
	}

	public WSChat() { }
	public WSChat(int uid, String nick, String cont, String time, String type) {
		this.uid = uid;
		this.nick = nick;
		this.cont = cont;
		this.time =  time;
		this.type = type;
	}
}