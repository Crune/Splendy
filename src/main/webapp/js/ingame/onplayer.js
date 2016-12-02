
var temp_player = Handlebars.compile($("#temp_player").html());

var isReadPrevPlayer = false;

var playerPriv;
var playerJoin;
var playerLeft;

var pls = {};
pls.order = new Array();
pls.data = {};

function onPlayer() {
    player_init();

    playerPriv = stompClient.subscribe('/player/private/'+uid, player_priv);

    playerJoin = stompClient.subscribe('/player/join/'+rid, player_join);
    playerLeft = stompClient.subscribe('/player/left', player_left);

    send('player/join/'+rid, '');
    send('player/prev/'+rid, '');
}

function player_init() {
    console.log("Player initialized!");
    
}

function player_refresh(){
	$(".player ul").detach();
	for(var i=0, uid; uid = pls.order[i]; i++) {
		console.log(pls.data[uid]);
		$(".player"+(i+1)).html(temp_player(pls.data[uid]));
	}
}


function player_priv(evt) {
    var data = JSON.parse(evt.body);
    if (data.type == 'prev') {
        var pl = data.cont;
        for (i = 0, pl; pl = data.cont[i]; i++) {
            pls.data[pl.uid] = pl;
            pls.order.push(pl.uid);
        	console.log("player added: "+pl);
        }
        isReadPrevPlayer = true;
        player_refresh();
    }
}

function player_join(evt) {
    var pl = JSON.parse(evt.body);
    if (pls.data[pl.uid]) {
        pls.data[pl.uid] = pl;
    } else {
        pls.data[pl.uid] = pl;
        pls.order.push(pl.uid);
        input_chat(new Chat('시스템', pl.nick+'님이 접속하였습니다.','','sys'));
        player_refresh();
    }
}

function player_left(evt) {
    var pl = JSON.parse(evt.body);
    if (pls.data[pl.uid] && pl.room == rid) {
        var tempArr = new Array();
        delete pls.data[pl.uid];
        for(var i=0, uid; uid=pls.order[i]; i++) {
            if(uid != pl.uid) {
                console.log("uid추가:"+uid);
                tempArr.push(uid);
            }
        }
        console.log(pls);
        pls.order = tempArr;
        input_chat(new Chat('시스템', pl.nick+'님이 나가셨습니다.','','sys'));
        player_refresh();
    }
}