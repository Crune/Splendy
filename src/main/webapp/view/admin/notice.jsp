<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<meta name="description" content="">
<meta name="author" content="">
<title>관리자 유저목록 페이지</title>
<!-- Bootstrap core CSS -->
<link rel='stylesheet' href='/webjars/bootstrap/3.3.4/dist/css/bootstrap.min.css' />
<!-- Custom styles for this template -->
<link href="/css/admin.css" rel="stylesheet">
<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
<!--[if lt IE 9]><script src="/js/ie8-responsive-file-warning.js"></script><![endif]-->
<script src="/js/ie-emulation-modes-warning.js"></script>
<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
<script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="/admin/index"><img src="/img/logo.png" /></a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="/admin/index">메인으로</a></li>
					<li><a href="/admin/servList">서비스</a></li>
					<li><a href="/admin/adminList">관리자</a></li>
					<li><a href="/admin/userList">유저</a></li>
					<li><a href="/admin/notice">공지사항</a></li>
					<li><a href="/admin/deleteForm">데이터정리</a></li>
					<li><a href="/lobby/">게임으로</a></li>
					<li><a href="/logout">로그아웃</a></li>
				</ul>
			</div>
		</div>
	</nav>
	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-3 col-md-2 sidebar">
				<ul class="nav nav-sidebar"> <li><a href="/admin/servList">서비스</a></li> </ul>
				<ul class="nav nav-sidebar"> <li><a href="/admin/adminList">관리자</a></li> </ul>
				<ul class="nav nav-sidebar"> <li><a href="/admin/userList">유저</a></li> </ul>
				<ul class="nav nav-sidebar"> <li><a href="/admin/notice">공지사항</a></li> </ul>
				<ul class="nav nav-sidebar"> <li><a href="/admin/deleteForm">데이터정리</a></li> </ul>
			</div>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h1 class="page-header">공지사항</h1>
				<div class="table-responsive">
					<form id="noticeForm" name="noticeForm" method="post">
						<table class="table table-hover" width="800">
							<tr>
								<td> 작성자 </td>
								<td> <input type="hidden" id="nickname" name="nickname" value="${user.nickname}">${user.nickname} </td>
							</tr>
							<tr>
								<td> 내용 </td>
								<td> <textarea rows="20" cols="100" id="content" name="content" ></textarea> </td>
							</tr>
							<tr>
								<td colspan="2"> <input type="submit" id="btn_send" name="btn_send" class="btn btn-default btn_send" value="전송" /> </td>
							</tr>
						</table>
					</form>
				</div>
			</div>
		</div>
	</div>
<script>
window.onload = function(){
	$(".btn_send").on('click', function () {
		sendNotice();
	});
}

function sendNotice() {
	console.log("send notice start");
jQuery.ajaxSettings.traditional = true;
	$.ajax({
        url:'/admin/notice_send',
        type:'post',
        data:$("#noticeForm").serialize(),
        success:function(data){
        	console.log(data);
        	console.log("send notice end");
        	window.location.reload();
        },error:function(request,status,error){

		}
    })
}
</script>
	<!-- Bootstrap core JavaScript
	================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="/webjars/jquery/2.1.3/dist/jquery.min.js"></script>
	<script src="/webjars/bootstrap/3.3.4/dist/js/bootstrap.js"></script>
	<!-- Just to make our placeholder images work. Don't actually copy the next line! -->
	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script src="/js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>