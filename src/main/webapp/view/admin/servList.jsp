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
				<a class="navbar-brand" href="/admin/index"><img src="/img/logo.png" /></a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="/admin/index">메인으로</a></li>
					<li><a href="/admin/servList">서비스</a></li>
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
				<ul class="nav nav-sidebar"> <li><a href="/admin/userList">유저</a></li> </ul>
				<ul class="nav nav-sidebar"> <li><a href="/admin/notice">공지사항</a></li> </ul>
				<ul class="nav nav-sidebar"> <li><a href="/admin/deleteForm">데이터정리</a></li> </ul>
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
										${list.value}
									</td>
									<td>
										<input id="${list.key}" class="btn btn-default btn_update" type="button" value="수정" />
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">서비스 상태 변경</h4>
			</div>
			<div class="modal-body">
				<div class="index-cont">
					<form method="post" id="servMF" name="servMF">
						<div class="form-group">
							<label for="textfield" id="s_key"> Key :  </label> <input type="hidden" name="key" id="key" class="form-contorl">
						</div>
						<div class="form-group">
							<label for="textfield"> 상태 </label> <input type="text" id="value" name="value" class="form-control">
						</div>
					</form>
				</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary btn_modify">Save changes</button>
				<button type="button" class="btn btn-default btn_modal_close" data-dismiss="modal">Close</button>
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
	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script src="/js/ie10-viewport-bug-workaround.js"></script>
<script>
window.onload = function(){
	$(".btn_update").on('click', function () {
		var key = $(this).attr('id');
		modalShow(key)
	});
	
	$(".btn_modal_close").on('click', function () {
		removeVar();
	});
	
	$(".btn_modify").on('click', function () {
		modify();
		removeVar();
	});
	
	$(".close").on('click', function () {
		removeVar();
	});
}

function modalShow(key) {
	$('#s_key').append(key)
	$('#key').val(key);
	$('#myModal').modal('show');
}

function removeVar() {
	$('#s_key').empty();
}

function modify() {
	$.ajax({
        url:'/admin/servState',
        type:'post',
        data:$("#servMF").serialize(),
        success:function(data){
        	if(data == 1) {
        		alert("실패");
        	} else if(data == 2){
        		alert("성공");
        	} else if(data == 0){
        		alert("빈값을 입력할 수 없습니다.");
        	}
        	window.location.reload();
        },error:function(request,status,error){
		}
    })
}

</script>
</body>
</html>