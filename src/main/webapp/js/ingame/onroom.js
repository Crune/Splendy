/**
 * 게임방 초기 정보가 전달될 경우 수행할 메서드  
 */

function onRoom(type, room) {
	if (type=='init') {
		console.log("Room initialized!");
		console.log("Room: "+room);
		
		// TODO 게임이 진행중일 경우
		if (room.turn > 0) {
			
		} else {
			
		}
		// TODO 카드 세팅
		
		// TODO 코인 세팅
		
		// TODO 플레이어 세팅
		
		// 사용 가능하게 설정
		setControll(true);
		
	} else {
		if (type=='remove' && room.id == rid) {
			isPageMove = true;
			location.replace("/lobby/");
		}
		if (type=='halt' && room.id == rid) {
			// TODO 누군가 탈주 하였을 경우 5분 대기
		}
		if (type=='resume' && room.id == rid) {
			// TODO 누군가 복귀 하였을 경우 재 실행
		}
		if (type=='end' && room.id == rid) {
			// TODO 탈주자는 복귀하지 않았다...
		}
	}
}