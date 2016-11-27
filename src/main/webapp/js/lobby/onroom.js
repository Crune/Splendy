var temp_room = Handlebars.compile($("#temp_room").html());
var temp_room_empty = Handlebars.compile($("#temp_room_empty").html());

var isReadPrevRoom = false;

var roomNew;
var roomPriv;

function onRoom() {
    roomPriv = stompClient.subscribe('/room/private/'+uid, room_priv);
    
    roomNew = stompClient.subscribe('/room/new', room_new);
    roomNew = stompClient.subscribe('/room/remove', room_remove);
    
    room_init();
    
    send('room/prev', '');
}

$(document).ready(function() {

});

function room_init() {
    console.log("Room initialized!")
    $(".lobby_room").detach();
}

function room_priv(evt) {

    var msg = JSON.parse(evt.body);
    var type = msg.type;
    var cont = msg.cont;
    
    if (type == 'prev') {
        room_prev(cont);
    }
    if (type == 'accept') {
        room_accept(cont);
    }
    if (type == 'can_left') {
        /* 로비에서는 작동하지 않음.
        isPageMove = true;
        location.replace("/lobby/");
        */
    }
}

function room_prev(rooms) {
    $(".empty_room").detach();
    
    roomLen = rooms.length;
    for (i = 0; i < roomLen; i++) {
        var curRoom = rooms[i];
        $("#roomlist").append(temp_room(curRoom));
        if (curRoom.password != 'true') {
            $('#ispw_'+curRoom.id).detach();
        }
    }
    send('player/list');
    
    $("#roomlist").append(temp_room_empty());
    
    roomMouseEvt();
    
}

function room_accept(roomId) {
    isPageMove = true;
    location.replace("/game/" + roomId);
}

function room_new(evt) {
    $(".empty_room").detach();
    
    var room = JSON.parse(evt.body);
    $("#roomlist").append(temp_room(room));
    if (room.password != 'true') {
        $('#ispw_'+room.id).detach();
    }
    
    $("#roomlist").append(temp_room_empty());
    
    roomMouseEvt();
}

function room_remove(roomId) {
    $("#room_"+roomId).detach();
}

function joinRoom(rid, password) {
    var room = new Object();
    room.id = rid;
    if (password != '') {
        room.password = password;
    }
    send('room/join/'+rid, password);
}

function roomMouseEvt() {

    // 방 개설시
    $("#btn_create").on("click", function() {
        var req_room = {
            'title':$('#title').val(),
            'info':$('#info').val(),
            'password':$('#password').val(),
            'playerLimits':$('#playerLimits').val()
        }
        send('room/new', req_room);
        $("#createRoom").hide();
        //alert("방개설을 요청하였습니다.");
    });
    
    // 방 개설 취소시
    $("#btn_create_cancel").on("click", function() {
        $("#createRoom").hide();
        $(".empty_room").show();
        $("#roomlist").css('height','calc(100% - 99px)');
        roomMouseEvt();
    });
    
    // 방 목록 항목 마우스 이벤트 동작 설정
    $(".lobby_room").on("mouseenter", function() {
        $(this).addClass("lobby_room_hover");
    }).on("mouseleave", function() {
        $(this).removeClass("lobby_room_hover");
    }).on("click", function() {
        var rid = $(this).attr("id").split('_')[1];
        if (rid == '0') {
            $("div#createRoom").show();
            $(".empty_room").hide();
            $("#roomlist").css('height','calc(100% - 366px)');
        } else if ($('#ispw_'+rid)) {
            $('#ispw_'+rid).show();
        } else {
            joinRoom(rid, '');
        }
        
    });
    
    // 비밀번호 입력 후 접속 클릭 시
    $(".btn_joinroom").on("click", function() {
        var rid = $(this).attr("id").split('_')[2];
        var pw = $('#rpw_'+rid).val();
        joinRoom(rid, pw);
    });
}