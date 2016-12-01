
//page 실행(ready도 실행) -> script css link 실행 -> onload(모든 파일이 준비되었을 때)
//$(#name)를 쓸 땐 html안에서 한번 정의된 것만 사용하기 때문에 여러번 정의되면 가장 처음 등장한 것만 실행됨. 따라서 여러번 쓸 때에는 class를 사용해야한다.

/*var curState = "join";
var email = "${user.email}";
var login_result = "${login_result}";
var credent = "${credent}";*/

$(document).ready(function() {
	
	if(msg !== "") {
		$('#myModal').modal('show');
	}
	
	window.fbAsyncInit = function(){
	    FB.init({ appId: '1687598368235887', 
	        status: true, 
	        cookie: true,
	        xfbml: true,
	        oauth: true});
	}
	
	function updateButton(response) {
	    var button = document.getElementById('btn_facebook');
	     
	    if (response.authResponse) {
	      FB.api('/me', function(response) {
	          $.ajax({
                  url:'/user/facebook',
                  type:'post',
                  data:{email: response.id, nickname: response.name},
                  dataType: 'text', 
                  success:function(data){
                  	document.location.href=data;
                  },error:function(request,status,error){
          			alert("로그인에 실패했습니다. 다시 시도해주세요.");
          		}
              });
	      });
	    } else {
	         FB.login(function(response) {
	            if (response.authResponse) {
	                FB.api('/me', function(response) {
	                	$.ajax({
	                        url:'/user/facebook',
	                        type:'post',
	                        data:{email: response.id, nickname: response.name},
	                        dataType: 'text', 
	                        success:function(data){
	                        	document.location.href=data;
	                        },error:function(request,status,error){
	                			alert("로그인에 실패했습니다. 다시 시도해주세요.");
	                		}
	                    })
	                });    
	            } else {
	            	alert("로그인을 다시 시도해주세요.");
	            }
	        }, {scope:'email'});    
	    }
	}
	
	document.getElementById('btn_facebook').onclick = function() {
	    FB.Event.subscribe('auth.statusChange', updateButton);  
	    FB.getLoginStatus(updateButton);
	};
	
	(function() {
		  var e = document.createElement('script'); e.async = true;
		  e.src = document.location.protocol 
		    + '//connect.facebook.net/ko_KR/all.js';
		  document.getElementById('fb-root').appendChild(e);
	}());
});


window.onload = function(){
	
		$(".btn_login").on('click', function () {
			$("#joinDiv").hide();
			$("#sendPwDiv").hide();
			$("#loginDiv").show();
		});
		
		$(".btn_join").on('click', function () {
			curState = "join";
			$("#loginDiv").hide();
			$("#sendPwDiv").hide();
			$("#joinDiv").show();
		});
		
		$(".btn_send_pw").on('click', function () {
			curState = "send_pw";
			$("#loginDiv").hide();
			$("#joinDiv").hide();
			$("#sendPwDiv").show();
		});
		
		$(".btn_join_prc").on('click', function () {
			joinRequest();
		});
		
		$(".btn_send_prc").on('click', function () {
			sendRequest();
		});
		
		$(".btn_modify_prc").on('click', function () {
			modifyRequest();
		});
		
		$(".btn_login_prc").on('click', function () {
			login_check();
		});
		
		$(".btn_naver").on('click', function(){
		});
		
		$("#btn_kakao").on('click', function(){
			kakaoLogin();
		});

		$("input#exampleInputPassword1").keypress(function(e) {
            if (e.which == 13) {
                login_check();
            }
        });
		self.opener = self;
	
		Kakao.init('434e775be31c907b0fcc3bc817220392');
}

function kakaoLogin(){	
		Kakao.Auth.login({
		       success: function(authObj) {
		    	   Kakao.API.request({
		    	          url: '/v1/user/me',
		    	          success: function(res) {
		    	            $.ajax({
		    	                url:'/user/kakao',
		    	                type:'post',
		    	                data:{email: res.id, nickname: res.properties.nickname},
		    	                dataType: 'text', 
		    	                success:function(data){
		    	                	document.location.href=data;
		    	                },error:function(request,status,error){
		    	        			alert("로그인에 실패했습니다. 다시 시도해주세요.");
		    	        		}
		    	            })
		    	          },
		    	          fail: function(error) {
		    	            alert(JSON.stringify(error));
		    	          }
		    	   })
		       },
		       fail: function(err) {
		         alert(JSON.stringify(err));
		       }
	    });
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
			console.log("login result: "+text);
			if(text == "true"){
				window.location.href = "../../";
			} else if (text == "credentFalse"){
				alert("email을 인증해주세요.");
				windwo.location.href = "/";
			} else if (text == "loginFalse") {
				alert("email과 password를 다시 확인해주세요.");
				window.location.href = "/";
			} else if (text == "pwFalse") {
				alert("password를 다시 확인해주세요.");
				window.location.href = "/";
			} else if (text == "idFalse") {
				alert("id를 다시 확인해주세요.");
				window.location.href = "/logout"; // "/logout"페이지는 security에서 기본제공
			} else if (text == "lockAccount") {
				alert("차단된 아이디 입니다.");
				window.location.href = "/logout";
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
        loginRequest();
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

var googleUser = {};
var startApp = function() {
  gapi.load('auth2', function(){
    // Retrieve the singleton for the GoogleAuth library and set up the client.
    auth2 = gapi.auth2.init({
      client_id: '768530434374-au3vrrllnhr3a96h3i6utec28filmqcn.apps.googleusercontent.com',
      cookiepolicy: 'VR7RGj1x7ET3dG8eMUlMn_jj',
      // Request scopes in addition to 'profile' and 'email'
      //scope: 'additional_scope'
    });
    attachSignin(document.getElementById('btn_google'));
  });
};

function attachSignin(element) {
  auth2.attachClickHandler(element, {},
      function(googleUser) {
	  document.googleForm.email.value = googleUser.getBasicProfile().getId(); //구글의 id:디비의 email
	  document.googleForm.nickname.value = googleUser.getBasicProfile().getName();
	  google();
      }, function(error) {
        alert(JSON.stringify(error, undefined, 2));
      });
}

function google() {
	$.ajax({
        url:'/user/google',
        type:'post',
        data:$("#googleForm").serialize(),
        dataType: 'text', 
        success:function(data){
        	document.location.href="http://spd.cu.cc/lobby";
        },error:function(request,status,error){
			alert("로그인에 실패했습니다. 다시 시도해주세요.");
		}
    })
}


