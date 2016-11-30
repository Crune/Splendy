package org.kh.splendy.service;

import org.kh.splendy.vo.Room;
import org.kh.splendy.vo.UserCore;

import java.util.List;

/**
 * Created by runec on 2016-11-27.
 */
public interface RoomService {
    int createRoom(Room reqRoom, UserCore user);

    void deleteRoom(int rid);

    List<Room> readList();
    
    List<Room> getCurrentRooms();
    
    void close(int id);
}
