var temp_chatmsg = Handlebars.compile($("#temp_chatmsg").html());

var isReadPrevChat = false;

var chatNew;
var chatPriv;

function onChat() {
/*    chatPriv = stompClient.subscribe('/chat/private/'+uid, chat_priv);
*/    
    chatNew = stompClient.subscribe('/chat/new/'+rid, chat_new);
    
    send('chat/prev', rid);
}

$(document).ready(function() {
    // 채팅 엔터
    $('#chat_input').keypress(function(e) {
        if (e.which == 13) {
            send('chat/new/'+rid, this.value);
            this.value = "";
        }
    });
    
    chat_init();
    
    $("#chat_btn").on('click', function (){
    	$("#game_chat").toggle();
    	$("#chat_input").toggle();
    });
});

function chat_init() {
    console.log("Chatting initialized!");
    $(".chat_msg").detach();
}

/*function chat_priv(evt) {
    var data = JSON.parse(evt.body);
    if (data.type == 'prev') {
	    var msg = data.cont;
	    msgLen = msg.length;
	    for (i = 0; i < msgLen; i++) {
	        var curMsg = msg[i];
	        if (curMsg.uid == uid) {
	            curMsg.type = 'me';
	        } else if (curMsg.type == 'me') {
	            curMsg.type = 'o';
	        }
	        $("#chatDiv").append(temp_chatmsg(curMsg));
	        $("#chatDiv").scrollTop($("#chatDiv")[0].scrollHeight);
	    }
	    if (isReadPrevChat == false) {
	        send('room/list');
	        isReadPrevChat = true;
	    }
    }
}*/

function chat_new(evt) {
    input_chat(JSON.parse(evt.body));
}
function input_chat(msg) {
    if (msg.uid == uid) {
        msg.type = 'me'
    } else if (msg.type == 'me') {
        msg.type = 'o'
    }
    $("#game_chat").append(temp_chatmsg(msg));
    $("#game_chat").scrollTop($("#game_chat")[0].scrollHeight);
}