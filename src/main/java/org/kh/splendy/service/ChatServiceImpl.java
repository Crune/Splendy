package org.kh.splendy.service;

import com.google.gson.Gson;
import org.kh.splendy.mapper.MsgMapper;
import org.kh.splendy.vo.Msg;
import org.kh.splendy.vo.UserCore;
import org.kh.splendy.vo.WSChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by runec on 2016-11-27.
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired private MsgMapper msgMap;

    @Autowired protected SocketService sock;

    @Override
    public WSChat writeMsg(UserCore sender, int rid, String msg) {

        WSChat chat = new WSChat();
        chat.setNick(sender.getNickname());
        chat.setUid(sender.getId());
        chat.setTime(sock.getCurrentTime());
        chat.setType("o");
        chat.setCont(msg);

        Msg newMsg = new Msg();
        newMsg.setRid(rid);
        newMsg.setUid(sender.getId());
        newMsg.setType("chat.new");
        newMsg.setCont(new Gson().toJson(chat));
        msgMap.create(newMsg);

        return chat;
    }

    @Override
    public void readPrev(int rid, int uid) {

        List<Msg> msgs = msgMap.readPrevChat(rid, 31);
        List<WSChat> chats = new ArrayList<WSChat>();
        for (Msg cur : msgs) {
            WSChat curMsg = WSChat.convert(cur.getCont());
            if (curMsg.getUid() == uid) {
                curMsg.setType("me");
            } else {
                curMsg.setType("o");
            }
            chats.add(curMsg);
        }

        sock.send(uid, "chat", "prev", chats);
    }

	@Override
	public List<Msg> read_all() {
		List<Msg> msg = msgMap.read_all();
		return msg;
	}

	@Override
	public void delete(int id) {
		msgMap.delete(id);
	}
}
