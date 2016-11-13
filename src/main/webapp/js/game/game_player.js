/*
 * 플레이어 클래스 
 */

function Player(id){
	
	this.id = id;
	this.score = 4;
	this.mode;
	/*var black_jewel;
	var green_jewel;
	var white_jewel;
	var red_jewel;
	var blue_jewel;
	var gold_jewel;
	
	var black_card;
	var green_card;
	var white_card;
	var red_card;
	var blue_card;
	var gold_card;*/
	 
	this.getJewel = function (){		
		this.score -= 1;
		this.mode = "getJewel"; //mode는 action 단어로 교체 
		gameSock.send( JSON.stringify(this) );		
	}
}
