package org.kh.splendy.service;

import org.kh.splendy.config.assist.Utils;
import org.kh.splendy.mapper.*;
import org.kh.splendy.vo.GameRoom;
import org.kh.splendy.vo.Room;
import org.kh.splendy.vo.WSPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Override public GameRoom getRoom(int rid) { return rooms.get(rid); }


    private void initRoom(int rid) {
        Room reqRoom = roomMap.read(rid);
        List<WSPlayer> players = plMap.getInRoomPlayerByRid(rid);

        GameRoom room = new GameRoom();
        room.setRoom(rid);
        room.setLimit(reqRoom.getPlayerLimits());

        room.getPls().addAll(players);

        room.getCards().addAll(comp.getNewDeck(rid));
        room.getCoins().addAll(comp.getNewCoins(rid, 0));

        room.setCurrentPl(0);
        room.setTurn(0);

        comp.initCompDB(rid);

        rooms.put(room.getRoom(), room);
    }

    @Override
    public GameRoom readRoom(int rid) {
        GameRoom room = rooms.get(rid);
        if (room == null) {
            initRoom(rid);
        }
        return room;
    }

    @Override
    public void joinPro(int rid, int uid) {
        if (roomMap.count(rid)>0) {
            WSPlayer joiner = plMap.getWSPlayer(uid).CanSend();
            boolean result = rooms.get(rid).reqJoin(joiner);
        } else {
            initRoom(rid);
        }
    }

    @Override
    public void leftPro(int rid, int uid) {
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
}
