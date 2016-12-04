<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<title>Untitled Document</title>
	<meta charset="UTF-8">
	<meta name="description" content="">
	<meta name="keywords" content="">
	<link rel="stylesheet" href="/css/ingame/main.css">
	<link rel="stylesheet" href="/css/ingame/chat.css">
	<link rel="stylesheet" href="/css/ingame/coin_modal.css">
	<link rel='stylesheet' href='/css/default.css'>
	<!--
	<link rel='stylesheet' href='/webjars/bootstrap/3.3.4/dist/css/bootstrap.min.css' />
	<link rel='stylesheet' href='/webjars/bootstrap/3.3.4/dist/css/bootstrap-theme.min.css' />
	-->
	<style type="text/css">
		body { _behavior: url(/js/iepngfix.htc) }
	</style>
	
	<script id="temp_chatmsg" type="text/x-handlebars-template">
		<div class="chat_msg"><span class="nick_{{ type }}">{{ nick }}:</span> {{ cont }} <span class="msg_time">- {{ time }}</span></div>
	</script>
	<script type='text/javascript'>
		function closeIt() {
			return "페이지를 벗어날 경우 탈주 처리됩니다.";
		}
		window.onbeforeunload = closeIt;

        var uid = ${sessionScope.user.id};
        var nick = "${sessionScope.user.nickname}";
        var email = "${sessionScope.user.email}";
        var rid = ${sessionScope.rid};
	</script>
	
	<script id="temp_player" type="text/x-handlebars-template">
            <ul class="aTop">
				<li class="playerIcon"><img src="{{ icon }}" alt="player icon"></li>
				<li class="playerName">{{ nick }}</li>
				<li class="playerTime"><span>11:23</span></li>
				<li class="playerScore">0</li>
			</ul>
			<ul class="aMid clearFix">
				<li id="pl{{ order }}_cn6"><img src="/img/yellow.png" alt="노란보석"><span>0</span></li>
				<li id="pl{{ order }}_cn1"><img src="/img/white.png" alt="하얀보석"><span>0</span></li>
				<li id="pl{{ order }}_cn2"><img src="/img/green.png" alt="초록보석"><span>0</span></li>
				<li id="pl{{ order }}_cn3"><img src="/img/blue.png" alt="블루보석"><span>0</span></li>
				<li id="pl{{ order }}_cn4"><img src="/img/red.png" alt="빨간보석"><span>0</span></li>
				<li id="pl{{ order }}_cn5"><img src="/img/purple.png" alt="보라보석"><span>0</span></li>
			</ul>
			<ul class="aBottom clearFix">
				<li id="pl{{ order }}_cd1"><img src="/img/cards/lev1_Black_B4.png" alt="플레이어카드1"><span>5</span></li>
				<li id="pl{{ order }}_cd2"><img src="/img/cards/lev1_Black_G1R3K1.png" alt="플레이어카드2"><span>4</span></li>
				<li id="pl{{ order }}_cd3"><img src="/img/cards/lev1_Black_G2R1.png" alt="플레이어카드3"><span>3</span></li>
				<li id="pl{{ order }}_cd4"><img src="/img/cards/lev1_Black_W1B1G1R1.png" alt="플레이어카드4"><span>2</span></li>
				<li id="pl{{ order }}_cd5"><img src="/img/cards/lev1_Black_W2G2.png" alt="플레이어카드5"><span>1</span></li>
			</ul>
    </script>
</head>

<body>
	<div id="mask"></div>
	<div class="window"></div>
	<header class="clearFix">
		<p class="guest">Guest1123</p>
		<p class="backSpace"><a href="#">Back</a></p>
	</header>
	<div id="wrap" class="clearFix">


		<aside>
		<div class="player player1">
			 
		</div>
		<div class="player player2">
			
		</div>

		<div class="player player3">
			
		</div>

		<div class="player player4">
			
		</div>
		</aside>
		<section id="container" class="clearFix">
		<ul id="side">
			<li id="cn_id_6" class="cn"><p class="gold"><img src="/img/yellow.png" alt="노랑메인보석"></p><span>0</span></li>
			<li id="cn_id_1" class="cn"><img src="/img/white.png" alt="하얀메인보석"><span>0</span></li>
			<li id="cn_id_2" class="cn"><img src="/img/green.png" alt="초록메인보석"><span>0</span></li>
			<li id="cn_id_3" class="cn"><img src="/img/blue.png" alt="파랑메인보석"><span>0</span></li>
			<li id="cn_id_4" class="cn"><img src="/img/red.png" alt="빨간메인보석"><span>0</span></li>
			<li id="cn_id_5" class="cn"><img src="/img/purple.png" alt="보라메인보석"><span>0</span></li>
			<p class="new_jewel add_jewel_1"><img src="/img/yellow.png"><span>+</span></p>
            <p class="new_jewel add_jewel_2"><img src="/img/yellow.png"><span>+</span></p>
            <p class="new_jewel add_jewel_3"><img src="/img/yellow.png"><span>+</span></p>
		</ul>

		<div id="main">
			<ul class="mHead">
				<li class="turn">Turn / 10</li>
				<li><img src="/img/turn_back2.png" alt="빨리되감기"></li>
				<li><img src="/img/turn_back1.png" alt="되감기"></li>
				<li><img src="/img/turn_front1.png" alt="빨리감기"></li>
				<li><img src="/img/turn_front2.png" alt="감기"></li>
				<p class="turnNum">16 / 16</p>
			</ul>

			<ul class="hero">
				<li><img src="/img/cards/hero_B3G3R3.png" alt="히어로카드1"></li>
				<li><img src="/img/cards/hero_B3W3G3.png" alt="히어로카드2"></li>
				<li><img src="/img/cards/hero_B3W3K3.png" alt="히어로카드3"></li>
				<li><img src="/img/cards/hero_B4W4.png" alt="히어로카드4"></li>
				<li><img src="/img/cards/hero_G4B4.png" alt="히어로카드5"></li>
			</ul>

			<div class="mVisual">
				<ul class="backCard">
					<li><img src="/img/cards/back_card_lev3.png" alt="카드뒷면1"></li>
					<li><img src="/img/cards/back_card_lev2.png" alt="카드뒷면2"></li>
					<li><img src="/img/cards/back_card_lev1.png" alt="카드뒷면3"></li>
				</ul>
				<div class="mainCard">
					<div class="cardBox cardBox1">
						<article><img id="modal_btn" src="/img/cards/lev1_Red_W2G1K2.png" alt=""></article>
						<article><img src="/img/cards/lev2_Black_B1G4R2.png" alt=""></article>
						<article><img src="/img/cards/lev3_Black_G3R6K3.png" alt=""></article>
						<article><img src="/img/cards/lev1_White_W3B1K1.png" alt=""></article>
					</div>
					<div class="cardBox cardBox2">
						<article><img src="/img/cards/lev2_Green_W2B3K2.png" alt=""></article>
						<article><img src="/img/cards/lev3_White_W3K7.png" alt=""></article>
						<article><img src="/img/cards/lev1_Blue_W1G2R2.png" alt=""></article>
						<article><img src="/img/cards/lev2_Red_W3K5.png" alt=""></article>
					</div>
					<div class="cardBox cardBox3">
						<article><img src="/img/cards/lev1_Red_W2G1K2.png" alt=""></article>
						<article><img src="/img/cards/lev2_Black_B1G4R2.png" alt=""></article>
						<article><img src="/img/cards/lev3_Black_G3R6K3.png" alt=""></article>
						<article><img src="/img/cards/lev1_White_W3B1K1.png" alt=""></article>
					</div>
				</div>
			</div>
		</div>
		</section>
		<div id="game_chat">			
			 
		</div>
		<input type="text" id="chat_input" size="74px"/>

		<div id="card_modal">
			<h3>Test Modal</h3>
			<p>이 창은 모달창입니다.</p>
			<button id="confirm_button">확인</button>
		</div>
		
		<div id="coin_modal">
			<ul id="#side_modal">
			<li id="cn_modal_id_1" class="cn_modal"><img src="/img/white.png" alt="하얀메인보석"><span>0</span></li>
			<li id="cn_modal_id_2" class="cn_modal"><img src="/img/green.png" alt="초록메인보석"><span>0</span></li>
			<li id="cn_modal_id_3" class="cn_modal"><img src="/img/blue.png" alt="파랑메인보석"><span>0</span></li>
			<li id="cn_modal_id_4" class="cn_modal"><img src="/img/red.png" alt="빨간메인보석"><span>0</span></li>
			<li id="cn_modal_id_5" class="cn_modal"><img src="/img/purple.png" alt="보라메인보석"><span>0</span></li>
		</ul>
			<button id="coin_confirm_btn">PICK</button>
		</div>
	</div>
	<footer class="clearFix">
		<p class="users"><a href="#">x 10</a></p>
		<p class="chat"><a href="#" id="chat_btn">System: Game started!</a></p>
	</footer>
</body>
<scripts>
	<script type='text/javascript' src="/webjars/jquery/2.1.3/dist/jquery.min.js"></script>
	<script type='text/javascript' src="/webjars/bootstrap/3.3.4/dist/js/bootstrap.js"></script>
	<script type='text/javascript' src="/webjars/handlebars/4.0.5/handlebars.js"></script>
	<script type='text/javascript' src="/webjars/sockjs-client/1.1.1/sockjs.min.js"></script>
	<script type='text/javascript' src="/webjars/stomp-websocket/2.3.3/stomp.min.js"></script>
	
	<script type="text/javascript" src="/js/default.js"></script>

	<script type="text/javascript" src="/js/ingame/cards.js"></script>
	<script type='text/javascript' src="/js/ingame/popup.js"></script>

	<script type="text/javascript" src="/js/sock.js"></script>
	<script type='text/javascript' src="/js/ingame/starter.js"></script>

	<script type='text/javascript' src="/js/ingame/onchat.js"></script>
	<script type='text/javascript' src="/js/ingame/oncomp.js"></script>
	<script type='text/javascript' src="/js/ingame/onplayer.js"></script>
	<script type='text/javascript' src="/js/ingame/onroom.js"></script>
</scripts>
</html>