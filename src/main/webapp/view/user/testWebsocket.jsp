<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
    <div id="output"></div>
    <script>

        function setup() {
            output = document.getElementById("output");
            ws = new WebSocket("ws://echo.websocket.org");
            ws.onopen = fuction(e) {
                log("연결되었습니다.");
                sendMessage("웹소켓님 반가워요");
            }
            ws.onclose = function (e) {
                log("연결 닫힘 : " + e.reason);
            }
            ws.onerror = function (e) {
                log("에러");
            }
            ws.onmessage = function (e) {
                log("메세지 도착 : ", e.data);
                ws.close();
            }
        }

        function sendMessage(msg) {
            ws.send(msg);
            log("메시지를 보냈습니다.");
        }

        function log(s) {
            var p = document.createElement("p");
            p.style.wordWrap = "break-word";
            p.textContent = s;
            output.appendChild(p);
            console.log(s);
        }
        setup();
    </script>
</body>
</html>