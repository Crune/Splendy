package org.kh.splendy.service;

import org.kh.splendy.vo.GameRoom;
import org.kh.splendy.vo.UserProfile;

import java.util.List;
import java.util.Map;

/**
 * Created by runec on 2016-11-27.
 */
public interface GameService {
    Map<Integer, GameRoom> getRooms();

    GameRoom getRoom(int rid);

    GameRoom readRoom(int rid);

    boolean joinPro(int rid, int uid);

    boolean leftPro(int rid, int uid);

    List<UserProfile> endingGame(int rid, Map<Integer, Integer> score);
}
