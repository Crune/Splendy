package org.kh.splendy.service;

import org.kh.splendy.config.assist.Utils;
import org.kh.splendy.mapper.*;
import org.kh.splendy.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameServiceImpl implements GameService {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(GameServiceImpl.class);

    @Autowired private CompService comp;
    @Autowired private SocketService sock;

    @Autowired private RoomMapper roomMap;
    @Autowired private PlayerMapper plMap;
    @Autowired private UserInnerMapper innerMap;
    @Autowired private UserMapper userMap;
    @Autowired private UserProfileMapper profMap;

    private static final Map<Integer, GameRoom> rooms = new HashMap<>();

    @Override public Map<Integer, GameRoom> getRooms() { return rooms; }
    @Override public GameRoom getRoom(int rid) {
        return initRoom(rid);
    }

    private GameRoom initRoom(int rid) {
        if (rooms.get(rid) == null && rid > 0) {
            Room reqRoom = roomMap.read(rid);
            if (reqRoom != null) {

                GameRoom room = new GameRoom();
                room.setRoom(rid);
                room.setLimit(reqRoom.getPlayerLimits());

                List<WSPlayer> players = plMap.getInRoomPlayerByRid(rid);
                if (players != null) {
                    room.getPls().addAll(players);
                }

                room.getCards().addAll(comp.getNewDeck(rid));
                room.getCoins().addAll(comp.getNewCoins(rid));

                room.setCurrentPl(0);
                room.setTurn(0);

                comp.initCompDB(rid);

                rooms.put(room.getRoom(), room);
            }
        }
        return rooms.get(rid);
    }

    @Override
    public GameRoom readRoom(int rid) {
        initRoom(rid);
        GameRoom room = rooms.get(rid);
        return room;
    }

    @Override
    public boolean isInPlayer(int rid, int uid) {
        boolean rst = false;
        if (getRoom(rid) != null) {
            rst = getRoom(rid).isInPlayer(uid);
        }
        return rst;
    }

    /** 게임 목록의 참가자에 없다면 참가자로 추가한다. */
    @Override
    public boolean joinPro(int rid, int uid) {
        initRoom(rid);
        WSPlayer joiner = plMap.getWSPlayer(uid).CanSend();
        boolean result = rooms.get(rid).reqJoin(joiner, sock);
        if (result) {
            startingGame(rid);
        }
        return result;
    }


    /** 게임 목록의 참가자에 있다면 해당 참가자 나가기 처리한다. */
    @Override
    public boolean leftPro(int rid, int uid) {
        initRoom(rid);
        boolean result = rooms.get(rid).reqLeft(uid);

        if (result) {
            List<WSPlayer> pls = rooms.get(rid).getPls();
            if (!pls.isEmpty()) {
                if (rooms.get(rid).isPlaying()) {
                    // 만약 게임 진행 중이라면 감점 처리한다.
                    if (!comp.checkEnding(rooms.get(rid).getCards())) {
                        int rate = profMap.read(uid).getRate() -100;
                        if (rate > 0) {
                            profMap.setRate(uid, rate - 100);
                        }
                    }
                    sock.sendRoom(rid, "halt", uid);
                    rooms.get(rid).halt();
                } else {
                    // 시작하지 않았기 때문에 해당 참가자의 정보를 제거한다.
                    for (PLCoin curCoin : rooms.get(rid).getCoins()) {
                        if (curCoin.getU_id() == uid) {
                            rooms.get(rid).getCoins().remove(curCoin);
                            plMap.delete(uid, rid);
                        }
                    }
                }
            } else {
                rooms.remove(rid);
            }
        }
        return result;
    }

    private void startingGame(int rid) {
        int innerPlsCount = rooms.get(rid).getPls().size();
        if (innerPlsCount == rooms.get(rid).getLimit()) {
            roomMap.setStart(rid, new Date(System.currentTimeMillis()));
            // 게임 시작!
            sock.sendRoom(rid, "start", "게임 시작!");
            int nextActor = rooms.get(rid).nextActor(sock);
            sock.sendRoom(rid, "actor", nextActor);
        }
    }

    @Override @Transactional
    public List<UserProfile> endingGame(int rid, Map<Integer, Integer> score) {
        initRoom(rid);
        int winner = 0, winnerScore = 0;
        for (int uid : score.keySet()) {
            int curScore = score.get(uid);
            if (winnerScore < curScore) {
                winner = uid;
                winnerScore = curScore;
            }
        }
        List<UserProfile> result = new ArrayList<>();
        if (winnerScore >= 15) {
            Room room = roomMap.read(rid);
            room.setEnd(new Date(System.currentTimeMillis()));
            room.setWinner(winner);
            roomMap.update(room);
            for (int uid : score.keySet()) {
                int rstRate = profMap.read(uid).getRate();
                if (uid == winner) {
                    rstRate += 100;
                } else {
                    rstRate -= 20;
                }
                profMap.setRate(uid, rstRate);
            }
            for (int uid : score.keySet()) {
                result.add(profMap.read(uid));
            }
        }
        return result;
    }

}
