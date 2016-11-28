var temp_player = Handlebars.compile($("#temp_player").html());

var isReadPrevPlayer = false;

var playerNew;
var playerPriv;

function onPlayer() {
	playerPriv = stompClient.subscribe('/player/private/'+uid, room_priv);
    
    playerJoin = stompClient.subscribe('/player/join', player_join);
    playerLeft = stompClient.subscribe('/player/left', player_left);
    
    room_init();
}

function player_join(evt) {
	
}
function joinRoom(rid, password) {
    var room = new Object();
    room.id = rid;
    if (password != '') {
        room.password = password;
    }
    send('player/join/'+rid, password);
}