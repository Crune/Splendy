package org.kh.splendy.service;

import java.util.List;

import org.kh.splendy.vo.Msg;
import org.kh.splendy.vo.UserCore;
import org.kh.splendy.vo.WSChat;

/**
 * Created by runec on 2016-11-27.
 */
public interface ChatService {
    void readPrev(int rid, int uid);

    WSChat writeMsg(UserCore sender, int rid, String msg);
    
    List<Msg> read_all();
    
    void delete(int id);
}
