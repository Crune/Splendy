var temp_player = Handlebars.compile($("#temp_player").html());

function onPlayer() {/*
    stompClient.subscribe('room.prev.'+uid, function(evt) {
        var room = JSON.parse(evt.body);
        
        roomLen = room.length;
        for (i = 0; i < roomLen; i++) {
            var curRoom = room[i];
            $("#roomlist").append(temp_room(curRoom));
            if (curRoom.password != 'true') {
                $('#ispw_'+curRoom.id).detach();
            }
        }
            
        wssend('request', 'playerList');
    });*/
}

function joinRoom(rid, password) {
    var room = new Object();
    room.id = rid;
    if (password != '') {
        room.password = password;
    }
    send('player/join/'+rid, password);
}