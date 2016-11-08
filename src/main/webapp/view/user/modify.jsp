<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Modify</title>
</head>
<body>
<h2>정보수정</h2>
<form method="post" action="modify_suc" name="user">
	Email : ${user[0].email} <br/>
	<input type="hidden" name="email" value="${user[0].email}" />
	Password : <input type="password" name="password" /> <br/> 
	Nickname : <input type="text" name="nickname" value="${user[0].nickname}"/> <br/>
	<input type="submit" value="수정하기" />
</form>

</body>
</html>