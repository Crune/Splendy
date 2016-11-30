var isReadPrevRoom = false;

var roomPriv;
var roomRemove;
var roomEvent;

function onRoom() {
    roomPriv = stompClient.subscribe('/room/private/'+uid, room_priv);

    roomEvent = stompClient.subscribe('/room/event/'+rid, room_event);
    roomRemove = stompClient.subscribe('/room/remove', room_remove);

    room_init();

    send('room/prev', rid);
}

$(document).ready(function() {

});

function room_init() {
    console.log("Room initialized!")
}

function room_event(evt) {

    var msg = JSON.parse(evt.body);
    var type = msg.type;
    var cont = msg.cont;

    if (type == 'start') {
    }
    if (type == 'halt') {
    }
    if (type == 'resume') {
    }
    if (type == 'end') {
    }
    if (type == 'score') {
    }
    if (type == 'actor') {
    }
}


function room_priv(evt) {

    var msg = JSON.parse(evt.body);
    var type = msg.type;
    var cont = msg.cont;

    if (type == 'current') {
        room_prev(cont);
    }
    if (type == 'can_left') {
    	isPageMove = true;
    	location.replace("/lobby/");
    }
}

function room_prev(room) {
    for (var i = 0, coin; coin = room.coins[i]; i++) {
        setCoin(coin.u_id, coin.cn_id, coin.cn_count);
    }

    if (!isReadPrevRoom) {
        isReadPrevRoom = true;
    }
    comp_refresh();
}

function room_remove(roomId) {
    roomId = roomId.body;
    if (rid == roomId) {
        isPageMove = true;
        location.replace("/lobby/");
    }
}