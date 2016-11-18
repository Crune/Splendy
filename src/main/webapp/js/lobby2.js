var stompClient = null;

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/lobby', eventHandler(data));
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function eventHandler(data) {
    console.log("event: "+JSON.parse(data.body).content);
}

var chatSock;

window.onload = function() {
	
	// 웹소켓에 사용자 정보 등록을 위한 인증 코드 획득
	getAuth();

	// 게임방 마우스 이벤트 동작 설정
	$(".lobby_room").on("mouseenter", function() {
		$(this).addClass("lobby_room_hover");
	}).on("mouseleave", function() {
		$(this).removeClass("lobby_room_hover");
	}).on("click", function() {

		if ($(this).attr("id") == "room_0") {
			alert("방 개설");
		} else {
			alert("방 접속: " + $(this).attr("id"));
		}
		
	});

	// 웹소켓 연결
	connect();
	
	// 웹소켓 실행 후 인증
	chatSock = new SockJS("http://" + window.location.host + "/ws");
	chatSock.onopen = function () {
	};

	// 메시지 이벤트 핸들러 연결
	chatSock.onmessage = function (evt) {
		console.log("read.raw: "+evt.data);
		var data = JSON.parse(evt.data);
		var k = data.type;
		var v = data.cont;
		if (k == 'init') {
			console.log("initialized.");
		} else if (k == 'room') {
			console.log(v.title);
		}
		//$("#chatMessage").append(evt.data + "<br/>");
	};

	// 연결 해제시 핸들러 연결
	chatSock.onclose = function () {
		alert("연결끊김!");
	};
}
function wssend(type, msg) {
    stompClient.send("/app/msg", {}, JSON.stringify( new Msg(type, msg) ));
}

function Msg(type, cont) {
	this.type = type;
	this.cont = cont;
}

function Auth(uid, code) {
	this.uid = uid;
	this.code = code;
}

var auth = new Auth();
function getAuth() {
	$.getJSON("/lobby/getAuthCode", function(data) {
		auth.code = data.code;
		auth.uid = data.uid;
	})
}