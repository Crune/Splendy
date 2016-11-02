<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel='stylesheet' href='/webjars/bootstrap/css/bootstrap.min.css'>
</head>
<body>
	<TEXTAREA id="view" rows="10" cols="50" readonly="readonly"> </TEXTAREA>
	<br/><br/>
	<input type="text" id="msg" value="Test Message">
	<input type="button" name="sendEvent" value="Send" onclick="send();">
	<br /><br/>
	<input type="button" name="ConnectEvent" value="Connect" onclick="connect();">
	<input type="button" name="ConnectEvent" value="Connect" onclick="connect1();">
	<input type="button" name="closeEvent" value="Close" onclick="closeEvent();">

<script type="text/javascript">
 
	var ws = null;
 
	connect = function(){
		if(ws == null){
			ws = new WebSocket('ws://' + window.location.host + '/1234/myHandler');
			//접속이 완료된 후 수행되는 부분
			ws.onopen = function(){
			console.log('Opened!');
			}
			//Message가 수신되면 수행되는 부분 
			ws.onmessage = function(messageEvent){
				console.log('messageEvent : ' + messageEvent.data);
				var view = document.getElementById('view')
				view.value += '\n' + messageEvent.data;
			}
			//접속이 종료된 후 수행하되 부분
			ws.onclose = function(){
				console.log('Closed!');
			}
			console.log(ws);
		}else{
			console.log('이미 연결되어 있음.');
		}
	}
	connect1 = function(){
		if(ws == null){
			ws = new WebSocket('ws://' + window.location.host + '/1111/myHandler');
			//접속이 완료된 후 수행되는 부분
			ws.onopen = function(){
			console.log('Opened!');
			}
			//Message가 수신되면 수행되는 부분 
			ws.onmessage = function(messageEvent){
				console.log('messageEvent : ' + messageEvent.data);
				var view = document.getElementById('view')
				view.value += '\n' + messageEvent.data;
			}
			//접속이 종료된 후 수행하되 부분
			ws.onclose = function(){
				console.log('Closed!');
			}
			console.log(ws);
		}else{
			console.log('이미 연결되어 있음.');
		}
	}   
	send = function(){
// Open이 되어 있지 않으면 send가 되지 않음.
               if(ws != null){
                       var msg = document.getElementById('msg').value;
                       if(msg.length > 0){
                              ws.send(msg);
                       }
               }else{
                       console.log('연결되어 있지 않음.');
               }
        }
       
        closeEvent = function(){
               if(ws != null){
                       ws.close();
                       ws = null;
               }else{
                       console.log('연결되어 있지 않음.');
               }
        }
 
</script>
</body>
</html>