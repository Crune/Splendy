<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<script>
	window.onload = function(){
		window.close();
	}
	</script>
	
		<title>Hello User</title>
	</head>
	<body>
	<c:if test="${facebookProfile != null}">
		<h3>Hello, ${facebookProfile.name} User!</h3><br/>
		id : ${facebookProfile.id} <br/>
	</c:if>
	</body>
</html>