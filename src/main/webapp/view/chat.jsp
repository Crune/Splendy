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
</head>
<body>
	<div id="view" style="overflow: auto; max-height: 500px;">as</div>
	<br/><br/>
	<input type="text" id="to" placeholder="귓속말" />
	<input type="text" id="msg" value="Test Message">
	<input type="button" id="sendEvent" value="Send">
	<br/><br/>
	<input type="button" id="closeEvent" value="Close">

<script type="text/javascript">
	var ws = null;
	$(document).ready(function () {
		ws = new SockJS('/myHandler');
		ws.onopen = function () {};
		
		ws.onmessage = function(messageEvent){
			$("#view").append(messageEvent.date+"<br />");
			$("#view").scrollTop(99999999);
		};
		ws.onclose = function () {
			//ws.send("");
		};
		$("#msg").keydown(function (key) {
			if(key.keyCode == 13) {
				$("#sendEvent").click();
			}
		});
		$("#sendEvent").click(function() {
			if($("msg").val() != "") {
				msg = {};
				msg.message = $("#msg").val();
				msg.type = "all";
				msg.to = "all";
				var to = $("#to").val();
				if(to != "") {
					msg.type = "one";
					msg.to = to;
				}
				ws.send(JSON.stringify(msg));
				$("#view").append("나 -> " + $("#msg").val() + "<br />");
				$("#view").scrollTop(99999999);
				$("msg").val("");
			}
		});
	});
</script>
</body>
</html>