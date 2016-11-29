var temp_player = Handlebars.compile($("#temp_player").html());

var isReadPrevPlayer = false;

var playerPriv;
var playerJoin;
var playerLeft;

function onPlayer() {
	playerPriv = stompClient.subscribe('/player/private/'+uid, player_priv);
    
    playerJoin = stompClient.subscribe('/player/join/'+rid, player_join);
    playerLeft = stompClient.subscribe('/player/left/'+rid, player_left);
    
    player_init();
    send('player/prev/'+rid, '');
}

function player_init() {
    console.log("Player initialized!");
    $(".player").detach();
}

function player_priv(evt) {
    var data = JSON.parse(evt.body);
    if (data.type == 'prev') {
	    var pl = data.cont;
		plLen = pl.length;
		for (i = 0; i < plLen; i++) {
			var curPl = pl[i];
			if (curPl.room == '0') {
				$(".lobby_players").append(temp_player(curPl));
			} else {
				$("#room_"+curPl.room+" .row .room_player").append(temp_player(curPl));
			}
		}
    }
	
}

function player_join(evt) {
    var pl = JSON.parse(evt.body);
    if (pl.room == '0') {
        $(".lobby_players").append(temp_player(pl));
        onChatMsg(new Chat('new', '시스템', pl.nick+'님이 접속하였습니다.','','sys'));
    } else {
        $("#user_"+pl.uid).detach();
        $("#room_"+pl.room+" .row .room_player").append(temp_player(pl));
    }
}

function player_left(evt) {
    var pl = JSON.parse(evt.body);
    $("#user_"+pl.uid).detach();
    if (pl.room == '0') {
        onChatMsg(new Chat('new', '시스템', pl.nick+'님이 나가셨습니다.','','sys'));
    } else {
    }
}

function joinRoom(rid, password) {
    var room = new Object();
    room.id = rid;
    if (password != '') {
        room.password = password;
    }
    send('player/join/'+rid, password);
}