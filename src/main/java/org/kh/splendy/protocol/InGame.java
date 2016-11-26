package org.kh.splendy.protocol;

import java.util.*;
import org.kh.splendy.vo.*;
import org.kh.splendy.service.*;
import org.kh.splendy.assist.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class InGame extends ProtocolHelper {

	private static final Logger log = LoggerFactory.getLogger(InGame.class);

	@Autowired private CompService compServ;
	
	private static Map<Integer, GameRoom> rooms = new HashMap<Integer, GameRoom>();
	public Map<Integer, GameRoom> getRooms() { return rooms; }

	public void initRoom(int rid) {
		Room reqRoom = roomMap.read(rid);
		GameRoom room = new GameRoom();
		room.setRoom(rid);
		room.setLimit(reqRoom.getPlayerLimits());
		
		room.getCards().addAll(compServ.getNewDeck(rid));
		room.getCoins().addAll(compServ.getNewCoins(rid, 0));
		
		room.setCurrentPl(0);
		room.setTurn(0);
		
		compServ.initCompDB(rid);
		
		rooms.put(room.getRoom(), room);
	}
	
	@WSReqeust("reqRoom")
	public void reqRoom(String sId, String msg) {
		WSPlayer pl = stream.getWsplayers().get(sId);
		int rid = pl.getRoom();
		if (!rooms.containsKey(rid)) {
			initRoom(rid);
		}
		stream.send(sId, "room.init", rooms.get(pl.getRoom()));
	}

	@WSReqeust("reqCards")
	public void reqCards(String sId, String msg) {
		// 윤.게임: 카드 세부 정보를 요청하였을 경우
		List<Card> cards = compServ.getCards();
		stream.send(sId, "comp.cards", cards);
	}

	/** TODO 윤.게임: 카드를 가져오겠다고 하는 경우 */
	@WSReqeust("reqPickCard")
	public void reqPickCard(String sId, String msg) {
		PLCard card = new Gson().fromJson(msg, PLCard.class);
	}

	/** TODO 윤.게임: 코인들을 가져오겠다고 하는 경우 */
	@WSReqeust("reqPickCoin")
	public void reqPickCoin(String sId, String msg) {
		PLCoin[] coins = new Gson().fromJson(msg, PLCoin[].class);
	}
	
	public void notiAuth(WSPlayer pl) {
		int rid = pl.getRoom();
		if (!rooms.containsKey(rid)) {
			initRoom(rid);
		}
		GameRoom room = rooms.get(rid);
		boolean isIn = false;
		for (WSPlayer inRoomPl : room.getPls()) {
			if (inRoomPl.getUid() == pl.getUid()) {
				isIn = true;
			}
		}
		if (!isIn) {
			room.getPls().add(pl);
			if (room.getLimit() <= room.getPls().size() && room.getTurn() == 0) {
				room.setTurn(1);
				sendRoom(rid, "room.init", rooms.get(pl.getRoom()));
			}
		}
	}

	public void notiOut(WSPlayer pl) {
		int rid = pl.getRoom();
		if (!rooms.containsKey(rid)) {
			initRoom(rid);
		}
		GameRoom room = rooms.get(rid);
		boolean isIn = false;
		if (room.getPls().size()>1) {
			for (WSPlayer inRoomPl : room.getPls()) {
				if (inRoomPl.getUid() == pl.getUid()) {
					room.getPls().remove(inRoomPl);
					isIn = true;
				}
			}
			if (!isIn) {
				if (room.getLimit() != room.getPls().size()) {
					room.setTurn(-1);
					GameRoom end = new GameRoom();
					end.setRoom(rid);
					end.setTurn(-1);
					sendRoom(rid, "room.end", end);
				}
			}
		}
	}
	
	public void drawGame(int rid, int uid) {
		
	}
	
	public void sendRoom(int rid, String type, Object Msg) {
		for (WSPlayer inRoomPl : rooms.get(rid).getPls()) {
			String sid = stream.findSid(inRoomPl.getUid());
			stream.send(sid, type, Msg);
		}
	}
}
