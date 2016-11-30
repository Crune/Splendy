<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="google-signin-scope" content="profile email">
	<meta name="google-signin-client_id" content="768530434374-au3vrrllnhr3a96h3i6utec28filmqcn.apps.googleusercontent.com">
	<title>Splendy - 환영합니다!</title>
	<link rel='stylesheet' href='/css/index/index.css'>
	<link rel='stylesheet' href='/css/default.css'>
	<link rel='stylesheet' href='/webjars/bootstrap/3.3.4/dist/css/bootstrap.min.css' />
	<style type="text/css">
		body, td, th { color: #777777; font-family: "나눔고딕"; }
		body { background-color: #191919; }
	</style>
	<script type='text/javascript' src="/webjars/jquery/2.1.3/dist/jquery.min.js"></script>
	<script type='text/javascript' src="/webjars/bootstrap/3.3.4/dist/js/bootstrap.js"></script>
	<script type='text/javascript' src="/webjars/handlebars/4.0.5/handlebars.js"></script>

	<script type="text/javascript" src="https://static.nid.naver.com/js/naverLogin_implicit-1.0.2.js" charset="utf-8"></script>
	<script type='text/javascript' src="https://apis.google.com/js/api:client.js"></script>
	<script type='text/javascript' src="https://apis.google.com/js/platform.js" async defer></script>

	<script>
        var msg = "${msg}";
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
									<label for="email">이메일 주소</label>
                                    <input name="email" type="email" class="form-control" id="email">
								</div>
								<div class="form-group">
									<label for="nickname">닉네임</label>
                                    <input name="nickname" type="text" class="form-control" id="nickname">
								</div>
								<div class="form-group">
									<label for="exampleInputPassword1">암호</label>
                                    <input name="password" type="password" class="form-control" id="password" placeholder="암호">
								</div>
								<input id="btn_join_prc" class="btn btn-default btn_join_prc" type="button" value="회원가입" />
								<input id="btn_login" class="btn btn-default btn_login" type="button" value="로그인" />
								<input id="btn_send_pw" class="btn btn-default btn_send_pw" type="button" value="비밀번호 찾기" />
							</form>
						</div>
					</div>
					<div id="testDiv" style="display: none"></div>
					<div class="index-right-frame" id="loginDiv">
						<p class="index-title">로그인</p>
						<img src="/img/work/index-hr.png" width="310" height="5" alt="" />
						<div class="index-cont">
							<form id="loginForm" name="loginForm" method="post">
								<div class="form-group">
									<label for="textfield">이메일 주소</label>
                                    <input name="email" type="email" class="form-control" id="email" />
								</div>
								<div class="form-group">
									<label for="exampleInputPassword1">암호</label>
                                    <input name="password" type="password" class="form-control" id="exampleInputPassword1" name="password" placeholder="암호" />
								</div>
								<input id="btn_login_prc" class="btn btn-default btn_login_prc" type="button" value="로그인" />
								<input id="btn_send_pw" class="btn btn-default btn_send_pw" type="button" value="비밀번호 찾기" />
								<input id="btn_join" class="btn btn-default btn_join" type="button" value="회원가입" />
								<div class="f-login" ><img src="/img/sns/facebook.png" id="btn_facebook" title="페이스북 아이디로 로그인"/></div>
								<div class="g-login" class="customGPlusSignIn"><img src="/img/sns/google.png" title="구글 아이디로 로그인" id="btn_google" /></div>
								<div id="naver_id_login"></div>
							</form>
							<form name="googleForm" id="googleForm" method="post">
								<input type="hidden" name="email" value="">
								<input type="hidden" name="nickname" value="">
							</form>
						</div>
					</div>
					<div class="index-right-frame" id="sendPwDiv" style="display: none">
						<p class="index-title">비밀번호 찾기</p>
						<img src="/img/work/index-hr.png" width="310" height="5" alt="" />
						<div class="index-cont">
							<form id="sendPwForm" name="sendPwForm" method="post">
								<div class="form-group">
									<label for="email">이메일 주소</label>
                                    <input name="email" type="email" class="form-control" id="email">
								</div>
								<input id="btn_send_prc" class="btn btn-default btn_send_prc" type="button" value="비밀번호 찾기" />
								<input id="btn_join" class="btn btn-default btn_join" type="button" value="회원가입" />
								<input id="btn_login" class="btn btn-default btn_login" type="button" value="로그인" />
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-12 index-bottom">
			<span>Copyright 2016 © spd.cu.cc All rights reserved.</span>
		</div>
	</div>
</div>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
	 aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
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
    var naver_id_login = new naver_id_login("iM6rVSYTz69Duz5F99Mp", "http://spd.cu.cc/login/naver_loginPro");
    var state = naver_id_login.getUniqState();
    naver_id_login.setButton("green", 1, 65);
    naver_id_login.setDomain("http://spd.cu.cc/");
    naver_id_login.setState(state);
    naver_id_login.setPopup();
    naver_id_login.init_naver_id_login();
</script>

<script type='text/javascript' src='/js/default.js'></script>
<script type='text/javascript' src='/js/index/index.js'></script>
<script>startApp();</script>
</body>
</html>
