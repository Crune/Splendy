<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Hello Facebook</title>
	</head>
	<body>
	<h3>Hello, ${facebookProfile.name} User</span>!</h3>

		<%-- <div th:each="post:${feed}">
			<b>${post.from.name}from</b> wrote:
			<p th:text="${post.message}">message text</p>
			<img th:if="${post.picture}" th:src="${post.picture}"/>
			<hr/>
		</div> --%>
	</body>
</html>