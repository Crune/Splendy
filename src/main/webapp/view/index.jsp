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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="/webjars/bootstrap/3.3.4/dist/js/bootstrap.js"></script>
<script src="/webjars/handlebars/4.0.5/handlebars.js"></script>
<script src='/js/default.js'></script>

<script id="temp_test" type="text/x-handlebars-template">

	<a>{{ test }}</a>

</script>
<script>
//page 실행(ready도 실행) -> script css link 실행 -> onload(모든 파일이 준비되었을 때)
//$(#name)를 쓸 땐 html안에서 한번 정의된 것만 사용하기 때문에 여러번 정의되면 가장 처음 등장한 것만 실행됨. 따라서 여러번 쓸 때에는 class를 사용해야한다.
var curState = "join";
var email = "${user.email}";
var login_result = "${login_result}";
var credent = "${credent}";
var msg = "${msg}";

window.onload = function(){
	
	var source_test = $("#temp_test").html();
	var temp_test = Handlebars.compile(source_test);	
	var data = {temp:""};
	
	if(msg !== "") {
		$('#myModal').modal('show');
	}
	
	$("#testDiv").html(temp_test(data));
	
	var result = "${result}";
	if(result == "2") {
		alert("email이 중복되었습니다.")
	} else if(result == "1") {
		alert("회원가입이 완료되었습니다.")
	}
	
	if (login_result === "0") {
		if (credent === "0"){ 
			console.log("비밀번호틀림");
			alert("email과 password를 다시 확인해주세요.");
			return false;
		} else if(credent === "1"){
			console.log("이메일인증");
			alert("email인증을 진행해주세요.");
			return false;
		}
	} else if (login_result === "1"){
		curState = "login_suc";
		$("#loginDiv").hide();
		$("#joinDiv").hide();
		$("#sendPwDiv").hide();
		$("#modifyDiv").hide();
		$("#login_sucDiv").show();
	}
	
	login_result = "";
	credent = "";
	

		$(".btn_login").on('click', function () {
			curState = "login";
			$("#joinDiv").hide();
			$("#login_sucDiv").hide();
			$("#sendPwDiv").hide();
			$("#modifyDiv").hide();
			$("#loginDiv").show();
		});
		
		$(".btn_join").on('click', function () {
			curState = "join";
			$("#loginDiv").hide();
			$("#login_sucDiv").hide();
			$("#sendPwDiv").hide();
			$("#modifyDiv").hide();
			$("#joinDiv").show();
		});
		
		$(".btn_send_pw").on('click', function () {
			curState = "send_pw";
			$("#loginDiv").hide();
			$("#login_sucDiv").hide();
			$("#joinDiv").hide();
			$("#modifyDiv").hide();
			$("#sendPwDiv").show();
		});
		
		$(".btn_modify").on('click', function () {
			curState = "modify";
			$("#loginDiv").hide();
			$("#login_sucDiv").hide();
			$("#joinDiv").hide();
			$("#sendPwDiv").hide();
			$("#modifyDiv").show();
		});
		
		$(".btn_join_prc").on('click', function () {
			joinRequest();
		});
		
		$(".btn_send_prc").on('click', function () {
			sendRequest();
		});
		
		$(".btn_modify_prc").on('click', function () {
			modifyRequest();
		})
	
}

function joinRequest() {
	login_result = "-1";
	credent = "-1";
	$.ajax({
        url:'/user/requestJoin',
        type:'post',
        data:$("#joinForm").serialize(),
        success:function(data){
        	console.log(data);
        	if(data == 2) {
        		alert("email이 중복되었습니다.");
        	} else if(data == 1) {
        		alert("회원가입이 완료되었습니다.");
        		alert("email인증을 진행해주세요.");
        		curState = "login";
    			$("#joinDiv").hide();
    			$("#login_sucDiv").hide();
    			$("#sendPwDiv").hide();
    			$("#modifyDiv").hide();
    			$("#loginDiv").show();
        	}	
        },error:function(request,status,error){
			alert("회원가입이 실패했습니다.");
		}
    }) 
}

function sendRequest() {
	console.log("email로 임시 비밀번호를 보냈습니다.");
	$.ajax({
        url:'/user/send_pw',
        type:'post',
        data:$("#sendPwForm").serialize(),
        success:function(data){
        	console.log(data);
        	if(data == 1) {
        		alert("email로 임시 비밀번호를 보냈습니다.");
        	} else if(data == -1) {
        		alert("가입된 email이 없습니다. 다시 확인해주세요.");
        	}
        },error:function(request,status,error){
			alert("");
		}
    })
}

function modifyRequest() {
	console.log("정보가 수정되었습니다.");
	$.ajax({
        url:'/user/modify_suc',
        type:'post',
        data:$("#modifyForm").serialize(),
        success:function(data){
        	console.log(data);
        		alert("정보를 수정하였습니다.");
        		document.location.reload();
        		$("#loginDiv").hide();
        		$("#joinDiv").hide();
        		$("#sendPwDiv").hide();
        		$("#modifyDiv").hide();
        		$("#login_sucDiv").show();
        },error:function(request,status,error){
			alert("정보 수정에 실패하였습니다. 다시 시도해주세요.");
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
		} else {
			joinRequest();
			return true;
		}
}

function login_check() {
	if(document.loginForm.email.value === ""){
		alert("email을 입력해주세요.")
		document.loginForm.email.focus();
		return false;
	} else if(document.loginForm.password.value === ""){
		alert("password을 입력해주세요.")
		document.loginForm.password.focus();
		return false;
	} else {
		return true;
	}
}

function delete_check() {
	if(confirm("정말 탈퇴하시겠습니까?")){
		document.location.href='/user/delete_suc';
	} else {
		return false;
	}
}

/**
 * TODO 민정: modal
 */
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
										<label for="exampleInputPassword1">암호</label> <input name="password"
											type="password" class="form-control"
											id="password" placeholder="암호">
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
									<input id="btn_send_pw" class="btn btn-default btn_send_pw" type="button" value="비밀번호 찾기" />
									<input id="btn_join" class="btn btn-default btn_join" type="button" value="회원가입" />
								</form>
							</div>
						</div>
						<div class="index-right-frame" id="login_sucDiv" style="display: none">
							<p class="index-title">로그인성공</p>
							<img src="/img/work/index-hr.png" width="310" height="5" alt="" />
							<div class="index-cont">
									<div class="form-group">
										<label for="textfield"><b>${user.nickname}</b>님 환영합니다.</label>
									</div>
									<input id="btn_logout" class="btn btn-default btn_logout" type="button" 
									onclick="location.href='/user/logout'" value="로그아웃" />
									<input id="btn_modify" class="btn btn-default btn_modify" type="button" value="정보수정" />
									<input id="btn_delete" class="btn btn-default btn_delete" type="button" 
									onclick="return delete_check()" value="회원탈퇴" />
							</div>
						</div>
						<div class="index-right-frame" id="sendPwDiv" style="display: none">
							<p class="index-title">비밀번호 찾기</p>
							<img src="/img/work/index-hr.png" width="310" height="5" alt="" />
							<div class="index-cont">
								<form id="sendPwForm" name="sendPwForm" method="post">
									<div class="form-group">
										<label for="textfield">이메일 주소</label> <input name="email"
											type="email" class="form-control" id="email">
									</div>
									<input id="btn_send_prc" class="btn btn-default btn_send_prc" type="button" value="비밀번호 찾기" />
									<input id="btn_join" class="btn btn-default btn_join" type="button" value="회원가입" />
									<input id="btn_login" class="btn btn-default btn_login" type="button" value="로그인" />
								</form>
							</div>	
						</div>
						<div class="index-right-frame" id="modifyDiv" style="display: none">
							<p class="index-title">개인정보 수정</p>
							<img src="/img/work/index-hr.png" width="310" height="5" alt="" />
							<div class="index-cont">
								<form id="modifyForm" name="modifyForm" method="post">
									<div class="form-group">
										<label for="textfield">email : </label> ${user.email} <br/>
											<input name="email" type="hidden" class="form-control" id="email" value="${user.email}">
										<label for="textfield">nickname : </label> <input name="nickname"
											type="text" class="form-control" id="nickname" value="${user.nickname}">
										<label for="textfield">password : </label> <input name="password"
											type="password" class="form-control" id="password">
									</div>
									<input id="btn_modify_prc" class="btn btn-default btn_modify_prc" type="button" value="정보수정" />
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

<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">${msg}</h4>
      </div>
      <div class="modal-body">
        	로그인해주세요.
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <!-- <button type="button" class="btn btn-primary">Save changes</button> -->
      </div>
    </div>
  </div>
</div>

</body>
</html>
