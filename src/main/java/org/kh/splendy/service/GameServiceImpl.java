package org.kh.splendy.service;

import org.kh.splendy.mapper.*;
import org.kh.splendy.vo.GameRoom;
import org.kh.splendy.vo.Room;
import org.kh.splendy.vo.UserProfile;
import org.kh.splendy.vo.WSPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameServiceImpl implements GameService {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(GameServiceImpl.class);

    @Autowired private CompService comp;
    //@Autowired private SocketService sock;

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
                room.getCoins().addAll(comp.getNewCoins(rid, 0));

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
    public boolean joinPro(int rid, int uid) {
        initRoom(rid);

        WSPlayer joiner = plMap.getWSPlayer(uid).CanSend();
        boolean result = rooms.get(rid).reqJoin(joiner);

        return result;
    }

    @Override
    public void leftPro(int rid, int uid) {
        initRoom(rid);

        List<WSPlayer> pls = rooms.get(rid).getPls();
        pls.forEach(pl->{
            if (uid == pl.getUid()) {
                pls.remove(pl);
            }
        });
        if (pls.isEmpty()) {
            rooms.remove(rooms.get(rid));
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
        for (int uid : score.keySet()) {
            int rstRate = profMap.read(uid).getRate();
            if (uid == winner) {
                rstRate += 100;
            } else {
                rstRate -= 20;
            }
            profMap.setRate(uid, rstRate);
        }
        List<UserProfile> result = new ArrayList<>();
        for (int uid : score.keySet()) {
            result.add(profMap.read(uid));
        }
        return result;
    }

}
