<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Splendy - 환영합니다!</title>
<link rel='stylesheet' href='/css/default.css'>
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

<script id="temp_test" type="text/x-handlebars-template">

	<a>{{ test }}</a>

</script>
<script>

var curState = "join";
var email = "${user[0].email}";

$(document).ready(function(){
	var source_test = $("#temp_test").html();
	var temp_test = Handlebars.compile(source_test);	
	var data = {temp:""};
	
	$("#testDiv").html(temp_test(data));
	
	var result = "${result}";
	if(result == "2") {
		alert("email이 중복되었습니다.")
	} else if(result == "1") {
		alert("회원가입이 완료되었습니다.")
	}
	
	if(email == "null" || email == "") {
		$("#btn_login").on('click', function () {
			curState = "login";
			$("#joinDiv").hide();
			$("#login_sucDiv").hide();
			$("#loginDiv").show();
		});
		
		$("#btn_join").on('click', function () {
			curState = "join";
			$("#loginDiv").hide();
			$("#login_sucDiv").hide();
			$("#joinDiv").show();
		});
	}else {
			curState = "login_suc";
			$("#loginDiv").hide();
			$("#login_sucDiv").show();
			$("#joinDiv").hide();
	}
	});

function joinRequest() {
	$.ajax({
        url:'/user/requestJoin',
        type:'post',
        data:$('#joinForm').serialize(),
        success:function(data){
        }
    })
}

function join_check() {
		if(document.joinForm.email.value == ""){
			alert("email을 입력해주세요.")
			document.joinForm.email.focus();
			return false;
		} else if(document.joinForm.nickname.value == ""){
			alert("nickname을 입력해주세요.")
			document.joinForm.nickname.focus();
			return false;
		} else if(document.joinForm.password.value == ""){
			alert("password을 입력해주세요.")
			document.joinForm.password.focus();
			return false;
		}  else {
			joinRequest();
			alert("회원가입을 완료하였습니다.")
			return true;
		}
}

function login_check_credent() {
	$.ajax({
        url:'/user/login_credent',
        type:'post',
        data:$('#loginForm').serialize(),
        success:function(data){
        	if(data == 1) {
        		alert("email을 인증하세요.");
        	}
        }
    })
}
var login_result = "${login_result}";
function login_check() {
	
	if(document.loginForm.email.value == ""){
		alert("email을 입력해주세요.")
		document.loginForm.email.focus();
		return false;
	} else if(document.loginForm.password.value == ""){
		alert("password을 입력해주세요.")
		document.loginForm.password.focus();
		return false;
	} else if(login_result == 0){ /* 야 여기 다시해 */
		alert("email과 password를 다시 확인해주세요.");
		return false;
	} else {
		return true;
	}
}


</script>
<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body bgcolor="#191919">
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
								<form id="joinForm" name="joinForm" method="post" 
									onsubmit="return join_check();">
									<div class="form-group">
										<label for="textfield">이메일 주소</label> <input name="email"
											type="email" class="form-control" id="email">
									</div>
									<div class="form-group">
										<label for="textfield">닉네임</label> <input name="nickname"

											type="text" class="form-control" id="nickname">
									</div>
									<div class="form-group">
										<label for="exampleInputPassword1">암호</label> <input name="password"
											type="password" class="form-control"
											id="password" placeholder="암호">
									</div>
									<button type="submit" class="btn btn-default">회원가입</button>
									<input id="btn_login" class="btn btn-default" type="button" value="로그인" />
								</form>
							</div>	
						</div>
						<div id="testDiv" style="display: none"></div>
						<div class="index-right-frame" id="loginDiv">
							<p class="index-title">로그인</p>
							<img src="/img/work/index-hr.png" width="310" height="5" alt="" />
							<div class="index-cont">
								<form id="loginForm" name="loginForm" method="post" action="/user/login_suc"
									onsubmit="return login_check();">
									<div class="form-group">
										<label for="textfield">이메일 주소</label> <input name="email"
											type="email" class="form-control" id="email" />
									</div>
									<div class="form-group">
										<label for="exampleInputPassword1">암호</label> <input name="password"
											type="password" class="form-control"
											id="exampleInputPassword1" name="password" placeholder="암호" />

									</div>
									<button type="submit" class="btn btn-default">로그인</button>
									<input id="btn_join" class="btn btn-default" type="button" value="회원가입" />
									<input id="btn_join" class="btn btn-default" type="button" 
									onclick = "" value="비밀번호 찾기" />
								</form>
							</div>
						</div>
						<div class="index-right-frame" id="login_sucDiv" style="display: none">
							<p class="index-title">로그인성공</p>
							<img src="/img/work/index-hr.png" width="310" height="5" alt="" />
							<div class="index-cont">
									<div class="form-group">
										<label for="textfield"><b>${user[0].nickname}</b>님 환영합니다.</label>
									</div>
									<input id="btn_logout" class="btn btn-default" type="button" 
									onclick="location.href='http://localhost/user/logout'" value="로그아웃" />
									<input id="btn_logout" class="btn btn-default" type="button" 
									onclick="location.href='http://localhost/user/delete_suc'" value="회원탈퇴" />
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
</body>
</html>
