var gameSock = null;
var player1 = new Player("dohyun", 1);
var player2 = new Player("yoon", 2);
var player3 = new Player("minjung", 3);
var player4 = new Player("jinkyu", 4);

var players = [player1,player2,player3,player4];
var game_status;

$(document).ready(function() {
	
	// 페이지가 시작됨과 동시에 소켓 서버 주소로 접속한다.
	gameSock = new SockJS("http://" + window.location.host + "/gameMain");
	
	gameSock.onopen = function() {
		console.log('welcome');
		player1.toggleTurn();
		for(var i in players){
			console.log(players[i].getUserId() + " turn status : " + players[i].getTurn());
		}
		
		$("#player1_name").html(player1.getUserId());
		$("#player2_name").html(player2.getUserId());
		$("#player3_name").html(player3.getUserId());
		$("#player4_name").html(player4.getUserId());		
	};

	gameSock.onmessage = function(evt) {
		game_status = JSON.parse(evt.data);	
		console.log(game_status);
	};

	gameSock.onclose = function() {
	};

	
	$("#popbutton").click(function(){
		 if(player1.getTurn() === true){
			player1.toggleTurn();
			player2.toggleTurn();
			for(var i in players){
				console.log(players[i].getUserId() + " turn status : " + players[i].getTurn());
			}
		} else if (player2.getTurn() === true){
			player2.toggleTurn();
			player3.toggleTurn();
			for(var i in players){
				console.log(players[i].getUserId() + " turn status : " + players[i].getTurn());
			}
		} else if (player3.getTurn() === true){
			player3.toggleTurn();
			player4.toggleTurn();
			for(var i in players){
				console.log(players[i].getUserId() + " turn status : " + players[i].getTurn());
			}
		} else if (player4.getTurn() === true){
			player4.toggleTurn();
			player1.toggleTurn();
			for(var i in players){
				console.log(players[i].getUserId() + " turn status : " + players[i].getTurn());
			}
		}
	});
	$("#get_whiteJ").click(function(){
		
		for(var i in players){
			if(players[i].getTurn() === true){
				players[i].getWhite();
			}
		}
	});
	
	
	function turnPlayer(){		//현재 사용하지 않음
		if(player1.getTurn() === false){				
				$("#popbutton").attr("disabled", true);
		} else if(player2.getTurn() === false){
				$("#popbutton").attr("disabled", true);
		} else if(player3.getTurn() === false){
				$("#popbutton").attr("disabled", true);
		} else if(player4.getTurn() === false){
				$("#popbutton").attr("disabled", true);
		}
	}
});
