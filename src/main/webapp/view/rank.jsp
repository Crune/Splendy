<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>랭크</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<link rel='stylesheet' href='/css/rank.css'>
</head>
<body>
	<br />
	<table class="table table-hover table-striped">
		<tr class="info">
			<td>순위</td>
			<td>닉네임</td>
			<td>승리</td>
			<td>패배</td>
			<td>무승부</td>
			<td>레이팅</td>
		</tr>
		<c:forEach var="prof" items="${profList}" varStatus="status">
			<tr>
				<td >${status.count} 위</td>
				<td >${prof.nickname}</td>
				<td >${prof.win}</td>
				<td >${prof.lose}</td>
				<td >${prof.draw}</td>
				<td >${prof.rate}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>