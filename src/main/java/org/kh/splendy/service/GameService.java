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

    GameRoom initRoom(int rid);

    void refreshPlayers(int rid);

    void refreshComponents(int rid);

    GameRoom readRoom(int rid);

    List<UserProfile> endingGame(int rid, Map<Integer, Integer> score);
}
