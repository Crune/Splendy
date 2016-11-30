//var temp_player = Handlebars.compile($("#temp_player").html());

var isReadPrevPlayer = false;

var playerPriv;
var playerJoin;
var playerLeft;

function onComp() {
    playerPriv = stompClient.subscribe('/player/private/'+uid, player_priv);

    playerJoin = stompClient.subscribe('/player/join/'+rid, player_join);
    playerLeft = stompClient.subscribe('/player/left', player_left);

    send('player/join/'+rid, '');
    player_init();
    send('player/prev/'+rid, '');
}
