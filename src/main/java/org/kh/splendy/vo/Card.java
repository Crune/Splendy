package org.kh.splendy.vo;

import lombok.Data;

@Data
public class Card {
	private int id;
	private String code="";	
	private String name="";
	private String img="";
	private String info="";
	
	private String type="ZZZZ00000";
	private int lv=0;
	private int point=0;
	
	private int white=0;
	private int blue=0;
	private int green=0;
	private int red=0;
	private int black=0;
	

	public Card parse() {
		setType(code.substring(0, 4));
		if (getType().length() == 9) {
			String templv = getType().substring(2, 1);
			setLv((templv.equals("B")) ? 4 : Integer.parseInt(templv) - 1);
			setPoint(Integer.parseInt(code.substring(4, 5)));
			setWhite(Integer.parseInt(code.substring(5, 6)));
			setBlue(Integer.parseInt(code.substring(6, 7)));
			setGreen(Integer.parseInt(code.substring(7, 8)));
			setRed(Integer.parseInt(code.substring(8, 9)));
			setBlack(Integer.parseInt(code.substring(9)));
		}
		return this;
	}	
}
