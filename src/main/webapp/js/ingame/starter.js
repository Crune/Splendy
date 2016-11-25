// 글로벌 변수 설정
var isPageMove = false;
var rid = -1;

$(document).ready(function() {			
	// 페이지가 시작됨과 동시에 소켓 서버 주소로 접속한다.
	chatSock = new SockJS("http://" + window.location.host + "/ws");

	// 방번호 가져옴
	var temp = (window.location.href).split('/');
	rid = temp[temp.lenght-1];
	
	// 입력 받지 않음
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
			// 접속시 카드들의 기본정보를 받아옴.
			wssend('reqCards', rid);
			// 그 이후 방 기본 정보를 받아옴.
		}
		if (k[0] == 'left') {
			isPageMove = true;
			location.replace("/lobby/");
		}
		
		if (k[0] == 'room') {
			onRoom(k[1], v);
		}
		if (k[0] == 'player') {
			onPlayer(k[1], v);
		}
		if (k[0] == 'chat') {
			onChat(k[1], v);
		}
		if (k[0] == 'comp') {
			onComp(k[1], v);
		}
	};

	chatSock.onclose = function() {
		if (isPageMove) {
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