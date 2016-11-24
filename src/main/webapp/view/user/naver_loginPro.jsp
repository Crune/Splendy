<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="https://static.nid.naver.com/js/naverLogin_implicit-1.0.2.js" charset="utf-8"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
</head>
<body>
<script type="text/javascript">
  var naver_id_login = new naver_id_login("iM6rVSYTz69Duz5F99Mp", "http://127.0.0.1/login/naver_loginPro");
  // 접근 토큰 값 출력
  //alert(naver_id_login.oauthParams.access_token);
  // 네이버 사용자 프로필 조회
  naver_id_login.get_naver_userprofile("naverSignInCallback()");
  // 네이버 사용자 프로필 조회 이후 프로필 정보를 처리할 callback function
  function naverSignInCallback() { 
	var email = naver_id_login.getProfileData('email');
    var nickname = naver_id_login.getProfileData('nickname');
    
    $.ajax({
    	url:("/naver_loginPro?email="+email+"&nickname="+nickname),
    	method:"post",
    	success:function(data){
    		window.opener.location.replace("http://spd.cu.cc/lobby/");
    		window.close();
    	},
    	error:function(request, status, error){
			alert("로그인 실패!");
			window.close();			
		}
    });
    
    
  }
 
</script>
</body>
</html>