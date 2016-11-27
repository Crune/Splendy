<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height: 100%">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Splendy - 환영합니다!</title>
<link rel='stylesheet' href='/css/default.css'>
<link rel='stylesheet' href='/css/rank.css'>
<link rel='stylesheet' href='/css/lobby.css'>
<link rel='stylesheet'
	href='/webjars/bootstrap/3.3.4/dist/css/bootstrap.min.css' />
<style type="text/css">
body, td, th {
	color: #777777;
	font-family: "나눔고딕";
	_behavior: url(/js/iepngfix.htc)
}

body {
	background-color: #191919;
	height: 100%;
}
</style>
<script type='text/javascript'>
	var uid = ${sessionScope.user.id};
	var nick = "${sessionScope.user.nickname}";
</script>

<script type='text/javascript' src="/webjars/jquery/2.1.3/dist/jquery.min.js"></script>
<script type='text/javascript' src="/webjars/bootstrap/3.3.4/dist/js/bootstrap.js"></script>
<script type='text/javascript' src="/webjars/handlebars/4.0.5/handlebars.js"></script>
<script type='text/javascript' src="/webjars/sockjs-client/1.1.1/sockjs.min.js"></script>
<script type='text/javascript' src="/webjars/stomp-websocket/2.3.3/stomp.min.js"></script>
<script id="temp_chatmsg" type="text/x-handlebars-template">
	<div class="chat_msg"><span class="nick_{{ type }}">{{ nick }}:</span> {{ cont }} <span class="msg_time">- {{ time }}</span></div>
</script>

<script id="temp_room_empty" type="text/x-handlebars-template">
	<div class="lobby_room empty_room" id="room_0">
		<span class="newroom_text">여기를 눌러 방을 개설하세요!</span>
	</div>
</script>
<script id="temp_room" type="text/x-handlebars-template">
	<div class="lobby_room" id="room_{{ id }}" name="room_{{ id }}">
		<div class="row">
			<div class="room_detail col-md-5">
				<div class="room_name">{{ title }}</div>
				<div class="room_info">{{ info }}</div>
			</div>
			<div class="room_player col-md-7">
			</div>
		</div>
		<div name="ispw_{{ id }}" id="ispw_{{ id }}" class="row" style="display: none;">
			<div class="col-md-12 room_pw">
				<div class="input-group">
					<input name="rpw_{{ id }}" id="rpw_{{ id }}" type="password" class="form-control" placeholder="비밀번호를 입력하여 주세요.">
					<span class="input-group-btn">
						<button name="btn_pwroom_{{ id }}" id="btn_pwroom_{{ id }}" class="btn btn-default btn_joinroom" type="button">접속!</button>
					</span>
				</div>
			</div>
		</div>
	</div>
</script>
<script id="temp_player" type="text/x-handlebars-template">
	<div class="player {{ role }}" id="user_{{ uid }}" name="user_{{ uid }}" style="padding-bottom: 10px">
		<div class="room_icon">
			<img src="{{ icon }}" width="50px" height="50px" />
		</div>
		<div class="room_nickname">{{ nick }}</div>
		<div class="room_rate">{{ rating }}</div>
	</div>
</script>
<script type='text/javascript' src='/js/default.js'></script>
<script type='text/javascript' src='/js/sock.js'></script>
<script type='text/javascript' src='/js/lobby/starter.js'></script>
<script type='text/javascript' src='/js/lobby/account.js'></script>
<script type='text/javascript' src='/js/lobby/onchat.js'></script>
<script type='text/javascript' src='/js/lobby/onroom.js'></script>
<script type='text/javascript' src='/js/lobby/onplayer.js'></script>

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body bgcolor="#191919">
<div id="fb-root"></div>
	<div class="container-fluid lobby_frame">
		<div id="top">
			<div class="container-fluid lobby_white_line" style="height: 5px"></div>
			<div class="container">
				<div class="row">
					<div class="lobby_top align_left" " id="logo">
						<img src="/img/logo.png">
					</div>
					<div class="lobby_top align_right">
						<sec:authorize access="hasAuthority('admin')">
							<button type="button" onclick="window.location='/admin/index'" class="btn btn-default btn-xs">관리자</button>
						</sec:authorize>
						<button type="button" class="btn btn-default btn-xs">공지사항</button>
						<button type="button" class="btn btn-default btn-xs" id="btn_rank">TOP
							20 Player</button>
						<button type="button" class="btn btn-default btn-xs">자유게시판</button>

						<button type="button" class="btn btn-default btn-xs" id="btn_modify">정보수정</button>
						<button type="button" class="btn btn-default btn-xs" id="btn_logout" data-onsuccess="onSignOut">로그아웃</button>

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
							<img id="userProfIcon" src="" />
						</div>
						<div class="profile_info">
							<div>
								<span class="lobby_text_nick">${sessionScope.user.nickname}</span>
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
									<div class="lobby_text_value">${sessionScope.profile.win+sessionScope.profile.lose+sessionScope.profile.draw}</div>
									<div class="lobby_text_biglabel">참가 게임수</div>
									<div class="lobby_text_sublabel">일별 게임 수: 0 <span class="lobby_text_subsublabel">/5 (일일미션)</span></div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="lobby_profile_data">
									<div class="lobby_text_value">${sessionScope.profile.totalTime}</div>
									<div class="lobby_text_biglabel">총 플레이 시간</div>
									<div class="lobby_text_sublabel">마지막: 0:12:31</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="lobby_profile_data">
									<div class="lobby_text_value">${sessionScope.profile.win}-${sessionScope.profile.lose}-${sessionScope.profile.draw}</div>
									<div class="lobby_text_biglabel">전적(승-패-무)</div>
									<div class="lobby_text_sublabel">
										마지막: 무승부</span>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="lobby_profile_data">
									<div class="lobby_text_value">${sessionScope.profile.rate}</div>
									<div class="lobby_text_biglabel">게임 레이팅</div>
									<div class="lobby_text_sublabel">최근 증감: +0</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="container lobby_white_line" style="height: 2px"></div>
		</div>
		<div class="container lobby_cont" style="height: calc(100% - 319px);">
			<div class="row" style="height: 100%">
				<div class="col-md-4 chat_frame" style="height: 100%">
					<div class="chat_msg_frame" id="chatDiv">
						<div class="chat_msg">
							<span class="nick_o">손님4885:</span> 이게 대체 뭠미? <span
								class="msg_time">- 16:52</span>
						</div>
						<div class="chat_msg">
							<span class="nick_o">손님4885:</span> 이거 망겜인듯 <span
								class="msg_time">- 16:53</span>
						</div>
						<div class="chat_msg">
							<span class="nick_me">최윤일지도모릅:</span> 개객끼야! <span
								class="msg_time">- 16:54</span>
						</div>
					</div>
					<div class="chat_input_frame">
						<input name="chat_input" id="chat_input" type="text"
							class="form-control" />
					</div>
				</div>
				<div class="col-md-8 room_frame" style="height: 100%">
					<div class="row lobby_newroom" id="createRoom">
						<form id="form_newroom" name="form_newroom">
							<div class="row">
								<div class="col-md-8">
									<span class="newroom_title">방제목</span> <input name="title"
										id="title" type="text" class="form-control" /> <span
										class="newroom_title">소갯말</span>
									<textarea name="info" id="info" type="text"
										class="form-control"></textarea>
								</div>
								<div class="col-md-4">
									<span class="newroom_title">비밀번호</span> <input name="password"
										id="password" type="password" class="form-control" /> <span
										class="newroom_title">인원제한(2~4명)</span> <input
										name="playerLimits" id="playerLimits" type="text"
										class="form-control" />
								</div>
							</div>
							<br />
							<button id="btn_create" type="button" class="btn btn-default">방개설</button>
							<button id="btn_create_cancel" type="button"
								class="btn btn-default">취소</button>
						</form>

					</div>
					<div id="roomlist" name="roomlist">

						<div class="lobby_room" id="room_1">
							<div class="room_detail col-md-5">
								<div class="room_name">아무나 들어오세요</div>
								<div class="room_info">너만 빼고 너만 빼고 너만 빼고 너만 빼고 너만 빼고 너만 빼고
									너만 빼고 너만 빼고 너만 빼고 너만 빼고</div>
							</div>
							<div class="room_player col-md-7">
								<div class="player host" id="uid">
									<div class="room_icon">
										<img src="/img/unnamed.png" width="50px" height="50px" />
									</div>
									<div class="room_nickname">닉네임입니다</div>
									<div class="room_rate">레이팅</div>
								</div>
								<div class="player" id="uid">
									<div class="room_icon">
										<img src="/img/unnamed.png" width="50px" height="50px" />
									</div>
									<div class="room_nickname">닉네임</div>
									<div class="room_rate">레이팅</div>
								</div>
								<div class="player" id="uid">
									<div class="room_icon">
										<img src="/img/unnamed.png" width="50px" height="50px" />
									</div>
									<div class="room_nickname">닉네임</div>
									<div class="room_rate">레이팅</div>
								</div>
								<div class="player" id="uid">
									<div class="room_icon">
										<img src="/img/unnamed.png" width="50px" height="50px" />
									</div>
									<div class="room_nickname">닉네임</div>
									<div class="room_rate">레이팅</div>
								</div>
							</div>
						</div>
						<div class="lobby_room empty_room" id="room_0">
							<span class="newroom_text">여기를 눌러 방을 개설하세요!</span>
						</div>
					</div>
					<div class="lobby_players">
						<c:forEach begin="1" end="20">
							<div class="player" id="uid" style="padding-bottom: 10px">
								<div class="room_icon">
									<img src="/img/unnamed.png" width="50px" height="50px" />
								</div>
								<div class="room_nickname">닉네임</div>
								<div class="room_rate">레이팅</div>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" role="dialog"
		aria-labelledby="gridSystemModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="gridSystemModalLabel">Modal title</h4>
				</div>
				<div class="modal-body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-4">.col-md-4</div>
							<div class="col-md-4 col-md-offset-4">.col-md-4
								.col-md-offset-4</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary">Save changes</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->

	<div class="modal fade" id="modify_modal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 265px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">정보 수정</h4>
				</div>
				<div class="modal-body">
					<form method="post" name="modal_form" id="modal_form">
						<table>
							<tr>
								<input type="hidden" class="form-control" name="email"
									id="email" value="${user.email}" />
								<td>비밀번호&nbsp;</td>
								<td><input type="password" name="password"
									class="form-control" id="password" /></td>
							</tr>
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td>닉네임</td>
								<td><input name="nickname" type="text" class="form-control"
									id="nickname" value="${user.nickname}" /></td>
							</tr>
							<tr>
								<td colspan="2"><br />아이콘 <img src="/img/top_icon1.png"
									id="icon1" class="icon" /> <img src="/img/top_icon2.png"
									id="icon2" class="icon" /> <img src="/img/top_icon3.png"
									id="icon3" class="icon" /> <img src="/img/top_icon4.png"
									id="icon4" class="icon" /></td>
							</tr>

						</table>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
					<button type="button" class="btn btn-primary" name="btn_modify_prc"
						id="btn_modify_prc">저장하기</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="modify_modal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 265px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">정보 수정</h4>
				</div>
				<div class="modal-body">
					<form method="post" name="modal_form" id="modal_form">
						<table>
							<tr>
								<input type="hidden" class="form-control" name="email"
									id="email" value="${user.email}" />
								<td>비밀번호&nbsp;</td>
								<td><input type="password" name="password"
									class="form-control" id="password" /></td>
							</tr>
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td>닉네임</td>
								<td><input name="nickname" type="text" class="form-control"
									id="nickname" value="${user.nickname}" /></td>
							</tr>
							<tr>
								<td colspan="2"><br />아이콘 <img src="/img/top_icon1.png"
									id="icon1" class="icon" /> <img src="/img/top_icon2.png"
									id="icon2" class="icon" /> <img src="/img/top_icon3.png"
									id="icon3" class="icon" /> <img src="/img/top_icon4.png"
									id="icon4" class="icon" /></td>
							</tr>

						</table>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
					<button type="button" class="btn btn-primary" name="btn_modify_prc"
						id="btn_modify_prc">저장하기</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="rank_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 500px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">상위 20명</h4>
				</div>
				<div class="modal-body">
					<table class="table table-hover table-striped">
						<tr class="info">
							<td>순위</td>
							<td>닉네임</td>
							<td>승리</td>
							<td>패배</td>
							<td>무승부</td>
							<td>레이팅</td>
						</tr>
						<c:forEach var="prof" items="${profList}" varStatus="status">
							<tr>
								<td>${status.count}위</td>
								<td>${prof.nickname}</td>
								<td>${prof.win}</td>
								<td>${prof.lose}</td>
								<td>${prof.draw}</td>
								<td>${prof.rate}</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class="modal-footer">
				</div>
			</div>
		</div>
	</div>

</body>
</html>