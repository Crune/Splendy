<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>jmnaverloginPop.html</title>
<meta name="viewport" content="width=device-width,initial-scale=1">
<META http-equiv="Pragma" content="no-cache">
<!-- jquery mobile -->
<link rel="stylesheet"
 href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css" />
<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
<script type="text/javascript"
 src="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
<!-- 사용자정의 css / script -->
<script src="/js/naverLogin_implicit-1.0.2.js"></script>
</head>
<body>
 <div data-role="page" id="pOne">
  <div data-role="header" id="pHead">
   <h1>WELCOME</h1>
  </div>
  <div data-role="main" class="ui-content">
   <p>WELCOME MAIN - NAVER LOGIN</p>
   <!-- 네이버아이디로로그인 버튼 노출 영역 -->
   <div id="naver_id_login" style="display: none;"></div>
   <!-- //네이버아이디로로그인 버튼 노출 영역 -->
   <!-- 네이버아디디로로그인 초기화 Script -->
   <script type="text/javascript">
    var naver_id_login = new naver_id_login("iM6rVSYTz69Duz5F99Mp",
      "http://127.0.0.1/login/naver_loginPro");
    var state = naver_id_login.getUniqState();
    naver_id_login.setButton("green", 2, 40);
    naver_id_login.setDomain("http://127.0.0.1/");
    naver_id_login.setState(state);
    naver_id_login.setPopup();
    naver_id_login.init_naver_id_login();
   </script>
   <!-- // 네이버아이디로로그인 초기화 Script -->
   <!-- 네이버아디디로로그인 Callback페이지 처리 Script -->
   <script type="text/javascript">
    // 네이버 사용자 프로필 조회 이후 프로필 정보를 처리할 callback function
    function naverSignInCallback() {
     // naver_id_login.getProfileData('프로필항목명');
     // 프로필 항목은 개발가이드를 참고하시기 바랍니다.
     // alert(naver_id_login.getProfileData('email'));
     // alert(naver_id_login.getProfileData('nickname'));
     
     // 로그인 처리
     $(opener.document).find("#naver_id_login").hide();
     $(opener.document).find("#naver_id_logout").show();
     
     // 창닫기 및 로그인입력
     $(opener.document).find("#dvLogin").empty();
     
     $(opener.document).find("#dvLogin").append(naver_id_login.getProfileData('birthday') + "님 로그인("+ naver_id_login.getProfileData('nickname') +","+ naver_id_login.getAccessToken() +")");
	  console.log(naver_id_login);
     
    }
    // 네이버 사용자 프로필 조회
    naver_id_login.get_naver_userprofile("naverSignInCallback()");
   </script>
   <!-- //네이버아디디로로그인 Callback페이지 처리 Script -->
   <div>
   
   </div>
  </div>
  <div data-role="footer" id="pFooter">
   <h5>COPYRIGHT, &copy; ALL RIGHTS RESERVED</h5>
  </div>
 </div>
</body>
</html>