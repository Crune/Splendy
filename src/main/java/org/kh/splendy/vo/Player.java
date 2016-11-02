package org.kh.splendy.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class Player {
	private int id;
	private int roomId;
	private int state;
	private String chatSessionId;
	private Date joinDate;
}
