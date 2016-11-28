package org.kh.splendy.service;

import org.kh.splendy.vo.GameRoom;

import java.util.Map;

/**
 * Created by runec on 2016-11-27.
 */
public interface GameService {
    Map<Integer, GameRoom> getRooms();

    GameRoom getRoom(int rid);

    GameRoom readRoom(int rid);

    void joinPro(int rid, int uid);

    void leftPro(int rid, int uid);
}
