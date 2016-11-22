package org.kh.splendy.task;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import org.kh.splendy.mapper.*;
import org.kh.splendy.service.*;
import org.kh.splendy.vo.*;

import lombok.*;

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

	@Autowired private StreamService stream;

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
					roomMap.delete(curRoom.getId());
				}
			}
		}
		trashRooms = rooms;
	}
	private void refreshConnector() {
		stream.refreshConnector();
	}
	
	// http://kanetami.tistory.com/entry/Schedule-Spring-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8A%A4%EC%BC%80%EC%A5%B4-%EC%84%A4%EC%A0%95%EB%B2%95-CronTab
	@Scheduled(fixedRate = 1000 * 60)
	public void checker() {
		clearRoom();
		refreshConnector();
	}

}
