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
		}
		if (type=='leave' && pl.room == rid) {
			console.log('leave! '+pl.nick);
		}
	}
}