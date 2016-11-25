package org.kh.splendy.vo;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class WSPlayer {

	@SerializedName("uid") @Expose
	private int uid;
	@SerializedName("nick") @Expose
	private String nick;
	@SerializedName("rating") @Expose
	private int rating;
	@SerializedName("icon") @Expose
	private String icon;
	@SerializedName("role") @Expose
	private String role;
	@SerializedName("room") @Expose
	private int room;

	public WSPlayer CanSend() {
		setRole((getUid() == Integer.parseInt(getRole()))?"host":"");
		return this;
	}
	
	public static WSPlayer convert(String source) {
		return new Gson().fromJson(source, WSPlayer.class);
	}
}