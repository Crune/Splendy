
var temp_player = Handlebars.compile($("#temp_player").html());

var isReadPrevPlayer = false;

var playerPriv;
var playerJoin;
var playerLeft;

var playerMap = newMap();
var playerList = new Array();

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
    
}

function player_refresh(){
	$(".player ul").detach();
	for(i=0; i<playerList.length; i++){
		$(".player"+(i+1)).html(temp_player(playerMap.get(playerList[i])));
	}
	
}


function player_priv(evt) {
    var data = JSON.parse(evt.body);
    
    if (data.type == 'prev') {
        var pl = data.cont;
        console.log(pl);
        plLen = pl.length;
        
        for (i = 0; i < plLen; i++) {
        	var curPl = pl[i];
        	playerList.push(curPl.uid);
        	playerMap.put(curPl.uid, curPl);        	
        }
        player_refresh();
    }
}

function player_join(evt) {
    var pl = JSON.parse(evt.body);      
    for(i = 0; i < playerList.length; i++){
    	if(playerList[i].uid === pl.uid){
    		return;
    	}	
    }
    playerMap.put(pl.uid, pl);
    playerList.push(pl.uid);
    
	input_chat(new Chat('시스템', pl.nick+'님이 접속하였습니다.','','sys'));
	player_refresh();
}

function player_left(evt) {
	var tempArr = new Array();
    var pl = JSON.parse(evt.body);
    
    for(i = 0; i < playerList.length; i++){
    	if( (pl.uid != playerList[i].uid) && (pl.room != 0) ){
    		console.log(playerList[i]);
    		tempArr.push(playerList[i]);
    		alert('t');
    	}
    }
    playerList = tempArr;
    $(".player ul").detach();
    console.log(playerList);
    player_refresh();
    
    

    input_chat(new Chat('시스템', pl.nick+'님이 나가셨습니다.','','sys'));
	/* 적절한 위치에 적절하게 붙일것
	 $("#room_"+pl.room+" .row .room_player").append(temp_player(pl));
	 */
}