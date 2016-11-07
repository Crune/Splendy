<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Modify</title>
</head>
<body>
<h2>정보수정</h2>
<form method="post" action="modified" name="user">
	Email : ${email} <br/>
	Password : <input type="password" name="password" /> <br/> 
	Nickname :  <input type="text" name="nickname" /> <br/>
	<input type="submit" value="수정하기" />
</form>

</body>
</html>