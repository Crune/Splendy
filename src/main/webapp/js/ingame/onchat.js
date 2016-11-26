/**
 * 
 */

function onChat(type, msg) {
	if (type =='init') {
		console.log("Chatting initialized!");
		//$(".chat_msg").detach();
	} else {
		if (type =='new') {
			if (msg.uid == auth.uid) {
				msg.type = 'me'
			} else if (msg.type == 'me') {
				msg.type = 'o'
			}
			// TODO 새로운 메시지가 들어올 경우
			/*
			$("#chatDiv").append(temp_chatmsg(msg));
			$("#chatDiv").scrollTop($("#chatDiv")[0].scrollHeight);*/
		}
		if (type =='prev') {
			msgLen = msg.length;
			for (i = 0; i < msgLen; i++) {
				var curMsg = msg[i];
				if (curMsg.uid == auth.uid) {
					curMsg.type = 'me'
				} else if (curMsg.type == 'me') {
					curMsg.type = 'o'
				}
				// TODO 이전 메시지 모음이 들어올 경우
				/*
				$("#chatDiv").append(temp_chatmsg(curMsg));
				$("#chatDiv").scrollTop($("#chatDiv")[0].scrollHeight);*/
			}
		}
	}
}