package org.kh.splendy.vo;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class WSMsg {
	
	@SerializedName("type") @Expose
	public String type="";
	@SerializedName("cont") @Expose
	public Object cont=null;
	
	public static WSMsg convert(String source) {
		return new Gson().fromJson(source, WSMsg.class);
	}

	public WSMsg(String type, Object cont) {
		this.type = type;
		this.cont = cont;
	}

	public WSMsg() {
	}
}