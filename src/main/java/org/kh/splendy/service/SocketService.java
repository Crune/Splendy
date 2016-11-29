package org.kh.splendy.service;

import org.kh.splendy.vo.UserCore;
import org.kh.splendy.vo.WSPlayer;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by runec on 2016-11-27.
 */
public interface SocketService {
    void putConnectors(WSPlayer uid);

    void removeConnectors(WSPlayer uid);

    Map<Integer, WSPlayer> getConnectors();

    MessageHeaders createHeaders(String sessionId, SimpMessageType type);

    void kick(String sessionId, String channel);

    UserCore sender(SimpMessageHeaderAccessor head);

    void send(String type, Object obj);

    void send(int uid, String topic, String type, Object obj);

    String getCurrentTime();

    Object copy(Object obj)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;

    void inject(Object target, Object org) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;

    void sendRoom(int rid, String type, Object cont);

    void send(UserCore sender, String topic, String type, Object obj);


}
