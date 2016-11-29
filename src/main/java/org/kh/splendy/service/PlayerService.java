package org.kh.splendy.service;

import org.kh.splendy.vo.UserCore;
import org.kh.splendy.vo.WSPlayer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlayerService {
    int getLastRoomAndInit(int uid);

    List<WSPlayer> readList(int rid);

    void connect(int uid, int rid);

    void disconnect(int uid, int rid);

    @Transactional
    void join(int uid, int rid, String password, boolean isInitial);

    void left(UserCore sender);

    @Transactional
    void left(int uid);

    @Transactional
    void left(int uid, int reqRid);
}
