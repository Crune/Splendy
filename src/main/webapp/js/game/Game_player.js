/*
 * 플레이어 클래스 
 */

function Player(){
	
	this.id;
	this.score = 0;
	this.action;
	this.websocketId;
	//각 보석 갯수
	this.black_jewel = 0;
	this.green_jewel = 0;
	this.white_jewel = 0;
	this.red_jewel = 0;
	this.blue_jewel = 0;
	this.gold_jewel = 0;
	//각 카드 갯수
	this.black_card = 0;
	this.green_card = 0;
	this.white_card = 0;
	this.red_card = 0;
	this.blue_card = 0;
	this.gold_card = 0;
}

Player.prototype = {
		
		getJewel : function(){
			
		},
		
		getBlack : function() {
			return this.black;
		},
		
		getGreen : function() {
			return this.green;
		},
		
		getWhite : function() {
			return this.white;
		},
		
		getRed : function(){
			return this.red;
		},
		
		getBlue : function(){
			return blue;
		},
		
		getGold : function(){
			return gold;
		}
		
}
