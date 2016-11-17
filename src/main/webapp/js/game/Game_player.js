/*
 * 플레이어 클래스 
 */

function Player(userId, order){
	
	this.userId = userId;
	this.order = order;
	this.score = 0;
	this.action;
	this.actionPoint = 0;
	this.turn = false;
	this.websocketId;
	//한 턴에 보석을 클릭했을 때 토글되는 변수
	this.blackJewel = false;
	this.greenJewel = false;
	this.whiteJewel = false;
	this.redJewel = false;
	this.blueJewel = false;
	this.goldJewel = false;
	//각 카드 갯수
	this.black_card = 0;
	this.green_card = 0;
	this.white_card = 0;
	this.red_card = 0;
	this.blue_card = 0;
	this.gold_card = 0;
}

Player.prototype = {
		
		toggleBlackJewelStatus : function(){
			if(this.blackJewel != true){
				this.blackJewel = true;
			} else {
				this.blackJewel = false;
			}
		},
		
		toggleWhiteJewelStatus : function(){
			if(this.whiteJewel != true){
				this.whiteJewel = true;
			} else {
				this.whiteJewel = false;
			}
		},
		
		toggleGreenJewelStatus : function(){
			if(this.greenJewel != true){
				this.greenJewel = true;
			} else {
				this.greenJewel = false;
			}
		},
		
		toggleRedJewelStatus : function(){
			if(this.redJewel != true){
				this.redJewel = true;
			} else {
				this.redJewel = false;
			}
		},
		
		toggleBlueJewelStatus : function(){
			if(this.blueJewel != true){
				this.blueJewel = true;
			} else {
				this.blueJewel = false;
			}
		},
		
		toggleGoldJewelStatus : function(){
			if(this.goldJewel != true){
				this.goldJewel = true;
			} else {
				this.goldJewel = false;
			}
		},
		
		getBlackJewelStatus : function(){
			return this.blackJewel;
		},
		
		getWhiteJewelStatus : function(){
			return this.whiteJewel;
		},
		
		getGreenJewelStatus : function(){
			return this.greenJewel;
		},
		
		getRedJewelStatus : function(){
			return this.redJewel;
		},
		
		getBlueJewelStatus : function(){
			return this.blueJewel;
		},
		
		getGoldJewelStatus : function(){
			return this.goldJewel;
		},
		
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
		
		getActionPoint : function(){
			return this.actionPoint;
		},
		
		setActionPoint : function(actionPoint){
			if(actionPoint === 0){
				this.actionPoint = 0;
				return;
			}
			this.actionPoint += actionPoint;
			
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
			var player = null;
			for(var i in players){
				if(players[i].getTurn() === true){
					player = players[i];
				}
			}
			var jewelValue = parseInt($("#jewel_white_value").html());
			var num = $("#player" + player.getOrder() + "_jewel_white").html();
			if(jewelValue === 0){ 				
				return 0;
			} else {
				num = parseInt(num);			
				num = num + 1;
				$("#player" + player.getOrder() + "_jewel_white").html(num);
				
											
				jewelValue = jewelValue - 1;
				$("#jewel_white_value").html(jewelValue);
				
				this.action = "getJewelWhite";	//플레이어 객체의 액션 코드 설정'
				return jewelValue;
			}
			
		},
		
		getBlack : function() {
			var player = null;
			for(var i in players){
				if(players[i].getTurn() === true){
					player = players[i];
				}
			}
			var jewelValue = parseInt($("#jewel_black_value").html());
			var num = $("#player" + player.getOrder() + "_jewel_black").html();
			if(jewelValue === 0){ 				
				return 0;
			} else {
				num = parseInt(num);				
				num = num + 1;
				$("#player" + player.getOrder() + "_jewel_black").html(num);
				
				
				jewelValue = jewelValue - 1;
				$("#jewel_black_value").html(jewelValue);
				
				this.action = "getJewelBlack";
				return jewelValue;
			}
		},
		
		getGreen : function() {
			var player = null;
			for(var i in players){
				if(players[i].getTurn() === true){
					player = players[i];
				}
			}
			var jewelValue = parseInt($("#jewel_green_value").html());
			var num = $("#player" + player.getOrder() + "_jewel_green").html();
			if(jewelValue === 0){ 				
				return 0;
			} else {
				num = parseInt(num);				
				num = num + 1;
				$("#player" + player.getOrder() + "_jewel_green").html(num);
				
				
				jewelValue = jewelValue - 1;
				$("#jewel_green_value").html(jewelValue);
				
				this.action = "getJewelGreen";
				return jewelValue;

			}
		},
						
		getRed : function(){
			var player = null;
			for(var i in players){
				if(players[i].getTurn() === true){
					player = players[i];
				}
			}
			var jewelValue = parseInt($("#jewel_red_value").html());
			var num = $("#player" + player.getOrder() + "_jewel_red").html();
			if(jewelValue === 0){ 				
				return 0;
			} else {
				num = parseInt(num);				
				num = num + 1;
				$("#player" + player.getOrder() + "_jewel_red").html(num);
							
				
				jewelValue = jewelValue - 1;
				$("#jewel_red_value").html(jewelValue);
				this.action = "getJewelRed";
				return jewelValue;

			}
		},
		
		getBlue : function(){
			var player = null;
			for(var i in players){
				if(players[i].getTurn() === true){
					player = players[i];
				}
			}
			var jewelValue = parseInt($("#jewel_blue_value").html());
			var num = $("#player" + player.getOrder() + "_jewel_blue").html();
			if(jewelValue === 0){ 				
				return 0;
			} else {
				num = parseInt(num);				
				num = num + 1;
				$("#player" + player.getOrder() + "_jewel_blue").html(num);
				
				
				jewelValue = jewelValue - 1;
				$("#jewel_blue_value").html(jewelValue);
				this.action = "getJewelBlue";
				return jewelValue;

			}
		},
		
		getGold : function(){
			var player = null;
			for(var i in players){
				if(players[i].getTurn() === true){
					player = players[i];
				}
			}
			var jewelValue = parseInt($("#jewel_gold_value").html());
			var num = $("#player" + player.getOrder() + "_jewel_gold").html();
			if(jewelValue === 0){ 				
				return 0;
			} else {
				num = parseInt(num);				
				num = num + 1;
				$("#player" + player.getOrder() + "_jewel_gold").html(num);
				
				
				jewelValue = jewelValue - 1;
				$("#jewel_gold_value").html(jewelValue);
				this.action = "getJewelGold";
				return jewelValue;

			}
		},
		
		turnChange : function(){
			
		}
		
}
