package org.kh.splendy.vo;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Auth {

	@SerializedName("uid") @Expose
	int uid;

	@SerializedName("code") @Expose
	String code;
	
	public static Auth convert(String source) {
		return new Gson().fromJson(source, Auth.class);
	}

}
