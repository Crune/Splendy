<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
window.onload = function(){
	document.getElementById("faceForm").submit();
}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>facebook_Connected</title>
</head>
<body>
<form id="faceForm" action="/user/facebook" method="POST">
    <input type="hidden" name="scope" value="user_posts" />
</form>
</body>
</html>