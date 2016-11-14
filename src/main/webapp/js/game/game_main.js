var gameSock = null;
var player1 = new Player('dohyun'); //접속시에 아이디는 여기로 받아서 플레이어 객체를 생성
var player2 = new Player('yoon');
var player3 = new Player();
var player4 = new Player();

var game_status;

$(document).ready(function() {
	
	// 페이지가 시작됨과 동시에 소켓 서버 주소로 접속한다.
	gameSock = new SockJS("http://" + window.location.host + "/gameMain");
	
	gameSock.onopen = function() {
		console.log("websocket success");
		$("#player_name_1").html(player1.id);
		$("#player_name_2").html(player2.id);
		$("#player_name_3").html(player3.id);
		$("#player_name_4").html(player4.id);
		
	};

	gameSock.onmessage = function(evt) {
		game_status = JSON.parse(evt.data);		
		if (game_status.id === player1.id) {
			$("#player1_score").html(game_status.score);
		} else if (game_status.id === player2.id) {
			$("#player2_score").html(game_status.score);
		}
	};

	gameSock.onclose = function() {
	};

	$("#jewel_1").click(function() {
		player1.getJewel();
	});

	$("#jewel_2").click(function() {
		player2.getJewel();
	});

});
