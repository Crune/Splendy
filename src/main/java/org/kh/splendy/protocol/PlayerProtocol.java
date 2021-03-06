package org.kh.splendy.protocol;

import org.kh.splendy.config.assist.ProtocolHelper;
import org.kh.splendy.service.PlayerService;
import org.kh.splendy.vo.UserCore;
import org.kh.splendy.vo.WSPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlayerProtocol extends ProtocolHelper {

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(PlayerProtocol.class);

    @Autowired PlayerService plServ;

	@MessageMapping("/player/prev/{rid}")
	public void readPlayers(SimpMessageHeaderAccessor head, @DestinationVariable int rid) throws Exception {
        UserCore sender = sender(head);
		List<WSPlayer> pls = plServ.readList(rid);
		sock.send(sender, "player", "prev", pls);
	}

	@MessageMapping("/player/join/{rid}")
	public void inRoom(SimpMessageHeaderAccessor head, String password, @DestinationVariable int rid) throws Exception {
		UserCore sender = sender(head);
		// 입장을 신청한다.
        plServ.join(sender.getId(), rid, password, false);
	}

	@MessageMapping("/player/left")
	public void outRoom(SimpMessageHeaderAccessor head) throws Exception {
		UserCore sender = sender(head);
		// 퇴장을 신청한다.
        plServ.left(sender.getId());
	}
}