package org.kh.splendy.service;

import org.kh.splendy.config.assist.Utils;
import org.kh.splendy.mapper.PlayerMapper;
import org.kh.splendy.mapper.UserInnerMapper;
import org.kh.splendy.vo.Player;
import org.kh.splendy.vo.UserCore;
import org.kh.splendy.vo.WSMsg;
import org.kh.splendy.vo.WSPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@EnableTransactionManagement
public class SocketServiceImpl implements  SocketService {

    @Autowired private SimpMessagingTemplate simpT;

    @Autowired private PlayerMapper playerMap;
    @Autowired private UserInnerMapper innerMap;

    private Logger log = LoggerFactory.getLogger(SocketServiceImpl.class);

    private static final Map<Integer, UserCore> connectors = new HashMap<>();

    @Override
    public void putConnectors(UserCore user) {
        if (user.getId() > 0 && connectors.get(user.getId()) == null) {
            connectors.put(user.getId(), user);

            innerMap.setConnect(user.getId(), 1);
            WSPlayer pl = playerMap.getWSPlayer(user.getId());
            send("/player/join/" + pl.getRoom(), pl);
        }
    }

    @Override
    public void removeConnectors(UserCore user) {
        if (user.getId() > 0 && connectors.get(user.getId()) != null) {
            connectors.remove(user.getId());

            WSPlayer pl = playerMap.getWSPlayer(user.getId());
            innerMap.setConnect(user.getId(), 0);
            send("/player/left/", pl);
        }
    }

    @Override
    public Map<Integer, UserCore> getConnectors() {
        return connectors;
    }

    @Override
    public MessageHeaders createHeaders(String sessionId, SimpMessageType type) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(type);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

    @Override
    public void kick(String sessionId, String channel) {
        simpT.convertAndSendToUser(sessionId, channel, "kicked!", createHeaders(sessionId, SimpMessageType.DISCONNECT));
    }

    @Override
    public UserCore sender(SimpMessageHeaderAccessor head) {
        return (UserCore) head.getSessionAttributes().get("user");
    }

    @Override
    public void send(String type, Object obj) {
        if (type != null) {
            if (obj == null) {
                simpT.convertAndSend(type , "");
                log.info("send_msg: "+type);
            } else {
                simpT.convertAndSend(type , obj);
                log.info("send_msg: "+type+"/"+obj);
            }
        }
    }

    @Override
    public void sendRoom(int rid, String type, Object cont) {
        send("/room/event/"+rid, new WSMsg(type, cont));
    }

    @Override
    public void send(UserCore sender, String topic, String type, Object obj) {
        send(sender.getId(), topic, type, obj);
    }

    @Override
    public void send(int uid, String topic, String type, Object obj) {

        // 허가 메시지 작성
        WSMsg msg = new WSMsg();
        msg.setType(type);
        msg.setCont(obj);

        send("/"+topic+"/private/"+uid , msg);
    }

    /** 현재 시간을 문자열로 반환
     * @return 현재 시간의 HH:mm:ss 형식의 문자열 반환 */
    @Override
    public String getCurrentTime() {
        return Utils.getCurrentTime();
    }

    @Override
    public Object copy(Object obj) {
        Object rst = null;
        try {
            rst = Utils.copy(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rst;
    }

    @Override
    public void inject(Object target, Object org) {
        try {
            Utils.inject(target, org);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
