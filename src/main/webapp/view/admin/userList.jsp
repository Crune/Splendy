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
<script src="/js/etc/ie-emulation-modes-warning.js"></script>
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
				<h1 class="page-header">가입자 현황</h1>
				<div class="table-responsive">
					<table class="table table-hover">
						<thead>
							<tr>
								<th>ID</th>
								<th>닉네임</th>
								<th>E-MAIL</th>
								<th>등록일</th>
								<th>수정</th>
								<th>권한</th>
								<th>프로필</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="list" items="${list}">
								<tr>
									<td>
										${list.id}
									</td>
									<td>
										${list.nickname}
									</td>
									<td>
										${list.email}
									</td>
									<td>
										${list.reg}
									</td>
									<td>
										<input id="${list.id}" class="btn btn-default btn_update" type="button" value="수정" />
									</td>
									<td>
										<input id="${list.id}" class="btn btn-default btn_authority" type="button" value="권한" />
									</td>
									<td>
										<input id="${list.id}" class="btn btn-default btn_profile" type="button" value="프로필" />
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

<div class="modal fade" id="userModal" tabindex="-1" role="dialog" aria-labelledby="userModalLabel" aria-hidden="true" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="userModalLabel">유저 정보</h4>
			</div>
			<div class="modal-body">
				<div class="index-cont">
					<form method="post" id="modifyForm" name="modifyForm">
						<div class="form-group">
							<label for="textfield"> ID :</label> <label for="textfield" id="u_id"></label> <input type="hidden" name="id" id="id" class="userID">
						</div>
						<div class="form-group">
							<label for="textfield"> 닉네임 </label> <input type="text" id="nickname" name="nickname" class="form-control">
						</div>
						<div class="form-group">
							<label for="textfield"> E-Mail :</label> <label for="textfield" id="email"></label>
						</div>
						<div class="form-group">
							<label for="exampleInputPassword1"> 암호 </label> <input name="password" type="password" id="password" placeholder="암호" class="form-control">						
						</div>
						<div class="form-group">
							<label for="textfield"> 유지 </label> <input type="text" id="enabled" name="enabled" class="form-control">
						</div>
						<div class="form-group">
							<label for="textfield"> 차단 </label> <input type="text" id="notLocked" name="notLocked" class="form-control">
						</div>
						<div class="form-group">
							<label for="textfield"> 연동 </label> <input type="text" id="notExpired" name="notExpired" class="form-control">
						</div>
						<div class="form-group">
							<label for="textfield"> 메일인증 </label> <input type="text" id="notCredential" name="notCredential" class="form-control">
						</div>
						<div class="form-group">
							<label for="textfield"> 등록일 :</label> <label for="textfield" id="reg"></label>
						</div>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary btn_modify">Save changes</button>
				<button type="button" class="btn btn-default btn_userModal_close" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="authorityModal" tabindex="-1" role="dialog" aria-labelledby="authorityModalLabel" aria-hidden="true" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="authorityModalLabel">유저 권한 변경</h4>
			</div>
			<div class="modal-body">
				<div class="index-cont">
					<form method="post" id="adminMF" name="adminMF">
						<div class="form-group">
							<label for="textfield"> ID :</label> <label for="textfield" id="a_id"></label> <input type="hidden" name="id" id="id" class="authorityID">
						</div>
						<div class="form-group">
							<label for="textfield"> 직책 </label> <input type="text" id="role" name="role" class="form-control">
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary btn_authorityModify">Save changes</button>
					<button type="button" class="btn btn-default btn_authorityModal_close" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="profileModal" tabindex="-1" role="dialog" aria-labelledby="profileModalLabel" aria-hidden="true" data-backdrop="static">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="profileModalLabel">유저 프로필 변경</h4>
			</div>
			<div class="modal-body">
				<div class="index-cont">
					<form method="post" id="profMF" name="profMF">
						<div class="form-group">
							<label for="textfield"> ID :</label> <label for="textfield" id="user_id"></label> <input type="hidden" name="userId" id="userId" class="profID">
						</div>
						<div class="form-group">
							<label for="textfield"> 최근 게임 </label> <input type="text" id="lastRId" name="lastRId" class="form-control">
						</div>
						<div class="form-group">
							<label for="textfield"> 승리 </label> <input type="text" id="win" name="win" class="form-control">
						</div>
						<div class="form-group">
							<label for="textfield"> 패배 </label> <input type="text" id="lose" name="lose" class="form-control">
						</div>
						<div class="form-group">
							<label for="textfield"> 무승부 </label> <input type="text" id="draw" name="draw" class="form-control">
						</div>
						<div class="form-group">
							<label for="textfield"> 점수 </label> <input type="text" id="rate" name="rate" class="form-control">
						</div>
						<div class="form-group">
							<label for="textfield"> 정보 </label> <textarea id="info" name="info" class="form-control"></textarea>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary btn_profileModify">Save changes</button>
					<button type="button" class="btn btn-default btn_profileModal_close" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
</div>

<script>
window.onload = function(){
	$(".btn_update").on('click', function () {
		var id = $(this).attr('id');
		userInfo(id);
	});
	$(".btn_userModal_close").on('click', function () {
		removeUserVar();
	});
	$(".btn_modify").on('click', function () {
		userModify();
		removeUserVar();
	});
	
	$(".btn_authority").on('click', function () {
		var id = $(this).attr('id');
		userAuthority(id)
	});
	$(".btn_authorityModal_close").on('click', function () {
		removeAuthorityVar();
	});
	$(".btn_authorityModify").on('click', function () {
		authorityModify();
		removeAuthorityVar();
	});
	
	$(".btn_profile").on('click', function () {
		var id = $(this).attr('id');
		userProfile(id)
	});
	$(".btn_profileModal_close").on('click', function () {
		removeProfileVar();
	});
	$(".btn_profileModify").on('click', function () {
		profileModify();
		removeProfileVar();
	});
}

function userInfo(id) {
	console.log("회원 정보 조회 : "+id);
	$.ajax({
		url:'/admin/userState/'+id+'/',
		type:'get',
		data:id,
		success:function(data){
			$('#userModal').modal('show');
			$('#u_id').append(data.id)
			$('.userID').val(data.id);
			$('#nickname').val(data.nickname);
			$('#email').append(data.email);
			$('#enabled').val(data.enabled);
			$('#notLocked').val(data.notLocked);
			$('#notExpired').val(data.notExpired);
			$('#notCredential').val(data.notCredential);
			$('#reg').append(data.reg);
		}
	})
}
function removeUserVar() {
	$('#u_id').empty();
	$('#email').empty();
	$('#reg').empty();
}
function userModify() {
	console.log("회원 정보 수정");
	$.ajax({
        url:'/admin/user_modify',
        type:'post',
        data:$("#modifyForm").serialize(),
        success:function(){
        	alert("수정 완료");
        	$('#userModal').modal('hide')
        	window.location.reload();
        },error:function(request,status,error){
			alert("실패");
		}
    })
}

function userAuthority(id) {
	console.log("회원 권한 조회 : "+id);
	$.ajax({
		url:'/admin/userAuthority/'+id+'/',
		type:'get',
		data:id,
		success:function(data){
			$('#authorityModal').modal('show');
			$('#a_id').append(data.id)
			$('.authorityID').val(data.id);
			$('#role').val(data.role);
		}
	})
}
function removeAuthorityVar() {
	$('#a_id').empty();
}
function authorityModify() {
	console.log("권한 변경");
	$.ajax({
        url:'/admin/admin_modify',
        type:'post',
        data:$("#adminMF").serialize(),
        success:function(){
        	alert("변경 완료");
        	$('#authorityModal').modal('hide')
        	window.location.reload();
        },error:function(request,status,error){
		}
    })
}

function userProfile(id) {
	console.log("회원 프로필 조회 : "+id);
	$.ajax({
		url:'/admin/userProfile/'+id+'/',
		type:'get',
		data:id,
		success:function(data){
			$('#profileModal').modal('show');
			$('#user_id').append(data.userId);
			$('.profID').val(data.userId);
			$('#win').val(data.win);
			$('#lose').val(data.lose);
			$('#draw').val(data.draw);
			$('#rate').val(data.rate);
			$('#lastRId').val(data.lastRId);
			$('#info').val(data.info);
		}
	})
}
function removeProfileVar() {
	$('#user_id').empty();
}
function profileModify() {
	console.log("회원 프로필 수정");
	$.ajax({
        url:'/admin/user_profile',
        type:'post',
        data:$("#profMF").serialize(),
        success:function(){
        	alert("수정 완료");
        	$('#profileModal').modal('hide')
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
	<script src="/js/etc/ie10-viewport-bug-workaround.js"></script>
</body>
</html>