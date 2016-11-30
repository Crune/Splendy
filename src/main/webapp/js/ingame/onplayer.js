
//var temp_player = Handlebars.compile($("#temp_player").html());

var isReadPrevPlayer = false;

var playerPriv;
var playerJoin;
var playerLeft;

function onPlayer() {
    playerPriv = stompClient.subscribe('/player/private/'+uid, player_priv);

    playerJoin = stompClient.subscribe('/player/join/'+rid, player_join);
    playerLeft = stompClient.subscribe('/player/left', player_left);

    send('player/join/'+rid, '');
    player_init();
    send('player/prev/'+rid, '');
}

function player_init() {
    console.log("Player initialized!");
    /* 적절하게 다 뗄것
    $(".player").detach();
    */
}

function player_priv(evt) {
    var data = JSON.parse(evt.body);
    if (data.type == 'prev') {
        var pl = data.cont;
        plLen = pl.length;
        for (i = 0; i < plLen; i++) {
            var curPl = pl[i];
            /* 적절한 위치에 적절하게 붙일것
			$("#room_"+curPl.room+" .row .room_player").append(temp_player(curPl));
			*/
        }
    }
}

function player_join(evt) {
    var pl = JSON.parse(evt.body);
    $("#user_"+pl.uid).detach();

	/* 적절한 위치에 적절하게 붙일것
	 $("#room_"+pl.room+" .row .room_player").append(temp_player(pl));
	 */
	input_chat(new Chat('시스템', pl.nick+'님이 접속하였습니다.','','sys'));
}

function player_left(evt) {
    var pl = JSON.parse(evt.body);
	/* 적절하게 다 뗄것
	 $(".player").detach();
	 */

    input_chat(new Chat('시스템', pl.nick+'님이 나가셨습니다.','','sys'));
	/* 적절한 위치에 적절하게 붙일것
	 $("#room_"+pl.room+" .row .room_player").append(temp_player(pl));
	 */
}