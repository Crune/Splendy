<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<script type='text/javascript' src="/webjars/jquery/2.1.3/dist/jquery.min.js"></script>
<script type='text/javascript' src="/webjars/sockjs-client/0.3.4/sockjs.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<script type="text/javascript">
    var sock = null;
    var message = {};
     
    $(document).ready(function() {
        sock = new SockJS("http://" + window.location.host + "/paint");
        var ctx = null;
        
        var isDown = false;
        var prevX = 0;
        var prevY = 0;
         
        // 마우스를 눌렀을 때
        $("#paint").mousedown(function(e) {
            var c = document.getElementById("paint");
            ctx = c.getContext("2d");
 
            ctx.strokeStyle = $("#color").val();
            ctx.beginPath();
            isDown = true;
            prevX = e.pageX;
            prevY = e.pageY;
            console.log(e.pageX);
        });
         
        var point = {};
        // 마우스를 움직일 때
        $("#paint").mousemove(function(e) {
             
            if ( isDown ) {
                point.prevX = prevX;
                point.prevY = prevY;
                point.nowX = e.pageX;
                point.nowY = e.pageY;
                point.color = $("#color").val();
                 
                sock.send( JSON.stringify(point) );
                 
                // moveTo 시작점을 설정해주는 것
                ctx.moveTo(prevX, prevY);
                // lineTo 끝점을 설정해주는 것
                ctx.lineTo(e.pageX,e.pageY);
                ctx.stroke();
                 
                prevX = e.pageX;
                prevY = e.pageY;
            }
        });
         
        // 마우스를 뗄 때
        $("#paint").mouseup(function(e) {
            isDown = false;
            ctx.closePath();
            console.log(e.pageX);
        });
         
        $("#fill").click(function () {
            var c = document.getElementById("paint");
            ctx = c.getContext("2d");
             
            ctx.beginPath();
            ctx.rect(0, 0, 500, 500);
            ctx.fillStyle = $("#color").val();
            ctx.fill();
            ctx.closePath();
             
            var fill = {};
            fill.mode = "fill";
            fill.color = $("#color").val();
            sock.send( JSON.stringify(fill));
        });
         
        sock.onmessage = function(evt) {
          console.log(evt);
            var drawData = JSON.parse(evt.data);

            var c = document.getElementById("paint");
            var otherCtx = c.getContext("2d");

            if(drawData.mode != undefined && drawData.mode == "fill") {
                otherCtx.rect(0, 0, 500, 500);
                otherCtx.fillStyle = drawData.color;
                otherCtx.fill();
                otherCtx.closePath();

                return;
            }

            otherCtx.strokeStyle = drawData.color;
            otherCtx.beginPath();
            otherCtx.moveTo(drawData.prevX, drawData.prevY);
            otherCtx.lineTo(drawData.nowX, drawData.nowY);
            otherCtx.stroke();
            otherCtx.closePath();
        };
          
    });
 
</script>
</head>
<body style="margin: 0px; padding: 0px;">
    <div style="width: 500px; float: left;">
        <canvas id="paint" width="500" height="500" style="border: 1px solid #333;"></canvas>
        <br/>
        <input type="color" id="color" value="#000000" />
        <input type="button" id="fill" value="채우기" />
    </div>
</body>
</html>