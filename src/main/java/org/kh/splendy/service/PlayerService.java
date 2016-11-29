package org.kh.splendy.service;

import org.kh.splendy.vo.UserCore;
import org.kh.splendy.vo.WSPlayer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by runec on 2016-11-27.
 */
public interface PlayerService {
    int getLastRoomAndInit(int uid);

    List<WSPlayer> readList(int rid);

    WSPlayer join(int uid, int rid, String password);

    WSPlayer left(UserCore sender);

    @Transactional
    WSPlayer left(int uid);
}
