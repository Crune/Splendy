/*
 * 플레이어 클래스 
 */

function Player(userId, order){
	
	this.userId = userId;
	this.order = order;
	this.score = 0;
	this.action;
	this.turn = false;
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
		getUserId : function(){
			return this.userId;
		},
		
		getOrder : function(){
			return this.order;
		},
		
		getScore : function(){
			return this.score;
		},
		
		setScore : function(point){
			this.score += point;
			return this.score;
		},
		
		getTurn : function(){
			return this.turn;
		},
		
		toggleTurn : function(){
			if(this.turn === false){
				this.turn = true;
			} else {
				this.turn = false;
			}
		},
		
		getWhite : function() {
			if(this.order === 1){
				var num = $("#player1_jewel_white").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player1_jewel_white").html(num);
			} else if (this.order === 2){
				$("#player2_jewel_white").text() + 1;
			} else if (this.order === 3){
				$("#player3_jewel_white").text() + 1;
			} else if (this.order === 4){
				$("#player4_jewel_white").text() + 1;
			}
			$("#jewel_white_value").text -= 1;
		},
		
		getBlack : function() {
			return this.black;
		},
		
		getGreen : function() {
			return this.green;
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
