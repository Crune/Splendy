<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
window.onload = function(){
	document.getElementById("faceForm").submit();
}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>facebook_Login</title>
</head>
<body>
<form id="faceForm" action="/connect/facebook" method="POST">
    <input type="hidden" name="scope" value="user_posts" />
</form>

</body>
</html>