<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<script type='text/javascript'
	src="/webjars/jquery/2.1.3/dist/jquery.min.js"></script>
<script type='text/javascript'
	src="/webjars/sockjs-client/0.3.4/sockjs.min.js"></script>
<script type='text/javascript' src="/js/game/Game_player.js"></script>
<script type='text/javascript' src="/js/game/Game_field_jewel.js"></script>
<script type='text/javascript' src="/js/game/game_main.js"></script>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<link rel='stylesheet' href='/css/game_main.css'>

<title>Insert title here</title>
<script type="text/javascript">
	$(function() {
		$("#popbutton").click(function() {
			$('div.modal').modal({
				remote : '/view/game/game_modal.jsp'
			});
		})
	})
</script>
</head>
<body>
	<div class="container">
		<div class="container_player">
			<div class="player" id="player_name_1">player1</div>
			<div id="player1_score">4</div>
			<div class="player" id="player_name_2">player2</div>
			<div id="player2_score">4</div>
			<div class="player" id="player_name_3">player3</div>
			<div class="player" id="player_name_4">player4</div>
		</div>
		<div class="jewel_group">
			<div class="jewel">
				<button type="button" class="btn btn-default" id="jewel_1">Left</button>
				gold
			</div>
			<div class="jewel">
				<button type="button" class="btn btn-default" id="jewel_2">Left</button>
				gold
			</div>
			<div class="jewel">
				<button type="button" class="btn btn-default">Left</button>
				gold
			</div>
			<div class="jewel">
				<button type="button" class="btn btn-default">Left</button>
				gold
			</div>
			<div class="jewel"><button type="button" class="btn btn-default">Left</button>
				gold
				</div>

			<button class="btn btn-default" id="popbutton">모달출력버튼</button>
			<br />
			<div class="modal fade">
				<div class="modal-dialog">
					<div class="modal-content">
						<!-- remote ajax call이 되는영역 -->
					</div>
				</div>
			</div>

		</div>
		<div class="cards_container">
			<div class="level">
				<div class="card">
					<img src="/img/iron.png" alt="">
				</div>
				<div class="card">
					<img src="/img/iron.png" alt="">
				</div>
				<div class="card">
					<img src="/img/iron.png" alt="">
				</div>
				<div class="card">
					<img src="/img/iron.png" alt="">
				</div>
			</div>

			<div class="level">
				<div class="card">
					<img src="/img/iron.png" alt="">
				</div>
				<div class="card">
					<img src="/img/iron.png" alt="">
				</div>
				<div class="card">
					<img src="/img/iron.png" alt="">
				</div>
				<div class="card">
					<img src="/img/iron.png" alt="">
				</div>
			</div>
			<div class="level">
				<div class="card">
					<img src="/img/iron.png" alt="">
				</div>
				<div class="card">
					<img src="/img/iron.png" alt="">
				</div>
				<div class="card">
					<img src="/img/iron.png" alt="">
				</div>
				<div class="card">
					<img src="/img/iron.png" alt="">
				</div>
			</div>
			<div class="level">
				<div class="card">
					<img src="/img/iron.png" alt="">
				</div>
				<div class="card">
					<img src="/img/iron.png" alt="">
				</div>
				<div class="card">
					<img src="/img/iron.png" alt="">
				</div>
				<div class="card">
					<img src="/img/iron.png" alt="">
				</div>
			</div>

		</div>
	</div>
</body>
</html>