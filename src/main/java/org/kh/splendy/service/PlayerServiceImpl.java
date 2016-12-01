package org.kh.splendy.service;

import com.google.gson.Gson;
import org.kh.splendy.mapper.*;
import org.kh.splendy.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 동작설명
     * - 플레이어 테이블에 해당 항목이 없을 경우 생성
     * - 해당 플레이어 접속정보 설정
     */
    @Override
    public int getLastRoomAndInit(int uid) {
        int rid = 0;
        if (uid > 0) {
            rid = profMap.getLastRoom(uid);
            join(uid, rid, "", true);
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

    @Override
    public void connect(int uid, int rid) {
        WSPlayer pl = playerMap.getWSPlayer(uid);
        pl.setRoom(rid);
        sock.putConnectors(pl);
        join(uid, rid, "", true);
        log.info("웹소켓 접속: user" + uid+" / room: "+rid);
        if (rid != 0) {
            sock.send("/player/join/" + 0, pl);
        }
    }

    @Override
    public void disconnect(int uid, int rid) {
        WSPlayer pl = playerMap.getWSPlayer(uid);
        pl.setRoom(rid);
        sock.removeConnectors(pl);
        try {
            if (!checkConnected(uid)) {
                left(uid, rid);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("웹소켓 접속해제: user" + uid);
    }

    @Async
    public boolean checkConnected(int uid) throws InterruptedException {
        Thread.sleep(1000 * 30);
        if (sock.getConnectors().containsKey(uid)) {
            return true;
        } else  {
            return false;
        }
    }

    @Override @Transactional
    public void join(int uid, int rid, String password, boolean isInitial) {

        String pw = new Gson().fromJson(password, String.class);
        boolean canJoin = false;
        boolean isAlreadyPro = playerMap.count(uid, rid) > 0;

        if (isAlreadyPro || rid == 0) {
            canJoin = true;
        } else if (rid > 0) {
            // 비밀번호가 일치하거나 없을 경우만 참가가능
            Room reqRoom = roomMap.read(rid);
            if (reqRoom.getPassword() == null || isInitial) {
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

            // 방이 존재할 경우만 가능

        }

        if (canJoin) {
            // DB에 접속 정보 입력
            Player player = null;
            if (!isAlreadyPro) {
                player = new Player();
                player.setId(uid);
                player.setRoom(rid);
                player.setIsIn(1);
                player.setIp("");
                playerMap.create(player);
                if (rid > 0) {
                    playerMap.createInitial(uid, rid);
                }
            } else {
                player = playerMap.read(uid, rid);
                player.setIp("");
                player.setIsIn(1);
                playerMap.update(player);
            }

            if (rid > 0) {
                // 로비 접속 불가능 설정
                playerMap.setIsIn(uid, 0, 0);

                // 게임 내부 입장 처리
                game.initRoom(rid);

                if (!isInitial) {
                    // 계정 입장 처리
                    profMap.setLastRoom(uid, rid);

                    sock.send(uid, "room", "accept", rid);
                }
            }
        }

    }

    @Override
    public void left(UserCore sender) {
        int uid = sender.getId();
        left(uid);
    }

    @Override @Transactional
    public void left(int uid) {
        int rid = profMap.getLastRoom(uid);
        left(uid, rid);
    }

    @Override @Transactional
    public void left(int uid, int rid) {
        if (uid>0) {
            playerMap.setIsIn(uid, rid, 0);

            if (rid > 0) {
                // 계정 퇴장 처리
                playerMap.setIsIn(uid, 0, 1);
                profMap.setLastRoom(uid, 0);

                // 게임 내부 퇴장 처리
                game.initRoom(rid);

                if (playerMap.getInRoomPlayerByRid(rid).size() == 0) {
                    roomServ.deleteRoom(rid);
                    sock.send("/room/remove", rid);
                }

                sock.send(uid, "room", "can_left", rid);
            }
        }
    }
}
