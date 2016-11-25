/**
 * 
 */

function onComp(type, comp) {
	if (type =='init') {
		console.log("Components initialized!");
	} else {
		if (type =='cards') {
			compLen = comp.length;
			for (i = 0; i < compLen; i++) {
				var curComp = comp[i];
				// TODO 카드 목록이 들어올 경우
				
			}
			// 완료 후 방 기본정보 요청
			wssend('reqRoom', rid);
		}
	}
}