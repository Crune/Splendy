// 글로벌 변수 설정
var isPageMove = false;

function setConnected(connected) {
    if (connected) {
        // 통신 내용 표시 여부
        //stompClient.debug = null;

        onChat();
        onRoom();
        onPlayer();
        onComp();

        setControll(true);
    }
    else {
        if (isPageMove == false) {
            alert("튕김");
            connect();
        }
    }
}


$(document).ready(function() {
    global:!1

	// 입력 받지 않음
	setControll(false);

    // 페이지가 시작됨과 동시에 웹소켓에 접속한다.
    connect();	// in sock.js
	
	$(".backSpace").click(function(){
		send("player/left", rid);
	});
});


function setControll(boolVar) {
	if (boolVar) {
		$('#mask, .window').hide();
	} else {
		wrapWindowByMask();
	}
}