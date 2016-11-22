<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>jmnaverlogin.jsp</title>
<meta name="viewport" content="width=device-width,initial-scale=1">
<!-- jquery mobile -->
<link rel="stylesheet"
	href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css" />
<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
<script type="text/javascript"
	src="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
<!-- 사용자정의 css / script -->
<script src="/js/naverLogin_implicit-1.0.2.js"></script>
<script>
	function logout() {
		// 로그아웃 아이프레임
		$("body").append(
				"<iframe id='logoutIframe' style='display: none;'></iframe>");
		$("#logoutIframe").attr("src", "http://nid.naver.com/nidlogin.logout");
		// 로그아웃 처리
		$("#naver_id_login").show();
		$("#naver_id_logout").hide();
	}
</script>
</head>
<body>
	<div data-role="page" id="pOne">
		<div data-role="header" id="pHead">
			<h1>WELCOME</h1>
		</div>
		<div data-role="main" class="ui-content">
			<p>WELCOME MAIN - NAVER LOGIN</p>
			<!-- 네이버아이디로 로그인 버튼 노출 영역 -->
			<div id="naver_id_login"></div>
			
			<!-- //네이버아이디로 로그인 버튼 노출 영역 -->
			<!-- 로그인 한 경우 -->
			<div id="naver_id_logout" style="display: none;">
				<a href="#" onclick="logout();">로그아웃</a>
				<!-- 로그인 사용자 정보출력 -->
				<div id="dvLogin"></div>
				<!-- 로그인 사용자 정보출력 끝 -->
			</div>
			<!-- 로그인 한 경우 -->
			<!-- 네이버아디디로로그인 초기화 Script -->
			<script type="text/javascript">
				var naver_id_login = new naver_id_login("iM6rVSYTz69Duz5F99Mp", "http://127.0.0.1/login/naver_loginPro");
				var state = naver_id_login.getUniqState();
				naver_id_login.setButton("white", 2, 40);
				naver_id_login.setDomain("http://127.0.0.1/");
				naver_id_login.setState(state);
				naver_id_login.setPopup();
				naver_id_login.init_naver_id_login();				
			</script>
			<!-- // 네이버아이디로로그인 초기화 Script -->
		
		</div>
		<div data-role="footer" id="pFooter">
			<h5>COPYRIGHT, &copy; ALL RIGHTS RESERVED</h5>
		</div>
	</div>
</body>
</html>
