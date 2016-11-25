<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="google-signin-scope" content="profile email">
<meta name="google-signin-client_id"
	content="768530434374-au3vrrllnhr3a96h3i6utec28filmqcn.apps.googleusercontent.com">
<title>Splendy - 환영합니다!</title>
<script type="text/javascript" src="https://static.nid.naver.com/js/naverLogin_implicit-1.0.2.js" charset="utf-8"></script>
<link rel='stylesheet' href='/css/index.css'>
<link rel='stylesheet' href='/css/default.css'>
<link rel='stylesheet'
	href='/webjars/bootstrap/3.3.4/dist/css/bootstrap.min.css' />
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
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="/webjars/bootstrap/3.3.4/dist/js/bootstrap.js"></script>
<script src="/webjars/handlebars/4.0.5/handlebars.js"></script>
<script src='/js/default.js'></script>
<script src="https://apis.google.com/js/platform.js" async defer></script>
<script src='/js/index.js'></script>
<script type='text/javascript' src='/js/index.js'></script>

<script>
	var msg = "${msg}";

	$(document).ready(function() {
		if (msg !== "") {
			$('#myModal').modal('show');
		}
	});
</script>

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body bgcolor="#191919">
<div id="fb-root"></div>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="index-frame">
					<div class="index-image-frame">
						<div class="index-top"></div>
						<div class="index-right-frame" id="joinDiv" style="display: none">
							<p class="index-title">회원가입</p>
							<img src="/img/work/index-hr.png" width="310" height="5" alt="" />
							<div class="index-cont">
								<form id="joinForm" name="joinForm" method="post">
									<div class="form-group">
										<label for="textfield">이메일 주소</label> <input name="email"
											type="email" class="form-control" id="email">
									</div>
									<div class="form-group">
										<label for="textfield">닉네임</label> <input name="nickname"
											type="text" class="form-control" id="nickname">
									</div>
									<div class="form-group">
										<label for="exampleInputPassword1">암호</label> <input
											name="password" type="password" class="form-control"
											id="password" placeholder="암호">
									</div>
									<input id="btn_join_prc" class="btn btn-default btn_join_prc"
										type="button" value="회원가입" /> <input id="btn_login"
										class="btn btn-default btn_login" type="button" value="로그인" />
									<input id="btn_send_pw" class="btn btn-default btn_send_pw"
										type="button" value="비밀번호 찾기" />
								</form>
							</div>
						</div>
						<div id="testDiv" style="display: none"></div>
						<div class="index-right-frame" id="loginDiv">
							<p class="index-title">로그인</p>
							<img src="/img/work/index-hr.png" width="310" height="5" alt="" />
							<div class="index-cont">
								<form id="loginForm" name="loginForm" method="post"
									onsubmit="return login_check();">
									<div class="form-group">
										<label for="textfield">이메일 주소</label> <input name="email"
											type="email" class="form-control" id="email" />
									</div>
									<div class="form-group">
										<label for="exampleInputPassword1">암호</label> <input
											name="password" type="password" class="form-control"
											id="exampleInputPassword1" name="password" placeholder="암호" />

									</div>
									<input id="btn_login_prc" class="btn btn-default btn_login_prc"
										type="button" onclick="return login_check();" value="로그인" />
									<input id="btn_send_pw" class="btn btn-default btn_send_pw"
										type="button" value="비밀번호 찾기" /> <input id="btn_join"
										class="btn btn-default btn_join" type="button" value="회원가입" />
									<input id="btn_facebook" class="btn btn-default btn_facebook"
										type="button" value="페이스북으로 로그인" />
									<div class="g-signin2" id="google" data-onsuccess="onSignIn"></div>
									<!-- 네이버아이디로로그인 버튼 노출 영역 -->
									<div id="naver_id_login"></div>
									<!-- //네이버아이디로로그인 버튼 노출 영역 -->

								</form>
								<form name="googleForm" id="googleForm" method="post">
									<input type="hidden" name="email" value=""> <input
										type="hidden" name="nickname" value="">
								</form>
							</div>
						</div>
						<div class="index-right-frame" id="login_sucDiv"
							style="display: none">
							<p class="index-title">로그인성공</p>
							<img src="/img/work/index-hr.png" width="310" height="5" alt="" />
							<div class="index-cont">
								<div class="form-group">
									<label for="textfield"><b>${user.nickname}</b>님 환영합니다.</label>
								</div>
								<input id="btn_logout" class="btn btn-default btn_logout"
									type="button" onclick="location.href='/user/logout'"
									value="로그아웃" /> <input id="btn_modify"
									class="btn btn-default btn_modify" type="button" value="정보수정" />
								<input id="btn_delete" class="btn btn-default btn_delete"
									type="button" onclick="return delete_check()" value="회원탈퇴" />
							</div>
						</div>
						<div class="index-right-frame" id="sendPwDiv"
							style="display: none">
							<p class="index-title">비밀번호 찾기</p>
							<img src="/img/work/index-hr.png" width="310" height="5" alt="" />
							<div class="index-cont">
								<form id="sendPwForm" name="sendPwForm" method="post">
									<div class="form-group">
										<label for="textfield">이메일 주소</label> <input name="email"
											type="email" class="form-control" id="email">
									</div>
									<input id="btn_send_prc" class="btn btn-default btn_send_prc"
										type="button" value="비밀번호 찾기" /> <input id="btn_join"
										class="btn btn-default btn_join" type="button" value="회원가입" />
									<input id="btn_login" class="btn btn-default btn_login"
										type="button" value="로그인" />
								</form>
							</div>
						</div>
						<div class="index-right-frame" id="modifyDiv"
							style="display: none">
							<p class="index-title">개인정보 수정</p>
							<img src="/img/work/index-hr.png" width="310" height="5" alt="" />
							<div class="index-cont">
								<form id="modifyForm" name="modifyForm" method="post">
									<div class="form-group">
										<label for="textfield">email : </label> ${user.email} <br />
										<input name="email" type="hidden" class="form-control"
											id="email" value="${user.email}"> <label
											for="textfield">nickname : </label> <input name="nickname"
											type="text" class="form-control" id="nickname"
											value="${user.nickname}"> <label for="textfield">password
											: </label> <input name="password" type="password"
											class="form-control" id="password">
									</div>
									<input id="btn_modify_prc"
										class="btn btn-default btn_modify_prc" type="button"
										value="정보수정" />
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-12 index-bottom">
				<span>Copyright © spd.cu.cc All rights reserved.</span>
			</div>
		</div>
	</div>

	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">${msg}</h4>
				</div>
				<div class="modal-body">로그인해주세요.</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<!-- <button type="button" class="btn btn-primary">Save changes</button> -->
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var naver_id_login = new naver_id_login("iM6rVSYTz69Duz5F99Mp",
				"http://spd.cu.cc/login/naver_loginPro");
		var state = naver_id_login.getUniqState();
		naver_id_login.setButton("green", 1, 40);
		naver_id_login.setDomain("http://spd.cu.cc/");
		naver_id_login.setState(state);
		naver_id_login.setPopup();
		naver_id_login.init_naver_id_login();
	</script>
</body>
</html>
