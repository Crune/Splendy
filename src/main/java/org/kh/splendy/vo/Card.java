package org.kh.splendy.vo;

import lombok.Data;

@Data
public class Card {
	private int id;
	private String code;	
	private String name;
	private String img;
	private String info;
	
	
	private String type;
	private int point;
	private int white;
	private int blue;
	private int green;
	private int red;
	private int black;
}
