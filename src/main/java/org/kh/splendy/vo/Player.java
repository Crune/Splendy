package org.kh.splendy.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class Player {

	private int id;
	private int room;
	private Date reg;
	private int isIn=0;
	private String ip= "";
	
}
