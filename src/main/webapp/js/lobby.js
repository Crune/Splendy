$( document ).ready(function() {
	
	// 웹소켓에 사용자 정보 등록을 위한 인증 코드 획득
	getAuth();

	// 게임방 마우스 이벤트 동작 설정
	roomMouseEvt();
	
	// 채팅 엔터
	$('#chat_input').keypress(function(e) {
		if (e.which == 13) {
			wssend('chat', this.value);
			this.value = "";
		}
	});
	
	// 웹소켓 실행 후 인증
	chatSock = new SockJS("http://" + window.location.host + "/ws");
	chatSock.onopen = function () {
		wssend('auth', auth);	//핸들러에서 구분할 수 있게 
	};

	// 메시지 이벤트 핸들러 연결
	chatSock.onmessage = function (evt) {	
		console.log("read.raw: "+evt.data);
		var data = JSON.parse(evt.data);
		var k = data.type.split(".");
		var v = data.cont;
		if (k[0] == 'init') {
			console.log("initialized.");
		}
		if (k[0] == 'auth' && v == 'ok') {
			wssend('request', 'prevMsg');
			wssend('request', 'roomList');
			wssend('request', 'playerList');
		}
		if (k[0] == 'room') {
			onRoom(k[1], v);
		}
		if (k[0] == 'player') {
			onPlayer(k[1], v);
		}
		if (k[0] == 'chat') {
			onChatMsg(k[1], v);
		}
		//$("#chatMessage").append(evt.data + "<br/>");
	};

	// 연결 해제시 핸들러 연결
	chatSock.onclose = function () {
		alert("연결끊김!");
	};
	
	// 방개설시

	$("#btn_create").on("click", function() {
		alert("방개설");
	});
	$("#btn_create_cancel").on("click", function() {
		$("#createRoom").hide();
		$("#roomlist").append(temp_room_empty());
		roomMouseEvt();
	});
});

var temp_chatmsg = Handlebars.compile($("#temp_chatmsg").html());
var temp_room = Handlebars.compile($("#temp_room").html());
var temp_player = Handlebars.compile($("#temp_player").html());
var temp_room_empty = Handlebars.compile($("#temp_room_empty").html());

function onChatMsg(type, msg) {
	if (type =='init') {
		console.log("Chatting initialized!");
		$(".chat_msg").detach();
	} else {
		if (type =='new') {
			console.log("chat.add!");
			$("#chatDiv").append(temp_chatmsg(msg));
			$("#chatDiv").scrollTop($("#chatDiv")[0].scrollHeight);
		}
	}
}

function onPlayer(type, pl) {
	if (type=='init') {
		console.log("Player initialized!");
		$(".player").detach();
	} else {
		$("#user_"+pl.uid).detach();	
		if (type=='add') {
			$(".lobby_players").append(temp_player(pl));
		}
		if (type=='join') {
			$(".lobby_players").append(temp_player(pl));
			onChatMsg(new Chat('시스템', pl.nick+'님이 접속하였습니다.','','sys'));
		}
		if (type=='leave') {
			onChatMsg(new Chat('시스템', pl.nick+'님이 나가셨습니다.','','sys'));
		}
		if (type=='enter') {
			var room = pl.room;
			$("#room_"+room).append(temp_player(pl));
		}
	}
}

function onRoom(type, room) {
	if (type=='init') {
		console.log("Room initialized!")
		$(".lobby_room").detach();
		$("#roomlist").append(temp_room_empty());
	} else {
		if (type=='add') {
			$(".empty_room").detach();
			$("#roomlist").append(temp_room(room));
			$("#roomlist").append(temp_room_empty());
		}
		if (type=='remove') {
			$("#room_"+room).detach();
		}
	}
	roomMouseEvt();
}

function roomMouseEvt() {
	// 게임방 마우스 이벤트 동작 설정
	$(".lobby_room").on("mouseenter", function() {
		$(this).addClass("lobby_room_hover");
	}).on("mouseleave", function() {
		$(this).removeClass("lobby_room_hover");
	}).on("click", function() {

		if ($(this).attr("id") == "room_0") {
			$("div#createRoom").show();
			$(".empty_room").detach();
		} else {
			alert("방 접속: " + $(this).attr("id"));
		}
		
	});
}