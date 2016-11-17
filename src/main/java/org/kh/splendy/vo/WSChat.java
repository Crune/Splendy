package org.kh.splendy.vo;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class WSChat {

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
}