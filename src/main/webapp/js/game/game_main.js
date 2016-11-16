var gameSock = null;
var player1 = new Player("dohyun", 1);
var player2 = new Player("yoon", 2);
var player3 = new Player("minjung", 3);
var player4 = new Player("jinkyu", 4);

var INIT_POINT = -3;
var ONE_POINT = 1;
var TWO_POINT = 2;
var THREE_POINT = 3;

var orderCount = 0;
var players = [ player1, player2, player3, player4 ];
var game_status;

$(document).ready(
		function() {

			// 페이지가 시작됨과 동시에 소켓 서버 주소로 접속한다.
			gameSock = new SockJS("http://" + window.location.host + "/gameMain");

			gameSock.onopen = function() {
				console.log('welcome');
				player1.toggleTurn();
				$("#player1").css("background-color", "red");
				for ( var i in players) {
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

			$("#popbutton").click(
					function() {
						if (player1.getTurn() === true) {
							player1.toggleTurn();
							player2.toggleTurn();

							$("#player1").css("background-color", "tomato");
							$("#player2").css("background-color", "red");
							for ( var i in players) {
								console.log(players[i].getUserId()
										+ " turn status : "
										+ players[i].getTurn());
							}
						} else if (player2.getTurn() === true) {
							player2.toggleTurn();
							player3.toggleTurn();

							$("#player2").css("background-color", "tomato");
							$("#player3").css("background-color", "red");
							for ( var i in players) {
								console.log(players[i].getUserId()
										+ " turn status : "
										+ players[i].getTurn());
							}
						} else if (player3.getTurn() === true) {
							player3.toggleTurn();
							player4.toggleTurn();

							$("#player3").css("background-color", "tomato");
							$("#player4").css("background-color", "red");
							for ( var i in players) {
								console.log(players[i].getUserId()
										+ " turn status : "
										+ players[i].getTurn());
							}
						} else if (player4.getTurn() === true) {
							player4.toggleTurn();
							player1.toggleTurn();

							$("#player4").css("background-color", "tomato");
							$("#player1").css("background-color", "red");
							for ( var i in players) {
								console.log(players[i].getUserId()
										+ " turn status : "
										+ players[i].getTurn());
							}
						}
					});

			$("#get_whiteJ").click(
					function() {
						var player;
						var nextPlayer;
						if (orderCount < 3) {
							player = players[orderCount]; // 현재 턴의 플레이어 설정
							nextPlayer = players[orderCount + 1]; // 다음 턴 플레이어
							// 설정
						} else {
							player = players[3]; // 네 번째 플레이어가 현재일 때
							nextPlayer = players[0]; // 다음 플레이어를 첫 번째 플레이어로
							// 설정

						}

						player.getWhite();

						if (player.getWhiteJewelStatus() === false) {
							player.setActionPoint(ONE_POINT);
							player.toggleWhiteJewelStatus(); // true
						} else if (player.getWhiteJewelStatus() === true) {
							player.setActionPoint(TWO_POINT);
						}

						if (player.getActionPoint() === THREE_POINT) {
							player.toggleTurn();
							player.toggleWhiteJewelStatus(); // false
							player.setActionPoint(INIT_POINT);
							nextPlayer.toggleTurn();
							orderCount++;
							$("#player" + (orderCount)).css("background-color",
									"tomato");
							$("#player" + (orderCount + 1)).css("background-color", "red");
							if (orderCount === 4) {
								orderCount = 0; // 플레이어 선택자를 초기화
								$("#player" + (orderCount + 1)).css("background-color", "red");
							}
						}

					});

			$("#get_greenJ").click(
					function() {
						var player;
						var nextPlayer;
						if (orderCount < 3) {
							player = players[orderCount]; // 현재 턴의 플레이어 설정
							nextPlayer = players[orderCount + 1]; // 다음 턴 플레이어
							// 설정
						} else {
							player = players[3]; // 네 번째 플레이어가 현재일 때
							nextPlayer = players[0]; // 다음 플레이어를 첫 번째 플레이어로
							// 설정
						}

						player.getGreen();

						if (player.getGreenJewelStatus() === false) {
							player.setActionPoint(ONE_POINT);
							player.toggleGreenJewelStatus(); // true
						} else if (player.getGreenJewelStatus() === true) {
							player.setActionPoint(TWO_POINT);
						}

						if (player.getActionPoint() === THREE_POINT) {
							player.toggleTurn();
							player.toggleGreenJewelStatus(); // false
							player.setActionPoint(INIT_POINT);
							nextPlayer.toggleTurn();
							orderCount++;
							$("#player" + (orderCount)).css("background-color","tomato");
							$("#player" + (orderCount + 1)).css(
									"background-color", "red");
							if (orderCount === 4) {
								orderCount = 0; // 플레이어 선택자를 초기화
								$("#player" + (orderCount + 1)).css("background-color", "red");
							}
						}
					});
			$("#get_blueJ").click(
					function() {
						var player;
						var nextPlayer;
						if (orderCount < 3) {
							player = players[orderCount]; // 현재 턴의 플레이어 설정
							nextPlayer = players[orderCount + 1]; // 다음 턴 플레이어
							// 설정
						} else {
							player = players[3]; // 네 번째 플레이어가 현재일 때
							nextPlayer = players[0]; // 다음 플레이어를 첫 번째 플레이어로
							// 설정

						}

						player.getBlue();

						if (player.getBlueJewelStatus() === false) {
							player.setActionPoint(ONE_POINT);
							player.toggleBlueJewelStatus(); // true
						} else if (player.getBlueJewelStatus() === true) {
							player.setActionPoint(TWO_POINT);
						}

						if (player.getActionPoint() === THREE_POINT) {
							player.toggleTurn();
							player.toggleBlueJewelStatus(); // false
							player.setActionPoint(INIT_POINT);
							nextPlayer.toggleTurn();
							orderCount++;
							$("#player" + (orderCount)).css("background-color", "tomato");
							$("#player" + (orderCount + 1)).css("background-color", "red");
							if (orderCount === 4) {
								orderCount = 0; // 플레이어 선택자를 초기화
								$("#player" + (orderCount + 1)).css("background-color", "red");
							}
						}
					});
			$("#get_redJ").click(
					function() {
						var player;
						var nextPlayer;
						if (orderCount < 3) {
							player = players[orderCount]; // 현재 턴의 플레이어 설정
							nextPlayer = players[orderCount + 1]; // 다음 턴 플레이어
							// 설정
						} else {
							player = players[3]; // 네 번째 플레이어가 현재일 때
							nextPlayer = players[0]; // 다음 플레이어를 첫 번째 플레이어로
														// 설정

						}

						player.getRed();

						if (player.getRedJewelStatus() === false) {
							player.setActionPoint(ONE_POINT);
							player.toggleRedJewelStatus(); // true
						} else if (player.getRedJewelStatus() === true) {
							player.setActionPoint(TWO_POINT);
						}

						if (player.getActionPoint() === THREE_POINT) {
							player.toggleTurn();
							player.toggleRedJewelStatus(); // false
							player.setActionPoint(INIT_POINT);
							nextPlayer.toggleTurn();
							orderCount++;
							$("#player" + (orderCount)).css("background-color",
									"tomato");
							$("#player" + (orderCount + 1)).css(
									"background-color", "red");
							if (orderCount === 4) {
								orderCount = 0; // 플레이어 선택자를 초기화
								$("#player" + (orderCount + 1)).css(
										"background-color", "red");
							}
						}
					});
			$("#get_blackJ").click(
					function() {
						var player;
						var nextPlayer;
						if (orderCount < 3) {
							player = players[orderCount]; // 현재 턴의 플레이어 설정
							nextPlayer = players[orderCount + 1]; // 다음 턴 플레이어
							// 설정
						} else {
							player = players[3]; // 네 번째 플레이어가 현재일 때
							nextPlayer = players[0]; // 다음 플레이어를 첫 번째 플레이어로
														// 설정

						}

						player.getBlack();

						if (player.getBlackJewelStatus() === false) {
							player.setActionPoint(ONE_POINT);
							player.toggleBlackJewelStatus(); // true
						} else if (player.getBlackJewelStatus() === true) {
							player.setActionPoint(TWO_POINT);
						}

						if (player.getActionPoint() === THREE_POINT) {
							player.toggleTurn();
							player.toggleBlackJewelStatus(); // false
							player.setActionPoint(INIT_POINT);
							nextPlayer.toggleTurn();
							orderCount++;
							$("#player" + (orderCount)).css("background-color",
									"tomato");
							$("#player" + (orderCount + 1)).css(
									"background-color", "red");
							if (orderCount === 4) {
								orderCount = 0; // 플레이어 선택자를 초기화
								$("#player" + (orderCount + 1)).css(
										"background-color", "red");
							}
						}
					});

			function turnPlayer() { // 현재 사용하지 않음
				if (player1.getTurn() === false) {
					$("#popbutton").attr("disabled", true);
				} else if (player2.getTurn() === false) {
					$("#popbutton").attr("disabled", true);
				} else if (player3.getTurn() === false) {
					$("#popbutton").attr("disabled", true);
				} else if (player4.getTurn() === false) {
					$("#popbutton").attr("disabled", true);
				}
			}
		});
