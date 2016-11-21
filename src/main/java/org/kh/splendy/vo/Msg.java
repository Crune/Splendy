package org.kh.splendy.vo;

import java.sql.Date;

import com.google.gson.Gson;

import lombok.Data;

@Data
public class Msg {
	private int mid=0;
	private int rid=0;
	private int uid=0;
	private String type="";
	private String cont="";
	private Date reg;
}
