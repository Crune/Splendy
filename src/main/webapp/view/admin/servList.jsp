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
<title>관리자 서비스 리스트 페이지</title>
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
				<a class="navbar-brand" href="/admin"><img src="/img/logo.png" /></a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="/admin">메인으로</a></li>
					<li><a href="/servList">서비스</a></li>
					<li><a href="adminList">관리자</a></li>
					<li><a href="/userList">유저</a></li>
				</ul>
			</div>
		</div>
	</nav>
	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-3 col-md-2 sidebar">
				<ul class="nav nav-sidebar">
					<li><a href="/servList">서비스</a></li>
				</ul>
				<ul class="nav nav-sidebar">
					<li><a href="/adminList">관리자</a></li>
				</ul>
				<ul class="nav nav-sidebar">
					<li><a href="/userList">유저</a></li>
				</ul>
			</div>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h1 class="page-header">Service 현황</h1>
				<div class="table-responsive">
					<table class="table table-hover">
						<thead>
							<tr>
								<th>이름</th>
								<th>상태</th>
								<th>관리</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="list" items="${list}">
								<tr>
									<td>
										${list.key}
									</td>
									<td>
										<div id="text_value_${list.key}" class="text_value" style="display: none">
											${list.value}
										</div>
										<div id="input_value_${list.key}" class="input_value" style="display: none">
											<form method="post" id="${list.key}" name="${list.key}">
												<input name="value" type="text" class="form-control" value="${list.value}" />
												<input name="key" type="hidden" class="form-control" value="${list.key}" />
												<input id="btn_updatePro_${list.key}" class="btn btn-default btn_updatePro" type="submit" value="저장" />
											</form>
										</div>
									</td>
									<td>
										<input id="btn_update_${list.key}" class="btn btn-default btn_update" type="button" value="수정" />
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- Bootstrap core JavaScript
	================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="/webjars/jquery/2.1.3/dist/jquery.min.js"></script>
	<script src="/webjars/bootstrap/3.3.4/dist/js/bootstrap.js"></script>
	<!-- Just to make our placeholder images work. Don't actually copy the next line! -->
	<script src="/js/vendor/holder.js"></script>
	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script src="/js/ie10-viewport-bug-workaround.js"></script>
<script>
window.onload = function(){
	$(".text_value").show();
	$(".input_value").hide();
	
	$(".btn_update").on('click', function () {
		var id = $(this).attr('id');
		var result = id.split("_");
		$("#text_value_"+result[2]).hide();
		$("#input_value_"+result[2]).show();
	});

	$(".btn_updatePro").on('click', function () {
		var id = $(this).attr('id');
		var result = id.split("_");
		$("#text_value_"+result[2]).show();
		$("#input_value_"+result[2]).hide();
		sendRequest(result[2]);
	});
	
}

function sendRequest(result) {
	$.ajax({
        url:'/admin/servState',
        type:'post',
        data:$("#"+result).serialize(),
        success:function(){
        	window.location.reload();
        }
    })
}

</script>
</body>
</html>