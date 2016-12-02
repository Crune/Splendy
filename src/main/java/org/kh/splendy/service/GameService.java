package org.kh.splendy.service;

import org.kh.splendy.vo.GameRoom;
import org.kh.splendy.vo.UserProfile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface GameService {

    GameRoom getRoom(int rid);

    GameRoom initRoom(int rid);

    void refreshPlayers(int rid);
    void refreshComponents(int rid);

    @Transactional
    void endingGame(int rid);
}
