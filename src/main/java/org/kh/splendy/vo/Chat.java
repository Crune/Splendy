package org.kh.splendy.vo;

import lombok.Data;
import com.google.gson.Gson;

@Data
public class Chat {
	private String message;
	private String type;
	private String to;
	
	public static Chat convert(String source) {
		Chat messge = new Chat();
		Gson gson = new Gson();
		messge = gson.fromJson(source, Chat.class);
		return messge;
	}
}
