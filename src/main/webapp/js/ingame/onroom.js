/**
 * 게임방 초기 정보가 전달될 경우 수행할 메서드  
 */

function onRoom(type, room) {
	if (type=='init') {
		console.log("Room initialized!");
		console.log("Room: "+room);
		setControll(true);
	} else {
		if (type=='remove' && room.id == rid) {
			isPageMove = true;
			location.replace("/lobby/");
		}
	}
}