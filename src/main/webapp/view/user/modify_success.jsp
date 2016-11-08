<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Modify_Success</title>
</head>
<body>
<h2>정보수정이 완료되었습니다.</h2>
email : ${user[0].email} <br/>
password : ${user[0].password} <br/>
nickname : ${user[0].nickname}
</body>
</html>