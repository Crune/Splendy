package org.kh.splendy.protocol;

import java.util.List;

import org.kh.splendy.assist.ProtocolHelper;
import org.kh.splendy.vo.Player;
import org.kh.splendy.vo.Room;
import org.kh.splendy.vo.UserCore;
import org.kh.splendy.vo.WSMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
public class RoomProtocol extends ProtocolHelper {

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(RoomProtocol.class);

	@MessageMapping("/room/prev")
	public void readRoom(SimpMessageHeaderAccessor head, int rid) throws Exception {
		UserCore sender = sender(head);
		
		List<Room> rooms = roomMap.getCurrentRooms();
		if (!rooms.isEmpty()) 
		for (Room cur : rooms) {
			if (cur.getId() != 0 && cur.getPassword() != null) {
				if (!cur.getPassword().isEmpty()) {
					cur.setPassword("true");
				}
			}
		}
		
		WSMsg msg = new WSMsg();
		msg.setType("prev");
		msg.setCont(rooms);
		
		send("/room/private/" + sender.getId() , msg);
	}
		
	@MessageMapping("/room/new")
	public void createRoom(SimpMessageHeaderAccessor head, Room reqRoom) throws Exception {
		UserCore user = sender(head);

		int rst = -1;
		
		reqRoom.setHost(user.getId());
		
		boolean isTitle = !reqRoom.getTitle().isEmpty();
		boolean isInfo = !reqRoom.getInfo().isEmpty();
		boolean isPlLimit = (reqRoom.getPlayerLimits() >= 2 && reqRoom.getPlayerLimits() <= 4);
		
		boolean isNotHaveRoom = true;
		for (Room cur : roomMap.getCurrentRooms()) {
			if (cur.getHost() == user.getId()) {
				isNotHaveRoom = false;
			}
		}
		
		if (isTitle && isInfo && isPlLimit && isNotHaveRoom) {
			roomMap.create(reqRoom);
			rst = roomMap.getMyRoom(user.getId());

			Room room = roomMap.read(rst);
			if (!room.getPassword().isEmpty()) {
				room.setPassword("true");
			}
			send("/room/new", room);

			// 허가 메시지 작성
			WSMsg msg = new WSMsg();
			msg.setType("accept");
			msg.setCont(room.getId());
			
			// 허가 메시지 전송
			send("/room/private/" + user.getId() , msg);
		}
	}

	//@MessageMapping("/room/remove")
	public void deleteRoom(int rid) throws Exception {
		int roomid = -1;

		roomMap.close(rid);
		
		send("/room/remove", roomid);
	}

	@MessageMapping("/room/join/{rid}")
	public boolean inRoom(SimpMessageHeaderAccessor head, String password, @DestinationVariable int rid) throws Exception {
		UserCore sender = sender(head);
		int uid = sender.getId();		
		String pw = new Gson().fromJson(password, String.class);
		log.info("canjoin0:"+password);
		boolean canJoin = false;
		
		// 비밀번호가 일치하거나 없을 경우만 참가가능
		Room reqRoom = roomMap.read(rid);
		if (reqRoom.getPassword() == null) {
			canJoin = true;
		} else if (reqRoom.getPassword().equals(pw)) {
			canJoin = true;
		}
		log.info("canjoin1:"+canJoin);
		

		// 인원제한 보다 참가자 수가 적을 경우만 참가가능
		int countLimits = playerMap.getInRoomPlayerByRid(rid).size();
		canJoin = (canJoin && reqRoom.getPlayerLimits() > countLimits)?true:false;
		log.info("canjoin2:"+canJoin);
		
		// 참가하고 있는 방이 없을 경우만 참가가능
		int countIsIn = playerMap.countIsIn(uid);
		canJoin = (canJoin && countIsIn == 0)?true:false;
		log.info("canjoin3:"+canJoin);
		
		
		if (canJoin) {
			// DB에 접속 정보 입력
			Player player = null;
			if (playerMap.count(uid, rid) == 0) {
				player = new Player();
				player.setId(uid);
				player.setRoom(rid);
				player.setIsIn(1);
				player.setIp("");
				playerMap.create(player);
			} else {
				player = playerMap.read(uid, rid);
				player.setIp("");
				player.setIsIn(1);
				playerMap.update(player);
			}
			
			// 로비 접속 불가능 설정
			playerMap.setIsIn(uid, 0, 0);
			profMap.setLastRoom(uid, rid);
			
			// 허가 메시지 작성
			WSMsg msg = new WSMsg();
			msg.setType("accept");
			msg.setCont(rid);
			
			// 허가 메시지 전송
			send("/room/private/" + sender.getId() , msg);
		}
		return canJoin;
	}
	
	@MessageMapping("/room/left")
	public void outRoom(SimpMessageHeaderAccessor head) {
		UserCore sender = sender(head);
		
		log.info("나가기테스트");
		int uid = sender.getId();
		int rid = profMap.getLastRoom(uid);
		
		if (uid>0 && rid>0) {
			playerMap.setIsIn(uid, rid, 0);
			profMap.setLastRoom(uid, 0);
			
			List<Integer> notEmpty = roomMap.getNotEmptyRoom();
			if (!notEmpty.contains(rid)) {
				roomMap.close(rid);
				Room tempRoom = new Room();
				send("/room/remove", rid);
			}

			// 허가 메시지 작성
			WSMsg msg = new WSMsg();
			msg.setType("can_left");
			msg.setCont(rid);
			
			// 허가 메시지 전송
			send("/room/private/" + sender.getId(), msg );
		}
	}
}