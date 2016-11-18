function HeroCardManager(){
	this.initCards;
	this.initLev1;
	this.initLev2;
	this.initLev3;
	this.initLevN;
} 

HeroCardManager.prototype = {
		setInitLevN : function(data){
			for(var i in data){
				console.log(i);
				$("#heroCard_define_" + i+1).append("<li> card id : " + data[i].getId + "</li>");
				$("#heroCard_define_" + i+1).append("<li> card id : " + data[i].getCode + "</li>");
				$("#heroCard_define_" + i+1).append("<li> card id : " + data[i].getImg + "</li>");
				$("#heroCard_define_" + i+1).append("<li> card id : " + data[i].getInfo + "</li>");
				$("#heroCard_define_" + i+1).append("<li> card id : " + data[i].getName + "</li>");
				$("#heroCard_define_" + i+1).append("<li> card id : " + data[i].getType + "</li>");
				$("#heroCard_define_" + i+1).append("<li> card id : " + data[i].getPoint + "</li>");
				$("#heroCard_define_" + i+1).append("<li> card id : " + data[i].getBlack + "</li>");
				$("#heroCard_define_" + i+1).append("<li> card id : " + data[i].getBlue + "</li>");
				$("#heroCard_define_" + i+1).append("<li> card id : " + data[i].getGreen + "</li>");
				$("#heroCard_define_" + i+1).append("<li> card id : " + data[i].getRed + "</li>");
				$("#heroCard_define_" + i+1).append("<li> card id : " + data[i].getWhite + "</li>");
				
			}
		} 
}