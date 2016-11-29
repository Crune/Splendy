var socket = null;
var stompClient = null;

function connect() {
    var socket = new SockJS("http://" + window.location.host + "/socket");
    stompClient = Stomp.over(socket);
    stompClient.connect({ 'uid': uid, 'rid': rid }, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/notice/everyone', function (evt) {
        	var notice = JSON.parse(evt.body)
        	var type = notice.type;
        	var cont = notice.cont;
            showNotice(type, cont);
        });
    });
    //stompClient.disconnect(disconnect);
}
function showNotice(type, cont) {
    alert(type+"/"+cont);
};
function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

var headers = {
    url: window.location.href
};
function send(type, cont) {
    stompClient.send("/req/"+type, headers, JSON.stringify(cont));
}

function Msg(type, cont) {
	this.type = type;
	this.cont = cont;
}

function Chat(nick, cont, time, type) {
	this.nick = nick;
	this.cont = cont;
	this.time = time;
	this.type = type;
}