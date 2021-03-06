package org.kh.splendy.task;

import org.kh.splendy.mapper.MsgMapper;
import org.kh.splendy.mapper.PlayerMapper;
import org.kh.splendy.mapper.RoomMapper;
import org.kh.splendy.mapper.UserMapper;
import org.kh.splendy.vo.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	private static List<Room> trashRooms = new ArrayList<Room>();

	private static Map<String, Method> schedule = new HashMap<String, Method>();

	@Autowired private RoomMapper roomMap;
	@Autowired private PlayerMapper playerMap;
	@Autowired private UserMapper userMap;
	@Autowired private MsgMapper msgMap;

	private void clearRoom() {
		List<Room> rooms = roomMap.getCurrentRooms();
		List<Integer> notEmpty = roomMap.getNotEmptyRoom();

		for (Room curRoom : rooms) {
			boolean isContain = notEmpty.contains(curRoom.getId());
			boolean isLobby = curRoom.getId() == 0;
			if (isContain || isLobby) {
				rooms.remove(curRoom);
			}
		}
		
		for (Room curTRoom : trashRooms) {
			for (Room curRoom : rooms) {
				if (curRoom.getId() == curTRoom.getId()) {
					rooms.remove(curRoom);
					//roomMap.delete(curRoom.getId());
					roomMap.close(curRoom.getId());
				}
			}
		}
		trashRooms = rooms;
	}
	
	// http://kanetami.tistory.com/entry/Schedule-Spring-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8A%A4%EC%BC%80%EC%A5%B4-%EC%84%A4%EC%A0%95%EB%B2%95-CronTab
	//@Scheduled(fixedRate = 1000 * 60 * 60)
	public void checker() {
		log.info("빈방 제거 및 접속자 갱신 시작");
		clearRoom();
		log.info("빈방 제거 및 접속자 갱신 종료");
	}

}
