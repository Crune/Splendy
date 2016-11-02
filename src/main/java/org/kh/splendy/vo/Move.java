package org.kh.splendy.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class Move {
	private int id;
	private int roomId;
	private int from;
	private int to;
	private int cardId;
	private int coinId;
	private int amount;
	private Date reg;
	
}
