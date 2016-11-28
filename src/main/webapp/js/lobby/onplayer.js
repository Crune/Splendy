var temp_player = Handlebars.compile($("#temp_player").html());

var isReadPrevPlayer = false;

var playerNew;
var playerPriv;

function onPlayer() {
	playerPriv = stompClient.subscribe('/player/private/'+uid, player_priv);
    
    playerJoin = stompClient.subscribe('/player/join', player_join);
    playerLeft = stompClient.subscribe('/player/left', player_left);
    
    player_init();
    send('player/prev/'+rid, '');
}

function player_init() {
    console.log("Chatting initialized!");
    $(".chat_msg").detach();
}

function player_priv(evt) {
	alert('pl:'+evt);
    var data = JSON.parse(evt.body);
    if (data.type == 'prev') {
	    var pl = data.cont;
		plLen = pl.length;
		for (i = 0; i < plLen; i++) {
			var curPl = pl[i];
			if (curPl.room == 0) {
				$(".lobby_players").append(temp_player(curPl));
			} else {
				$("#room_"+curPl.room+" .row .room_player").append(temp_player(curPl));
			}
		}
    }
	
}

function player_join(evt) {
	
}

function player_left(evt) {
	
}

function joinRoom(rid, password) {
    var room = new Object();
    room.id = rid;
    if (password != '') {
        room.password = password;
    }
    send('player/join/'+rid, password);
}