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
				$("#player1_jewel_white").html(num);								
			} else if (this.order === 2){
				var num = $("#player2_jewel_white").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player2_jewel_white").html(num);
			} else if (this.order === 3){
				var num = $("#player3_jewel_white").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player3_jewel_white").html(num);
			} else if (this.order === 4){
				var num = $("#player4_jewel_white").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player4_jewel_white").html(num);
			}
			var jewelValue = parseInt($("#jewel_white_value").html());
			jewelValue = jewelValue - 1;
			$("#jewel_white_value").html(jewelValue);
			
		},
		
		getBlack : function() {
			if(this.order === 1){
				var num = $("#player1_jewel_black").html();
				num = parseInt(num);				
				num = num + 1;
				$("#player1_jewel_black").html(num);			
			} else if (this.order === 2){
				var num = $("#player2_jewel_black").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player2_jewel_black").html(num);
			} else if (this.order === 3){
				var num = $("#player3_jewel_black").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player3_jewel_black").html(num);
			} else if (this.order === 4){
				var num = $("#player4_jewel_black").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player4_jewel_black").html(num);
			}
			var jewelValue = parseInt($("#jewel_black_value").html());
			jewelValue = jewelValue - 1;
			$("#jewel_black_value").html(jewelValue);
		},
		
		getGreen : function() {
			if(this.order === 1){
				var num = $("#player1_jewel_green").html();
				num = parseInt(num);				
				num = num + 1;
				$("#player1_jewel_green").html(num);			
			} else if (this.order === 2){
				var num = $("#player2_jewel_green").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player2_jewel_green").html(num);
			} else if (this.order === 3){
				var num = $("#player3_jewel_green").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player3_jewel_green").html(num);
			} else if (this.order === 4){
				var num = $("#player4_jewel_green").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player4_jewel_green").html(num);
			}
			var jewelValue = parseInt($("#jewel_green_value").html());
			jewelValue = jewelValue - 1;
			$("#jewel_green_value").html(jewelValue);
		},
						
		getRed : function(){
			if(this.order === 1){
				var num = $("#player1_jewel_red").html();
				num = parseInt(num);				
				num = num + 1;
				$("#player1_jewel_red").html(num);			
			} else if (this.order === 2){
				var num = $("#player2_jewel_red").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player2_jewel_red").html(num);
			} else if (this.order === 3){
				var num = $("#player3_jewel_red").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player3_jewel_red").html(num);
			} else if (this.order === 4){
				var num = $("#player4_jewel_red").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player4_jewel_red").html(num);
			}
			var jewelValue = parseInt($("#jewel_red_value").html());
			jewelValue = jewelValue - 1;
			$("#jewel_red_value").html(jewelValue);
		},
		
		getBlue : function(){
			if(this.order === 1){
				var num = $("#player1_jewel_blue").html();
				num = parseInt(num);				
				num = num + 1;
				$("#player1_jewel_blue").html(num);			
			} else if (this.order === 2){
				var num = $("#player2_jewel_blue").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player2_jewel_blue").html(num);
			} else if (this.order === 3){
				var num = $("#player3_jewel_blue").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player3_jewel_blue").html(num);
			} else if (this.order === 4){
				var num = $("#player4_jewel_blue").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player4_jewel_blue").html(num);
			}
			var jewelValue = parseInt($("#jewel_blue_value").html());
			jewelValue = jewelValue - 1;
			$("#jewel_blue_value").html(jewelValue);
		},
		
		getGold : function(){
			if(this.order === 1){
				var num = $("#player1_jewel_gold").html();
				num = parseInt(num);				
				num = num + 1;
				$("#player1_jewel_gold").html(num);			
			} else if (this.order === 2){
				var num = $("#player2_jewel_gold").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player2_jewel_gold").html(num);
			} else if (this.order === 3){
				var num = $("#player3_jewel_gold").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player3_jewel_gold").html(num);
			} else if (this.order === 4){
				var num = $("#player4_jewel_gold").html();
				num = parseInt(num);				
				num = num + 1;
				console.log(num);
				$("#player4_jewel_gold").html(num);
			}
			var jewelValue = parseInt($("#jewel_gold_value").html());
			jewelValue = jewelValue - 1;
			$("#jewel_gold_value").html(jewelValue);
		}
		
}
