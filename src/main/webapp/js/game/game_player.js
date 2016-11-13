/*
 * 플레이어 클래스 
 */

function Player(id){
	
	this.id = id;
	this.score = 0;
	this.mode;
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
	
	 
	Player.prototype.getJewelBlack = function (){		
		this.black_jewel += 1;
		this.action = "getJewelBlack";
		gameSock.send( JSON.stringify(this) ); //서버에 로그 기록을 위해서 JSON으로 전송
	}
	
	Player.prototype.getJewelGreen = function (){		
		this.green_jewel += 1;
		this.action = "getJewelGreen";
		gameSock.send( JSON.stringify(this) ); //서버에 로그 기록을 위해서 JSON으로 전송
	}  //근데 이렇게 하면 안된다... 지정을 해서 한번에 가져올거기때문이다.
}
