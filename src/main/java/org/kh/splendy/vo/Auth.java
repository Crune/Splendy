package org.kh.splendy.vo;

import com.google.gson.Gson;

import lombok.Data;

@Data
public class Auth {
	int uid;
	String code;
	
	public static Auth convert(String source) {
		return new Gson().fromJson(source, Auth.class);
	}

}
