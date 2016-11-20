package org.kh.splendy.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class Room {
	private int id;
	private String title;
	private String password="";
	private int host=0;
	private String info="";
	private int playerLimits=0;
	private int winner=-1;
	private Date start;
	private Date end;
}
