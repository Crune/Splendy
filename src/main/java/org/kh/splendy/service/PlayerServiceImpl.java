package org.kh.splendy.service;

import com.google.gson.Gson;
import org.kh.splendy.mapper.*;
import org.kh.splendy.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        List<WSPlayer> players = null;
        if (rid > 0) {
            players = playerMap.getInRoomPlayerByRid(rid);
        } else {
            players = playerMap.getAllWSPlayer();
        }
        players.forEach(pl -> pl.CanSend());
        return players;
    }

    @Override @Transactional
    public WSPlayer join(int uid, int rid, String password) {
        WSPlayer rst = null;
        rst = playerMap.getWSPlayer(uid);
        if (rid == 0) {
            sock.send("/player/join/" + rid, rst);
        } else {
            String pw = new Gson().fromJson(password, String.class);
            boolean canJoin = false;

            // 비밀번호가 일치하거나 없을 경우만 참가가능
            Room reqRoom = roomMap.read(rid);
            if (reqRoom.getPassword() == null) {
                canJoin = true;
            } else if (reqRoom.getPassword().equals(pw)) {
                canJoin = true;
            }

            // 인원제한 보다 참가자 수가 적을 경우만 참가가능
            int countLimits = playerMap.getInRoomPlayerByRid(rid).size();
            canJoin = (canJoin && reqRoom.getPlayerLimits() > countLimits);

            // 참가하고 있는 방이 없을 경우만 참가가능
            int countIsIn = playerMap.countIsIn(uid);
            canJoin = (canJoin && countIsIn == 0);

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

                boolean joinRst = game.joinPro(rid, uid);
                int innerPlsCount = game.getRoom(rid).getPls().size();
                if (joinRst && innerPlsCount == game.getRoom(rid).getLimit()) {
                    // 게임 시작!
                    sock.sendRoom(rid, "start", "게임 시작!");
                    int nextActor = game.getRoom(rid).nextActor(sock);
                    sock.sendRoom(rid, "actor", nextActor);
                }

                sock.send(uid, "room", "accept", rid);
                sock.send("/player/join/" + rid, rst);
            }
        }
        return rst;
    }

    @Override
    public WSPlayer left(UserCore sender) {
        int uid = sender.getId();
        return left(uid);
    }

    @Override @Transactional
    public WSPlayer left(int uid) {
        int rid = profMap.getLastRoom(uid);

        WSPlayer rst = playerMap.getWSPlayer(uid);
        if (rid == 0) {
            sock.send("/player/left/"+rid, rst);
        } else if (uid>0 && rid>0) {
            playerMap.setIsIn(uid, rid, 0);
            playerMap.setIsIn(uid, 0, 1);
            profMap.setLastRoom(uid, 0);

            List<Integer> notEmpty = roomMap.getNotEmptyRoom();
            if (!notEmpty.contains(rid)) {
                roomMap.close(rid);
                roomServ.deleteRoom(rid);
            }

            game.leftPro(rid, uid);
            sock.send(uid, "room", "can_left", rid );
            sock.send("/player/left/"+rid, rst);
        }
        return rst;
    }
}
