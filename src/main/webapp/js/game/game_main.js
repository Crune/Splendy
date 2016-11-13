
var gameSock = null;
var field_jewel = new Field_jewel();

	$(document).ready(function () {
		// 페이지가 시작됨과 동시에 소켓 서버 주소로 접속한다.
		gameSock = new SockJS("http://" + window.location.host + "/game");
		gameSock.onopen = function () {
			console.log("websocket success");
		};

		gameSock.onmessage = function (evt) {
			
			console.log(evt);
			$("#player1_score").html(evt.data);
			
		};

		gameSock.onclose = function () {
		};

		$("#jewel_1").click(function () {
					
				player.mode = "getJewel";
				gameSock.send( JSON.stringify(player) );
				player.score -= 1;
		});
	});
