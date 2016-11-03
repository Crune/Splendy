<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel='stylesheet' href='/webjars/bootstrap/3.3.4/dist/css/bootstrap.min.css' />
<script type='text/javascript' src="/webjars/jquery/2.1.3/dist/jquery.min.js"></script>
<script type='text/javascript' src="/webjars/sockjs-client/0.3.4/sockjs.min.js"></script>
<script type="text/javascript">
	var chatSock = null;

	$(document).ready(function () {
		// 페이지가 시작됨과 동시에 소켓 서버 주소로 접속한다.
		chatSock = new SockJS("http://" + window.location.host + "/myHandler");
		chatSock.onopen = function () {

			message = {};
			message.message = "반갑습니다~";
			message.type = "all";
			message.to = "all";

			chatSock.send( JSON.stringify(message) );
		};

		chatSock.onmessage = function (evt) {
			$("#chatMessage").append(evt.data + "<br/>");
			$("#chatMessage").scrollTop(99999999);
		};

		chatSock.onclose = function () {
		};

		$("#sendMessage").click(function () {
			if ( $("#message").val() != "") {
				message = {};
				message.message = $("#message").val();
				message.type = "all";
				message.to = "all"

				var to = $("#to").val();
				if (to != "" ) {
				message.type = "one";
				message.to = to;
				}

				chatSock.send( JSON.stringify(message) );
				$("#chatMessage").append("나 -> "+ $("#message").val() + "<br/>");
				$("#chatMessage").scrollTop(99999999);

				$("#message").val("");
			}
		});
	});
</script>
</head>
<body>
<div id="chatMessage" style="overflow: auto; height: 250px;"></div>
	<input type="text" id="to" placeholder="귓속말" />
	<input type="text" id="message" placeholder="메시지" />
	<input type="button" id="sendMessage" value="메시지 보내기" />
</body>
</html>