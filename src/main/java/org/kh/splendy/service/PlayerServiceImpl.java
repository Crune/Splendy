package org.kh.splendy.service;

import com.google.gson.Gson;
import org.apache.commons.lang3.RandomStringUtils;
import org.kh.splendy.mapper.*;
import org.kh.splendy.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by runec on 2016-11-27.
 */
@Service
@EnableTransactionManagement
public class PlayerServiceImpl implements PlayerService {

    @Autowired private RoomMapper roomMap;
    @Autowired private PlayerMapper playerMap;
    @Autowired private UserInnerMapper innerMap;
    @Autowired private UserMapper userMap;
    @Autowired private UserProfileMapper profMap;

    @Autowired private RoomService roomServ;
    @Autowired private SocketService sock;
    @Autowired private GameService game;

    private static final Logger log = LoggerFactory.getLogger(PlayerServiceImpl.class);

    @Override
    public int getLastRoomAndInit(int uid) {
        int rid = 0;
        if (uid > 0) {
            rid = profMap.getLastRoom(uid);
            // 플레이어 기본정보 생성 여부 확인 및 생성
            sock.initPlayer(uid, rid);
        }
        return rid;
    }

    @Override
    public List<WSPlayer> readList(int rid) {
        List<WSPlayer> players = playerMap.getInRoomPlayerByRid(rid);
        players.forEach(pl->pl.CanSend());
        return players;
    }

    @Override @Transactional
    public boolean join(int uid, int rid, String password) {

        String pw = new Gson().fromJson(password, String.class);
        log.info("canjoin0:"+password);
        boolean canJoin = false;

        // 비밀번호가 일치하거나 없을 경우만 참가가능
        Room reqRoom = roomMap.read(rid);
        if (reqRoom.getPassword() == null) {
            canJoin = true;
        } else if (reqRoom.getPassword().equals(pw)) {
            canJoin = true;
        }
        log.info("canjoin1:"+canJoin);


        // 인원제한 보다 참가자 수가 적을 경우만 참가가능
        int countLimits = playerMap.getInRoomPlayerByRid(rid).size();
        canJoin = (canJoin && reqRoom.getPlayerLimits() > countLimits);
        log.info("canjoin2:"+canJoin);

        // 참가하고 있는 방이 없을 경우만 참가가능
        int countIsIn = playerMap.countIsIn(uid);
        canJoin = (canJoin && countIsIn == 0);
        log.info("canjoin3:"+canJoin);


        if (canJoin) {
            // DB에 접속 정보 입력
            Player player = null;
            if (playerMap.count(uid, rid) == 0) {
                player = new Player();
                player.setId(uid);
                player.setRoom(rid);
                player.setIsIn(1);
                player.setIp("");
                playerMap.create(player);
            } else {
                player = playerMap.read(uid, rid);
                player.setIp("");
                player.setIsIn(1);
                playerMap.update(player);
            }

            // 로비 접속 불가능 설정
            playerMap.setIsIn(uid, 0, 0);
            profMap.setLastRoom(uid, rid);

            game.joinPro(rid, uid);
            sock.send(uid, "room", "accept", rid);
        }
        return canJoin;
    }

    @Override
    public void left(UserCore sender) {
        int uid = sender.getId();
        left(uid);
    }

    @Override @Transactional
    public void left(int uid) {
        int rid = profMap.getLastRoom(uid);

        if (uid>0 && rid>0) {
            playerMap.setIsIn(uid, rid, 0);
            profMap.setLastRoom(uid, 0);

            List<Integer> notEmpty = roomMap.getNotEmptyRoom();
            if (!notEmpty.contains(rid)) {
                roomMap.close(rid);
                roomServ.deleteRoom(rid);
            }

            game.leftPro(rid, uid);
            sock.send(uid, "room", "can_left", rid );
        }
    }
}
