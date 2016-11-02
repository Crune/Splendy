package org.kh.splendy.vo;

import lombok.Data;

@Data
public class Room {
	private int id;
	private String title;
	private String password;
	private String info;
	private int playerLimits;
	private int state;
}
