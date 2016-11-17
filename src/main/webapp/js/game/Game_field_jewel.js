function Field_jewel(){	
	this.jewelBlack = 7;
	this.jewelGreen = 7;
	this.jewelWhite = 7;
	this.jewelRed = 7;
	this.jewelBlue = 7;
	this.jewelGold = 7;
}

Field_jewel.prototype = {
		initJewel : function (){
			$("#jewel_black_value").html(this.jewelBlack);
			$("#jewel_green_value").html(this.jewelGreen);
			$("#jewel_white_value").html(this.jewelWhite);
			$("#jewel_red_value").html(this.jewelRed);
			$("#jewel_blue_value").html(this.jewelBlue);
			$("#jewel_gold_value").html(this.jewelGold);
		}
}