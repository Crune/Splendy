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
	_behavior: url(/js/iepngfix.htc)
}

body {
	background-color: #191919;
}
</style>
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
		<div class="room_detail col-md-5">
			<div class="room_name">{{ title }}</div>
			<div class="room_info">{{ info }}</div>
		</div>
		<div class="room_player col-md-7">
		</div>
	</div>
</script>
<script id="temp_player" type="text/x-handlebars-template">
	<div class="player {{ role }}" id="user_{{ uid }}" name="user_{{ uid }}" style="padding-bottom: 10px">
		<div class="room_icon">
			<img src="/img/{{ icon }}" width="50px" height="50px" />
		</div>
		<div class="room_nickname">{{ nick }}</div>
		<div class="room_rate">{{ rating }}</div>
	</div>
</script>
<script type='text/javascript' src='/js/default.js'></script>
<script type='text/javascript' src='/js/commonWS.js'></script>
<script type='text/javascript' src='/js/lobby.js'></script>

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
						<button type="button" class="btn btn-default btn-xs" id="btn_modify">정보수정</button>
						<button type="button" class="btn btn-default btn-xs" id="btn_logout">로그아웃</button>
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
				<div class="col-md-4 chat_frame">
					<div class="chat_msg_frame" id="chatDiv">
						<div class="chat_msg"><span class="nick_o">손님4885:</span> 이게 대체 뭠미? <span class="msg_time">- 16:52</span></div>
						<div class="chat_msg"><span class="nick_o">손님4885:</span> 이거 망겜인듯 <span class="msg_time">- 16:53</span></div>
						<div class="chat_msg"><span class="nick_me">최윤일지도모릅:</span> 개객끼야! <span class="msg_time">- 16:54</span></div>
					</div>
					<div class="chat_input_frame">
						<input name="chat_input" id="chat_input" type="text" class="form-control" />
					</div>
				</div>
				<div class="col-md-8 room_frame">
					<div id="roomlist" name="roomlist">
						<div class="lobby_room" id="room_1">
							<div class="room_detail col-md-5">
								<div class="room_name">아무나 들어오세요</div>
								<div class="room_info">너만 빼고 너만 빼고 너만 빼고 너만 빼고 너만 빼고 너만 빼고 너만 빼고 너만 빼고 너만 빼고 너만 빼고</div>
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
	<div class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <h4 class="modal-title" id="gridSystemModalLabel">Modal title</h4>
        </div>
        <div class="modal-body">
          <div class="container-fluid">
            <div class="row">
              <div class="col-md-4">.col-md-4</div>
              <div class="col-md-4 col-md-offset-4">.col-md-4 .col-md-offset-4</div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
          <button type="button" class="btn btn-primary">Save changes</button>
        </div>
      </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
  </div><!-- /.modal -->
  
  <div class="modal fade" id="modify_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">정보 수정</h4>
      </div>
      <div class="modal-body">
        	비밀번호 : 
         	 닉네임  : 
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
  </div>
  
</body>
</html>