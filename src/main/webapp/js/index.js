
//page 실행(ready도 실행) -> script css link 실행 -> onload(모든 파일이 준비되었을 때)
//$(#name)를 쓸 땐 html안에서 한번 정의된 것만 사용하기 때문에 여러번 정의되면 가장 처음 등장한 것만 실행됨. 따라서 여러번 쓸 때에는 class를 사용해야한다.

/*var curState = "join";
var email = "${user.email}";
var login_result = "${login_result}";
var credent = "${credent}";
var msg = "${msg}";*/

window.onload = function(){
	
	var source_test = $("#temp_test").html();
	var temp_test = Handlebars.compile(source_test);	
	var data = {temp:""};
	
	/*if(msg !== "") {
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
	*/

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
		
		$(".btn_login_prc").on('click', function () {
			loginRequest();
		})
		
		$(".btn_facebook").on('click', function () {
			loginFacebook();
		})
		
		$(".btn_google").on('click', function () {
			loginGoogle();
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

function loginRequest() {
	console.log("로그인이 시도되었습니다.");
	$.ajax({
		url:'/user/login_suc',
		type:'post',
		data:$("#loginForm").serialize(),
		dataType: 'text', //!!!
		success:function(text){
			if(text == "true"){
				window.location.href = "lobby/";
			} else if (text == "credentFalse"){
				alert("email을 인증해주세요.");
				windwo.location.href = "/";
			} else if (text == "loginFalse") {
			alert("email과 password를 다시 확인해주세요.");
			window.location.href = "/";
			}
		},error:function(request,status,error){
			 alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	})
}

function modifyRequest() {
	console.log("정보가 수정되었습니다.");
	$.ajax({
        url:'/user/modify_suc',
        type:'post',
        data:$("#modifyForm").serialize(),
        dataType: 'text', 
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

function loginFacebook() {
	window.open("/user/facebook", "Facebook Login", "left=300, top=100 width=600, height=350, toolbar=no, menubar=no, scrollbars=yes, resizable=yes" );
}

function onSignIn(googleUser) {
    // Useful data for your client-side scripts:
    var profile = googleUser.getBasicProfile();
    console.log("ID: " + profile.getId()); // Don't send this directly to your server!
    console.log('Full Name: ' + profile.getName());
    console.log('Given Name: ' + profile.getGivenName());
    console.log('Family Name: ' + profile.getFamilyName());
    console.log("Image URL: " + profile.getImageUrl());
    console.log("Email: " + profile.getEmail());

    // The ID token you need to pass to your backend:
    var id_token = googleUser.getAuthResponse().id_token;
    console.log("ID Token: " + id_token);
    
    document.googleForm.email.value = profile.getId(); //구글의 id:디비의 email
    document.googleForm.nickname.value = profile.getName();
   
    google();
}

function google() {
	$.ajax({
        url:'/user/google',
        type:'post',
        data:$("#googleForm").serialize(),
        dataType: 'text', 
        success:function(data){
        	alert(data);
        		document.location.href="lobby/";
        },error:function(request,status,error){
			alert("로그인에 실패했습니다. 다시 시도해주세요.");
		}
    })
}