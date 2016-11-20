package org.kh.splendy.vo;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class WSPlayer {

	@SerializedName("uid") @Expose
	public int uid;
	@SerializedName("nick") @Expose
	public String nick;
	@SerializedName("rating") @Expose
	public int rating;
	@SerializedName("icon") @Expose
	public String icon;
	@SerializedName("role") @Expose
	public String role;
	@SerializedName("room") @Expose
	public int room;

	public WSPlayer CanSend() {
		setRole((getUid() == Integer.parseInt(getRole()))?"host":"");
		return this;
	}
	
	public static WSPlayer convert(String source) {
		return new Gson().fromJson(source, WSPlayer.class);
	}
}