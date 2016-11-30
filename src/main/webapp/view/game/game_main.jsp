<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>

<script type="text/javascript">
	
</script>
</head>
<body>
	<div>
		<button id="left_btn">Go lobby</button>
		</div>
	<div class="container">
		<div class="container_player">
			<div class="player" id="player1">
				<div class="player_name" id="player1_name">null</div>
				</br>
				<div class="player_value">
					<div class="player1_jewel">
						보석 <br>
						<p id="player1_jewel_white">0</p>
						<p id="player1_jewel_green">0</p>
						<p id="player1_jewel_blue">0</p>
						<p id="player1_jewel_red">0</p>
						<p id="player1_jewel_black">0</p>
						<p id="player1_jewel_gold">0</p>
					</div>
					<div class="player1_card">
						카드 <br>
						<p id="player1_card_white">0</p>
						<p id="player1_card_green">0</p>
						<p id="player1_card_blue">0</p>
						<p id="player1_card_red">0</p>
						<p id="player1_card_black">0</p>
					</div>
				</div>
			</div>

			<div class="player" id="player2">
				<div class="player_name" id="player2_name">null</div>
				</br>
				<div class="player_value">
					<div class="player2_jewel">
						보석 <br>
						<p id="player2_jewel_white">0</p>
						<p id="player2_jewel_green">0</p>
						<p id="player2_jewel_blue">0</p>
						<p id="player2_jewel_red">0</p>
						<p id="player2_jewel_black">0</p>
						<p id="player2_jewel_gold">0</p>
					</div>
					<div class="player2_card">
						카드 <br>
						<p id="player2_card_white">0</p>
						<p id="player2_card_green">0</p>
						<p id="player2_card_blue">0</p>
						<p id="player2_card_red">0</p>
						<p id="player2_card_black">0</p>
					</div>
				</div>
			</div>
			<div class="player" id="player3">
				<div class="player_name" id="player3_name">null</div>
				</br>
				<div class="player_value">
					<div class="player3_jewel">
						보석 <br>
						<p id="player3_jewel_white">0</p>
						<p id="player3_jewel_green">0</p>
						<p id="player3_jewel_blue">0</p>
						<p id="player3_jewel_red">0</p>
						<p id="player3_jewel_black">0</p>
						<p id="player3_jewel_gold">0</p>
					</div>
					<div class="player3_card">
						카드 <br>
						<p id="player3_card_white">0</p>
						<p id="player3_card_green">0</p>
						<p id="player3_card_blue">0</p>
						<p id="player3_card_red">0</p>
						<p id="player3_card_black">0</p>
					</div>
				</div>
			</div>
			<div class="player" id="player4">
				<div class="player_name" id="player4_name">null</div>
				</br>
				<div class="player_value">
					<div class="player4_jewel">
						보석 <br>
						<p id="player4_jewel_white">0</p>
						<p id="player4_jewel_green">0</p>
						<p id="player4_jewel_blue">0</p>
						<p id="player4_jewel_red">0</p>
						<p id="player4_jewel_black">0</p>
						<p id="player4_jewel_gold">0</p>
					</div>
					<div class="player4_card">
						카드 <br>
						<p id="player4_card_white">0</p>
						<p id="player4_card_green">0</p>
						<p id="player4_card_blue">0</p>
						<p id="player4_card_red">0</p>
						<p id="player4_card_black">0</p>
					</div>
				</div>
			</div>
		</div>
		<div class="jewel_group">
			<div class="jewel">
				<img class="jewel_img" src="/img/cards/jewel_gold.png" />
				<div id="jewel_gold_value"></div>
			</div>

			<div class="jewel">
				<img class="jewel_img" src="/img/cards/jewel_white.png" />
				<div id="jewel_white_value"></div>
			</div>
			<div class="jewel">
				<img class="jewel_img" src="/img/cards/jewel_green.png" />
				<div id="jewel_green_value"></div>
			</div>
			<div class="jewel">
				<img class="jewel_img" src="/img/cards/jewel_blue.png" />
				<div id="jewel_blue_value"></div>
			</div>
			<div class="jewel">
				<img class="jewel_img" src="/img/cards/jewel_red.png" />
				<div id="jewel_red_value"></div>
			</div>
			<div class="jewel">
				<img class="jewel_img" src="/img/cards/jewel_black.png" />
				<div id="jewel_black_value"></div>
			</div>

		</div>

		<div class="deck_container">
			<div class="deck_level">
				<div id="deck_lev3">
					<img src="/img/cards/back_card_lev3.png" />
				</div>
				<div id="lev3_left">20</div>
				<div id="deck_lev2">
					<img src="/img/cards/back_card_lev2.png" />
				</div>
				<div id="lev2_left">20</div>
				<div id="deck_lev1">
					<img src="/img/cards/back_card_lev1.png" />
				</div>
				<div id="lev1_left">20</div>
			</div>
		</div>

		<div class="cards_container">

			<div class="container_cardLevel" id="hero_level">
				<div class="card" id="heroCard_container1">
					<div class="card_define" id="heroCard_detail_1"></div>
				</div>
				<div class="card" id="heroCard_container2">
					<div class="card_define" id="heroCard_detail_2"></div>
				</div>
				<div class="card" id="heroCard_container3">
					<div class="card_define" id="heroCard_detail_3"></div>
				</div>
				<div class="card" id="heroCard_container4">
					<div class="card_define" id="heroCard_detail_4"></div>
				</div>
				<div class="card" id="heroCard_container5">
					<div class="card_define" id="heroCard_detail_5"></div>
				</div>
			</div>

			<div class="container_cardLevel" id="lev3_level">
				<div class="card" id="lev3Card_container1">
					<div class="card_define" id="lev3Card_detail_1"></div>
				</div>
				<div class="card" id="lev3Card_container2">
					<div class="card_define" id="lev3Card_detail_2"></div>
				</div>
				<div class="card" id="lev3Card_container3">
					<div class="card_define" id="lev3Card_detail_3"></div>
				</div>
				<div class="card" id="lev3Card_container4">
					<div class="card_define" id="lev3Card_detail_4"></div>
				</div>
				
			</div>

			<div class="container_cardLevel" id="lev2_level">
				<div class="card" id="lev2Card_container1">
					<div class="card_define" id="lev2Card_detail_1"></div>
				</div>
				<div class="card" id="lev2Card_container2">
					<div class="card_define" id="lev2Card_detail_2"></div>
				</div>
				<div class="card" id="lev2Card_container3">
					<div class="card_define" id="lev2Card_detail_3"></div>
				</div>
				<div class="card" id="lev2Card_container4">
					<div class="card_define" id="lev2Card_detail_4"></div>
				</div>
				
			</div>

			<div class="container_cardLevel" id="lev1_level">
				<div class="card" id="lev1Card_container1">
					<div class="card_define" id="lev1Card_detail_1"></div>
				</div>
				<div class="card" id="lev1Card_container2">
					<div class="card_define" id="lev1Card_detail_2"></div>
				</div>
				<div class="card" id="lev1Card_container3">
					<div class="card_define" id="lev1Card_detail_3"></div>
				</div>
				<div class="card" id="lev1Card_container4">
					<div class="card_define" id="lev1Card_detail_4"></div>
				</div>
				
			</div>

		</div>
	</div>


	<!-- 보석 가져오는 모달창 -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Modal 제목</h4>
				</div>
				<div class="modal-body">
					<div class="jewel">
						<div class="jewel_container_M">
							<img class="jewel_img" id="jewel_white_img"
								src="/img/cards/jewel_white.png" />
							<div class="jewel_value_M" id="jewel_white_value_M"></div>
						</div>
					</div>
					<div class="jewel">
						<div class="jewel_container_M">
							<img class="jewel_img" id="jewel_green_img"
								src="/img/cards/jewel_green.png" />
							<div class="jewel_value_M" id="jewel_green_value_M"></div>
						</div>
					</div>
					<div class="jewel">
						<div class="jewel_container_M">
							<img class="jewel_img" id="jewel_blue_img"
								src="/img/cards/jewel_blue.png" />
							<div class="jewel_value_M" id="jewel_blue_value_M"></div>
						</div>
					</div>
					<div class="jewel">
						<div class="jewel_container_M">
							<img class="jewel_img" id="jewel_red_img"
								src="/img/cards/jewel_red.png" />
							<div class="jewel_value_M" id="jewel_red_value_M"></div>
						</div>
					</div>
					<div class="jewel">
						<div class="jewel_container_M">
							<img class="jewel_img" id="jewel_black_img"
								src="/img/cards/jewel_black.png" />
							<div class="jewel_value_M" id="jewel_black_value_M"></div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
					</div>
				</div>
			</div>
		</div>
		<!-- //보석 가져오는 모달창 -->
	</div>
</body>

<script class="heroCard_details" type="text/x-handlebars-template">
		<img src="{{img}}"/>
</script>

















<script type='text/javascript'
	src="/webjars/jquery/2.1.3/dist/jquery.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
<script type='text/javascript'
	src="/webjars/sockjs-client/1.1.1/sockjs.min.js"></script>
<script type='text/javascript'
	src="/webjars/handlebars/4.0.5/handlebars.js"></script>

<script type="text/javascript" src="/js/commonWS.js"></script>
<script type='text/javascript' src="/js/game/Game_field_jewel.js"></script>
<script type='text/javascript' src="/js/game/Game_CardManager.js"></script>
<script type='text/javascript' src="/js/game/Game_Card.js"></script>
<script type='text/javascript' src="/js/game/Game_player.js"></script>
<script type='text/javascript' src="/js/game/Game_field_jewel.js"></script>


<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<link rel='stylesheet' href='/css/game_main.css'>
<script type='text/javascript' src="/js/game/game_main2.js"></script>
</html>