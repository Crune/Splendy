/**
 * 
 */
function onPlayer(type, pl) {
	if (type=='init') {
		console.log("Player initialized!");
		$(".player").detach();
	} else {
		if (type=='enter' && pl.room == rid) {
			console.log('enter! '+pl.nick);
			// TODO 사용자가 들어왔을 경우
		}
		if (type=='leave' && pl.room == rid) {
			console.log('leave! '+pl.nick);
			// TODO 사용자가 나갔을 경우
		}
	}
}