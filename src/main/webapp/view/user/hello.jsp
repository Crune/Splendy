<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Hello User</title>
	</head>
	<body>
	<c:if test="${facebookProfile != null}">
		<h3>Hello, ${facebookProfile.name} User!</h3>
	</c:if>
		<h3>Hello, google User!</h3>
		<%-- <div th:each="post:${feed}">
			<b>${post.from.name}from</b> wrote:
			<p th:text="${post.message}">message text</p>
			<img th:if="${post.picture}" th:src="${post.picture}"/>
			<hr/>
		</div> --%>
	</body>
</html>