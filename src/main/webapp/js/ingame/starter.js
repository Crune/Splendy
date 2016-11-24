// 글로벌 변수 설정
var isPageMove = false;
var rid = -1;

$(document).ready(function() {			
	// 페이지가 시작됨과 동시에 소켓 서버 주소로 접속한다.
	chatSock = new SockJS("http://" + window.location.host + "/ws");

	// 방번호 가져옴
	var temp = (window.location.href).split('/');
	rid = temp[temp.lenght-1];
	
	setControll(false);
	
	// 인증 시도
	getAuth();
	
	chatSock.onopen = function() {
		wssend('auth', auth);
	};
			
	chatSock.onmessage = function(evt) {
		console.log("read: "+evt.data);
		
		var data = JSON.parse(evt.data);
		var k = data.type.split(".");
		var v = data.cont;
		
		if (k[0] == 'auth' && v == 'ok') {
			wssend('reqRoom', rid);
		}
		if (k[0] == 'room') {
			onRoom(k[1], v);
		}
		if (k[0] == 'player') {
			onPlayer(k[1], v);
		}
		if (k[0] == 'chat') {
			onChatMsg(k[1], v);
		}
		if (k[0] == 'left') {
			isPageMove = true;
			location.replace("/lobby/");
		}
	};

	chatSock.onclose = function() {
		if (isPageMove == false) {
			alert("연결끊김!");
		}
	};
	
	$(".backSpace").click(function(){
		wssend("left", rid);
	});
});


function setControll(boolVar) {
	if (boolVar) {
		$('#mask, .window').hide();
	} else {
		wrapWindowByMask();
	}
}