<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%    
String appKey = "768530434374-au3vrrllnhr3a96h3i6utec28filmqcn.apps.googleusercontent.com";
String redirect_uri = "http://localhost/main/google";
String uri = "https://accounts.google.com/o/oauth2/auth?scope=profile&redirect_uri="+
		redirect_uri+"&response_type=code&client_id="+appKey;
%>
<script type = "text/javascript">
function process()
{
 document.google_main.submit();
} 
</script>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>google_login</title>
</head>
<body onLoad = "javascript:process()">

<form method = "post" name = "google_main" action = '<%=uri%>'>
	<!-- <input type="hidden" name="name" value="profile.name"/> -->
</form>
</body>
