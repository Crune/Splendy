package org.kh.splendy.protocol;

import org.kh.splendy.config.assist.ProtocolHelper;
import org.kh.splendy.service.CompService;
import org.kh.splendy.service.RoomService;
import org.kh.splendy.vo.Card;
import org.kh.splendy.vo.PLCard;
import org.kh.splendy.vo.PLCoin;
import org.kh.splendy.vo.UserCore;
import org.kh.splendy.vo.WSCoinRequest;
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
import java.util.Map;

@RestController
public class CompProtocol extends ProtocolHelper {

    @SuppressWarnings("unused")
    private Logger log = LoggerFactory.getLogger(CompProtocol.class);

    @Autowired private CompService compServ;

    @MessageMapping("/comp/cards")
    public void reqCards(SimpMessageHeaderAccessor head) throws Exception {
        UserCore sender = sender(head);
        List<Card> cards = compServ.getCards();
        sock.send(sender.getId(), "comp", "card", cards);
    }

    @SubscribeMapping("/comp/card/{rid}")
    @SendTo("/comp/card/{rid}")
    public List<PLCard> requestCard(SimpMessageHeaderAccessor head, PLCard reqCard, @DestinationVariable int rid) throws Exception {
        UserCore sender = sender(head);

        List<PLCard> rst = null;
        if (rid(head) == rid) {
            rst = compServ.reqPickCard(reqCard, sender.getId(), game.getRoom(rid));
            if (rst != null) {
                sock.send("/room/event/"+rid(head), game.getRoom(rid).nextActor(sock));
                Map<Integer, Integer> score = compServ.scoring(game.getRoom(rid).getCards());
            }
        }

        // 카드 홀딩시 골드 코인 정보는 'CompService'가 '/comp/coin/{rid}'의 구독자들에게 제공
        return rst;
    }

    @SubscribeMapping("/comp/coin/{rid}")
    @SendTo("/comp/coin/{rid}")
    public List<PLCoin> requestCoin(SimpMessageHeaderAccessor head, WSCoinRequest request, @DestinationVariable int rid) throws Exception {
        UserCore sender = sender(head);

        List<PLCoin> req = request.getReq();
        List<PLCoin> draw = request.getDraw();

        List<PLCoin> rst = null;

        if (rid(head) == rid) {
            rst = compServ.reqPickCoin(req, draw, sender.getId(), game.getRoom(rid));
            if (rst != null) {
                sock.send("/room/event/"+rid(head), game.getRoom(rid).nextActor(sock));
            }
        }

        return rst;
    }



}
