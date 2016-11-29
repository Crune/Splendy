// 글로벌 변수 설정
var isPageMove = false;
var rid = 0;
/*
function noticePrc(data) {
    alert(data);
}
*/
function setConnected(connected) {
    if (connected) {
        // 통신 내용 표시 여부
        //stompClient.debug = null;
        //var notice = stompClient.subscribe('/notice/everyone/', noticePrc);

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