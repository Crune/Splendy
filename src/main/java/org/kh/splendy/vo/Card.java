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

	private int yieldType = 0;

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
			String temp = getType().substring(2, 1);
			setLv((temp.equals("B")) ? 4 : Integer.parseInt(temp) - 1);

            temp = getType().substring(3, 4);
            int yield =0;
            yield = temp.toUpperCase().equals("W")?1:yield;
            yield = temp.toUpperCase().equals("G")?2:yield;
            yield = temp.toUpperCase().equals("B")?3:yield;
            yield = temp.toUpperCase().equals("R")?4:yield;
            yield = temp.toUpperCase().equals("K")?5:yield;
            yield = temp.toUpperCase().equals("L")?6:yield;
            setYieldType(yield);

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
