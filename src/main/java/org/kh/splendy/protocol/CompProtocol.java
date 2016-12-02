package org.kh.splendy.protocol;

import org.kh.splendy.config.assist.ProtocolHelper;
import org.kh.splendy.service.CompService;
import org.kh.splendy.vo.Card;
import org.kh.splendy.vo.PLCard;
import org.kh.splendy.vo.PLCoin;
import org.kh.splendy.vo.UserCore;
import org.kh.splendy.vo.UserProfile;
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

    private static boolean isCompInitialized = false;

    private boolean isCanActing(int rid, int uid) {
         boolean rst = game.getRoom(rid).isMyTurn(uid) && game.getRoom(rid).isPlaying() && !game.getRoom(rid).isHalted();
         return  rst;
    }

    private void sendNext(int rid) {
        // 현재 점수를 계산
        Map<Integer, Integer> score  = compServ.scoring(rid);
        sock.sendRoom(rid, "score", score);

        // 게임 종료 여부를 계산
        boolean isGameEnd = false;
        for (int curScore : score.keySet()) {
            if (curScore >= 15) {
                isGameEnd = true;
            }
        }
        if (isGameEnd) {
            // 게임 종료 처리
            game.endingGame(rid);
        } else {
            // 다음 사람으로 넘김
            int nextActor  = game.getRoom(rid).nextActor();
            sock.sendRoom(rid, "actor", nextActor);
        }
    }

    @MessageMapping("/comp/cards")
    public void reqCards(SimpMessageHeaderAccessor head) throws Exception {
        if (!isCompInitialized) { compServ.initialize(); }
        UserCore sender = sender(head);
        List<Card> cards = compServ.getCardAll();
        sock.send(sender.getId(), "comp", "card", cards);
    }

    @MessageMapping("/comp/card/{rid}")
    public void requestCard(SimpMessageHeaderAccessor head, PLCard reqCard, @DestinationVariable int rid) throws Exception {
        if (!isCompInitialized) { compServ.initialize(); }
        UserCore sender = sender(head);

        if (rid(head) == rid && isCanActing(rid, sender.getId())) {
            if (reqCard.getCd_id() == 0) {
                compServ.holdByDeck(sender.getId(), rid, compServ.getCard(reqCard.getCd_id()).getLv());
                sendNext(rid(head));
            } else if (compServ.reqPickCard(reqCard, sender.getId(), rid)) {
                sendNext(rid(head));
            }
        }
    }

    @MessageMapping("/comp/coin/{rid}")
    public void requestCoin(SimpMessageHeaderAccessor head, WSCoinRequest request, @DestinationVariable int rid) throws Exception {
        if (!isCompInitialized) { compServ.initialize(); }
        UserCore sender = sender(head);

        List<PLCoin> req = request.getReq();
        List<PLCoin> draw = request.getDraw();

        if (rid(head) == rid && isCanActing(rid, sender.getId())) {
            if (compServ.reqPickCoin(req, draw, sender.getId(), rid)) {
                sendNext(rid(head));
            }
        }
    }

}
