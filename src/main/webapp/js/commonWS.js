function wssend(type, msg) {
	chatSock.send( JSON.stringify( new Msg(type, msg)) );
}

function Msg(type, cont) {
	this.type = type;
	this.cont = cont;
}

function Chat(type, cont, time, type) {
	this.nick = nick;
	this.cont = cont;
	this.time = time;
	this.type = type;
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