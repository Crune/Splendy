package org.kh.splendy.service;

import java.util.List;

import org.kh.splendy.vo.Article;
import org.kh.splendy.vo.Auth;
import org.kh.splendy.vo.Room;
import org.kh.splendy.vo.UserCore;

public interface LobbyService {

	List<Room> getList();

	UserCore initPlayer(UserCore user);

	Auth getAuth(int uid);

	int createRoom(Room reqRoom, UserCore user);


}
