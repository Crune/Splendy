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
	
	// 웹소켓 실행 후 인증
	chatSock = new SockJS("http://" + window.location.host + "/ws");
	chatSock.onopen = function () {
		wssend('auth', auth);
		wssend('request', 'roomList');
	};

	// 메시지 이벤트 핸들러 연결
	chatSock.onmessage = function (evt) {
		$("#chatMessage").append(evt.data + "<br/>");
	};

	// 연결 해제시 핸들러 연결
	chatSock.onclose = function () {
		alert("연결끊김!");
	};
}
function wssend(type, msg) {
	chatSock.send( JSON.stringify( new Msg(type, msg)) );
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