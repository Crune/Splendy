<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Login_Success</title>
</head>
<body>
<c:if test="${result == 1}">
	<h2>���������� �α��εǾ����ϴ�.</h2>
	email : ${user[0].email} <br/>
	nickname : ${user[0].nickname}
</c:if>

<c:if test="${result != 1}">
	<h2>�α��ο� �����Ͽ����ϴ�.</h2>
	�̸��� Ȥ�� ��й�ȣ�� �ٽ� Ȯ�����ּ���.
</c:if>
</body>
</html>