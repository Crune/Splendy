// 글로벌 변수 설정
var isPageMove = false;
var rid = 0;

function setConnected(connected) {
    if (connected) {
        onChat();
        onRoom();
        onPlayer();
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
    // 입력 받지 않음
    setControll(false);
    
    // 페이지가 시작됨과 동시에 웹소켓에 접속한다.
    connect();
});