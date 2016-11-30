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

    if (type == 'prev') {
        room_prev(cont);
    }
    if (type == 'can_left') {
    	isPageMove = true;
    	location.replace("/lobby/");
    }
}

function room_prev(rooms) {


    if (!isReadPrevRoom) {
        isReadPrevRoom = true;
    }
}

function room_remove(roomId) {
    roomId = roomId.body;
    if (rid == roomId) {
        isPageMove = true;
        location.replace("/lobby/");
    }
}