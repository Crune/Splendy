<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User_Remove</title>
</head>
<body>
<h2>회원탈퇴</h2>
${user[0].nickname}(${user[0].email})님 탈퇴하시겠습니까?
<form method="post" action="delete_suc" >
password : <input type="password" name="password" /><br/>
<input type="hidden" name="email" value="${user[0].email}" />
<input type="submit" value="탈퇴하기" />
<input type="button" value="메인으로" onclick="location.href='http://localhost/main'" />
</form>
</body>
</html>