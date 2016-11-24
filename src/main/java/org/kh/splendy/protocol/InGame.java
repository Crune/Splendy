package org.kh.splendy.protocol;

import java.util.*;
import org.kh.splendy.vo.*;
import org.kh.splendy.service.*;
import org.kh.splendy.assist.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InGame extends ProtocolHelper {

	private static final Logger log = LoggerFactory.getLogger(InGame.class);

	@Autowired private CompService compServ;
	
	private static Map<Integer, GameRoom> rooms = new HashMap<Integer, GameRoom>();
	public Map<Integer, GameRoom> getRooms() { return rooms; }

	public void initRoom(int rid) {
		GameRoom room = new GameRoom();
		room.setRoom(rid);
		
		room.getCards().addAll(compServ.getNewDeck(rid));
		room.getCoins().addAll(compServ.getNewCoins(rid, 0));
		
		room.setCurrentPl(0);
		room.setTurn(0);
		
		compServ.initCompDB(rid);
		
		rooms.put(room.getRoom(), room);
	}
	
	@WSReqeust("reqRoom")
	public void requestedRoom(String sId, String msg) {
		WSPlayer pl = stream.getWsplayers().get(sId);
		stream.send(sId, "room.init", rooms.get(pl.getRoom()));
	}
}
