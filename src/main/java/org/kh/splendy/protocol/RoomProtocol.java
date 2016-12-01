package org.kh.splendy.protocol;

import org.kh.splendy.config.assist.ProtocolHelper;
import org.kh.splendy.service.GameService;
import org.kh.splendy.service.RoomService;
import org.kh.splendy.vo.GameRoom;
import org.kh.splendy.vo.Room;
import org.kh.splendy.vo.UserCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoomProtocol extends ProtocolHelper {

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(RoomProtocol.class);

	@Autowired RoomService roomServ;

    @MessageMapping("/room/new")
    public void createRoom(SimpMessageHeaderAccessor head, Room reqRoom) throws Exception {
        UserCore sender = sender(head);
        roomServ.createRoom(reqRoom, sender);
    }

	@MessageMapping("/room/prev")
	public void readRoom(SimpMessageHeaderAccessor head, int rid) throws Exception {
		UserCore sender = sender(head);
        List<Room> rooms = roomServ.readList();
        if (rid > 1 && rid(head) == rid) {
            GameRoom room = game.getRoom(rid);
            sock.send(sender, "room", "current", room);
        } else {
            sock.send(sender, "room", "prev", rooms);
        }
	}

    @MessageMapping("/room/event/{rid}")
    public void roomEvent(SimpMessageHeaderAccessor head, @DestinationVariable int rid) throws Exception {
        UserCore sender = sender(head);
        //
    }
}