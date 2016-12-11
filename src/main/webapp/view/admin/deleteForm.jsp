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
				<h1 class="page-header">잔여 데이터 관리</h1>
				<button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#msgModal"> Message 정리 </button>
				<button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#roomModal"> Room 정리 </button>
				<button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#connModal"> Player 정리 </button>
			</div>
		</div>
	</div>

<div class="modal fade" id="msgModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="msgModalLabel">채팅 로그</h4>
			</div>
			<div class="modal-body">
					<form id="msgForm" name="msgForm" method="post" >
						<table class="table table-hover">
							<tr>
								<td> <input type="checkbox" name="msg-all" id="msg-all" /> </td>
								<td> M_ID </td>
								<td> R_ID </td>
								<td> content </td>
							</tr>
							<c:forEach var="msg" items="${msg}">
								<tr>
									<td> <input type="checkbox" name="mid" id="mid" class="msg-box" value="${msg.mid}" /> </td>
									<td> ${msg.mid} </td>
									<td> ${msg.rid} </td>
									<td> ${msg.cont} </td>
								</tr>
							</c:forEach>
						</table>
					</form>
			</div>
			<div class="modal-footer">
				<input type="submit" id="btn_delete" name="btn_delete" class="btn btn-default btn_delete_msg" value="삭제" />
				<button type="button" class="btn btn-default btn_modal_close" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="roomModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="roomModal">룸 로그</h4>
			</div>
			<div class="modal-body">
				<form id="roomForm" name="roomForm" method="post" >
					<table class="table table-hover">
						<tr>
							<td> <input type="checkbox" name="room-all" id="room-all" /> </td>
							<td> 방번호 </td>
							<td> 방제목 </td>
							<td> 방장 </td>
							<td> 정보 </td>
							<td> 승자 </td>
							<td> 시작여부 </td>
							<td> 종료여부 </td>
						</tr>
						<c:forEach var="room" items="${room}">
							<tr>
								<td> <input type="checkbox" name="id" id="id" class="room-box" value="${room.id}" /> </td>
								<td> ${room.id} </td>
								<td> ${room.title} </td>
								<td> ${room.host} </td>
								<td> ${room.info} </td>
								<td> ${room.winner} </td>
								<td> ${room.start} </td>
								<td> ${room.end} </td>
							</tr>
						</c:forEach>
					</table>
				</form>
			</div>
			<div class="modal-footer">
				<input type="submit" id="btn_delete" name="btn_delete" class="btn btn-default btn_delete_room" value="변경" />
				<button type="button" class="btn btn-default btn_modal_close" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="connModal" tabindex="-1" role="dialog" aria-labelledby="connModalLabel" aria-hidden="true" data-backdrop="static">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="connModalLabel">접속자 현황</h4>
			</div>
			<div class="modal-body">
					<form id="connForm" name="connForm" method="post" >
						<table class="table table-hover">
							<thead>
								<tr>
									<th colspan="3">실제 접속자</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td> uid </td>
									<td> nick </td>
									<td> room </td>
								</tr>
								<c:forEach var="player" items="${player}">
									<tr>
										<td> ${player.uid} <input type="hidden" id="connectID" name="connectID" value="${player.uid}" /> </td>
										<td> ${player.nick} </td>
										<td> ${player.room} </td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<table class="table table-hover">
							<thead>
								<tr>
									<th>기록상 접속자</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="connect" items="${connect}">
									<tr>
										<td> ${connect} <input type="hidden" id="recordID" name="recordID" value="${connect}" /> </td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</form>
			</div>
			<div class="modal-footer">
				<input type="submit" id="btn_delete" name="btn_delete" class="btn btn-default btn_delete_player" value="삭제" />
				<button type="button" class="btn btn-default btn_modal_close" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<script>
window.onload = function(){
	$("#msg-all").click( function () {
		$(".msg-box").prop( 'checked', this.checked );
	});

	$('.msg-box').on( 'change', function(){
		if(!this.checked){
			$('#msg-all').prop('checked', false);
		}
	});
	
	$("#room-all").click( function () {
		$(".room-box").prop( 'checked', this.checked );
	});

	$('.room-box').on( 'change', function(){
		if(!this.checked){
			$('#room-all').prop('checked', false);
		}
	});
	
	$("#player-all").click( function () {
		$(".player-box").prop( 'checked', this.checked );
	});

	$('.player-box').on( 'change', function(){
		if(!this.checked){
			$('#player-all').prop('checked', false);
		}
	});
	
	$(".btn_delete_msg").on('click', function () {
		deleteMsg();
	});
	
	$(".btn_delete_room").on('click', function () {
		closeRoom();
	});
	
	$(".btn_delete_player").on('click', function () {
		deletePlayer();
	});
}

function deleteMsg() {
	console.log("delete Message start");
jQuery.ajaxSettings.traditional = true;
	$.ajax({
        url:'/admin/msg_delete',
        type:'post',
        data:$("#msgForm").serialize(),
        success:function(data){
        	if(data == 0){
        		alert("성공");
        	} else {
        		alert("선택된 항목이 없습니다.");
        	}
        	console.log("delete Message end");
        	window.location.reload();
        },error:function(request,status,error){
		}
    })
}

function closeRoom() {
	console.log("close room start");
jQuery.ajaxSettings.traditional = true;
	$.ajax({
        url:'/admin/room_close',
        type:'post',
        data:$("#roomForm").serialize(),
        success:function(data){
        	if(data == 0){
        		alert("성공");
        	} else {
        		alert("선택된 항목이 없습니다.");
        	}
        	console.log("close room end");
        	window.location.reload();
        },error:function(request,status,error){
		}
    })
}

function deletePlayer() {
	console.log("delete player start");
jQuery.ajaxSettings.traditional = true;
	$.ajax({
        url:'/admin/player_delete',
        type:'post',
        data:$("#connForm").serialize(),
        success:function(){
        	alert("성공");
        	console.log("delete player end");
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