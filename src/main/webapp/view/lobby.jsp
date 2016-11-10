<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Splendy - 환영합니다!</title>
<link rel='stylesheet' href='/css/default.css'>
<link rel='stylesheet' href='/css/lobby.css'>
<link rel='stylesheet' href='/webjars/bootstrap/3.3.4/dist/css/bootstrap.min.css' />
<style type="text/css">
body, td, th {
	color: #777777;
	font-family: "나눔고딕";
}

body {
	background-color: #191919;
}
</style>
<script src="/webjars/jquery/2.1.3/dist/jquery.min.js"></script>
<script src="/webjars/bootstrap/3.3.4/dist/js/bootstrap.js"></script>
<script src="/webjars/handlebars/4.0.5/handlebars.js"></script>
<script src='/js/default.js'></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body bgcolor="#191919">
	<div class="container-fluid lobby_frame">
		<div id="top">
			<div class="container-fluid lobby_white_line" style="height: 5px"></div>
			<div class="container">
				<div class="row">
					<div class="lobby_top align_left"" id="logo"><img src="/img/logo.png"></div>
					<div class="lobby_top align_right">
						<button type="button" class="btn btn-default btn-xs">공지사항</button>
						<button type="button" class="btn btn-default btn-xs">자유게시판</button>
						<button type="button" class="btn btn-default btn-xs">로그아웃</button>
					</div>
				</div>
			</div>
			<div class="container-fluid lobby_white_line" style="height: 2px"></div>
		</div>
		<div class="lobby_profile">
			<div class="container lobby_white_line" style="height: 2px"></div>
			<div class="container">
				<div class="row lobby_profile_innner">
					<div class="col-md-4 profile_left">
						<div class="profile_icon">
							<div class="profile_icon_change"></div>
						</div>
						<div class="profile_info">
							<div>
								<span class="lobby_text_nick">최윤일지도모릅</span>
								<button type="button" class="btn btn-default btn-xs">변경</button>
							</div>
							<div class="lobby_text_level">Lv.7</div>
							<div class="lobby_text_biglabel">플레이어 레벨</div>
							<div class="lobby_text_sublabel">경험치: 2150/5000</div>
						</div>
					</div>
					<div class="col-md-8">
						<div class="row">
							<div class="col-md-6">
								<div class="lobby_profile_data">
									<div class="lobby_text_value">74</div>
									<div class="lobby_text_biglabel">참가 게임수</div>
									<div class="lobby_text_sublabel">일별 게임 수: 3 <span class="lobby_text_subsublabel">/5 (일일미션)</span></div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="lobby_profile_data">
									<div class="lobby_text_value">151:31</div>
									<div class="lobby_text_biglabel">총 플레이 시간</div>
									<div class="lobby_text_sublabel">마지막: 0:12:31</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="lobby_profile_data">
									<div class="lobby_text_value">5-14-4</div>
									<div class="lobby_text_biglabel">전적(승-패-무)</div>
									<div class="lobby_text_sublabel">마지막: 무승부</span></div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="lobby_profile_data">
									<div class="lobby_text_value">1524</div>
									<div class="lobby_text_biglabel">게임 레이팅</div>
									<div class="lobby_text_sublabel">최근 증감: +20</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="container lobby_white_line" style="height: 2px"></div>
		</div>
		<div class="container lobby_cont">
			<div class="row">
				<div id="chat_frame">
					<div class="chat_msg">메시지</div>
				</div>
				<div id="room_frame">
					<div class="lobby_room">
						<div class="room_name">방제</div>
						<div class="room_info">설명</div>
					</div>
					<div class="lobby_room empty_room">
						<img/>+버튼
					</div>
					<div id="lobby_players">
						플레이어1
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>