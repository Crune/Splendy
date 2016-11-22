<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%    
String appKey = "768530434374-au3vrrllnhr3a96h3i6utec28filmqcn.apps.googleusercontent.com";
String redirect_uri = "http://localhost/main/google";
String uri = "https://accounts.google.com/o/oauth2/auth?scope=profile&redirect_uri="+
		redirect_uri+"&response_type=code&client_id="+appKey;
%>

<html lang="en">
  <head>
    <meta name="google-signin-scope" content="profile email">
    <meta name="google-signin-client_id" content="768530434374-au3vrrllnhr3a96h3i6utec28filmqcn.apps.googleusercontent.com">
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <script>
	
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
        
        document.form.id.value = profile.getId();
        document.form.name.value = profile.getName();
        document.form.submit();
        
      };
	
      /* function attachSignin(element) {
    		console.log(element.id);
    		auth2.attachClickHandler(element, {},
    			function (googleUser) {
    				var profile = googleUser.getBasicProfile();
    				// The ID token you need to pass to your backend:
    				var id_token = googleUser.getAuthResponse().id_token;

    				//googleUser은 사용자 정보가 담겨진 객체
    				//login.aspx 에서 googleUser로 받은 사용자 정보를 조회해서 가입이 되어 있으면 로그인, 가입이 되어 있지 않으면 회원가입 페이지로 이동한다, 그리고 토큰값에 대한 검증을 한다.
    				$.post("/login.aspx", { "userid": profile.getId(), "email": profile.getEmail(), "username": profile.getName(), "fbaccesstoken": id_token },
    				function (responsephp) {
    					if (responsephp == "login") {
    						//로그인 성공시 login.aspx에서 로그인 처리를 해 준 후 URL로 이동한다.
    						location.href = 'user/hello';
    					} else if (responsephp == "signup") {
    						//회원의 정보(토큰,아이디,이름)은 쿠키나 파라미터로 넘겨서 회원가입 페이지에서 회원 정보를 이용해서 회원 등록을 진행한다.
    						location.href = '/';
    					}
    				});
    			}, function (error) {
    				alert(JSON.stringify(error, undefined, 2));
    			});
    	} */
      
    </script>
  </head>
  <body>
    <div class="g-signin2" id="google" data-onsuccess="onSignIn" ></div>
   	<form name="form" id="form" action="/user/google" method="post" >
   		<input type="hidden" name="id" value="">
   		<input type="hidden" name="name" value="">
   	</form>
  </body>
</html> --%>