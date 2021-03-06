package org.kh.splendy.service;

import org.kh.splendy.mapper.*;
import org.kh.splendy.vo.Room;
import org.kh.splendy.vo.UserCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@EnableTransactionManagement
public class RoomServiceImpl implements RoomService {

    @Autowired private SocketService sock;

    @Autowired private RoomMapper roomMap;
    @Autowired private MsgMapper msgMap;

    @Autowired private PlayerMapper playerMap;

    @Autowired private UserInnerMapper innerMap;
    @Autowired private UserMapper userMap;
    @Autowired private UserProfileMapper profMap;

    @Autowired private SimpMessagingTemplate simpT;

    @Override @Transactional
    public int createRoom(Room reqRoom, UserCore reqUser) {

        reqRoom.setHost(reqUser.getId());

        boolean isTitle = !reqRoom.getTitle().isEmpty();
        boolean isInfo = !reqRoom.getInfo().isEmpty();
        boolean isPlLimit = (reqRoom.getPlayerLimits() >= 2 && reqRoom.getPlayerLimits() <= 4);

        boolean isNotHaveRoom = false;
        List<Room> curRooms = roomMap.getCurrentRooms();
        if (curRooms != null) {
            boolean isTempValid = true;
            for (Room cur : curRooms) {
                if (cur.getHost() == reqUser.getId()) {
                    isTempValid = false;
                }
            }
            if (isTempValid) {
                isNotHaveRoom = true;
            }
        }

        if (isTitle && isInfo && isPlLimit && isNotHaveRoom) {
            roomMap.create(reqRoom);
            int rstRoomId = roomMap.getMyRoom(reqUser.getId());
            Room rstRoom = roomMap.read(rstRoomId);

            // 방 생성시 기본 덱과 코인을 깔음
            roomMap.createInitial(rstRoomId);

            if (!rstRoom.getPassword().isEmpty()) {
                rstRoom.setPassword("true");
            }
            profMap.setLastRoom(reqUser.getId(), rstRoomId);

            sock.send("/room/new", rstRoom);

            // 허가 메시지 전송
            sock.send(reqUser.getId(),"room","accept", rstRoom.getId());
            return rstRoom.getId();
        } else {
            int rst = 0;
            rst += isTitle?0:-1;
            rst += isInfo?0:-2;
            rst += isPlLimit?0:-4;
            rst += isNotHaveRoom?0:-8;
            return rst;
        }
    }

    @Override
    public List<Room> readList() {
        List<Room> rooms = roomMap.getCurrentRooms();
        if (!rooms.isEmpty()) {
            for (Room cur : rooms) {
                if (cur.getId() != 0 && cur.getPassword() != null) {
                    if (!cur.getPassword().isEmpty()) {
                        cur.setPassword("true");
                    }
                }
            }
        }
        return rooms;
    }

    @Override
    public void deleteRoom(int rid) {
        if (rid > 0) {
            List<Integer> notEmpty = roomMap.getNotEmptyRoom();
            if (!notEmpty.contains(rid)) {
                sock.send("/room/remove", rid);
                Room temp = roomMap.read(rid);
                if (temp != null) {
                    if (temp.getStart() == null) {
                        roomMap.delete(rid);
                    } else {
                        roomMap.close(rid);
                    }
                }
            }
        }
    }

	@Override
	public List<Room> getCurrentRooms() {
		List<Room> room = roomMap.getCurrentRooms();
		return room;
	}

	@Override
	public void close(int rid) {
        if (roomMap.read(rid).getStart() == null) {
            roomMap.delete(rid);
        } else {
            roomMap.close(rid);
        }
	}
}
