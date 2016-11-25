package org.kh.splendy.service;

import java.util.List;

import org.kh.splendy.vo.Article;
import org.kh.splendy.vo.Auth;
import org.kh.splendy.vo.Room;
import org.kh.splendy.vo.UserCore;

public interface LobbyService {

	List<Room> getList();

	Auth getAuth(int uid);

	int createRoom(Room reqRoom, UserCore user);

	int getLastRoom(int uid);

	UserCore initPlayer(UserCore inUser, int rid);

	void join(String sId, String msg);

	void request(String sId, String msg);

	void left(String sId, String msg);


}
