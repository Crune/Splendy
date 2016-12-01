package org.kh.splendy.service;

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

    @Override
    public GameRoom initRoom(int rid) {
        comp.initialize();
        if (rooms.get(rid) == null && rid > 0) {
            Room reqRoom = roomMap.read(rid);
            if (reqRoom != null) {
                GameRoom room = new GameRoom();
                room.setRoom(rid);
                room.setLimit(reqRoom.getPlayerLimits());
                room.setCurrentPl(0);
                room.setTurn(0);
                rooms.put(room.getRoom(), room);

                refreshPlayers(rid);
                refreshComponents(rid);
            }
        }
        return rooms.get(rid);
    }

    @Override
    public void refreshPlayers(int rid) {
        List<WSPlayer> joinPlayers = new ArrayList<>();
        List<WSPlayer> leftPlayers = new ArrayList<>();

        // 기존 참가자 목록 획득
        List<WSPlayer> prevPlayers = rooms.get(rid).getPls();
        List<Integer> prevUids = new ArrayList<>();
        for (WSPlayer cur : prevPlayers) {
            prevUids.add(cur.getUid());
        }
        // 신규 참가자 목록 획득
        List<WSPlayer> newPlayers = plMap.getInRoomPlayerByRid(rid);
        List<Integer> newUids = new ArrayList<>();
        for (WSPlayer cur : newPlayers) {
            newUids.add(cur.getUid());
        }

        // 입장 및 퇴장 참가자 산정
        for (WSPlayer cur : prevPlayers) {
            if (!newUids.contains(cur.getUid())) {
                leftPlayers.add(cur);
            }
        }
        for (WSPlayer cur : newPlayers) {
            if (!prevUids.contains(cur.getUid())) {
                joinPlayers.add(cur);
            }
        }

        // 참가자 목록 갱신 및 처리

        rooms.get(rid).setPls(newPlayers);

        for (WSPlayer cur : joinPlayers) {

        }

        for (WSPlayer cur : leftPlayers) {
            int uid = cur.getUid();
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
            }
        }

        // 인원수가 충족되면 게임 시작!
        if (rooms.get(rid).getLimit() == newPlayers.size()) {
            roomMap.setStart(rid, new Date(System.currentTimeMillis()));
            sock.sendRoom(rid, "start", "게임 시작!");
            int nextActor = rooms.get(rid).nextActor();
            sock.sendRoom(rid, "actor", nextActor);
        }
    }

    @Override
    public void refreshComponents(int rid) {
        rooms.get(rid).getCards().addAll(comp.getCardsInDB(rid));
        rooms.get(rid).getCoins().addAll(comp.getCoinsInDB(rid));
    }

    @Override
    public GameRoom readRoom(int rid) {
        initRoom(rid);
        GameRoom room = rooms.get(rid);
        return room;
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
