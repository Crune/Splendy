package org.kh.splendy.service;

import java.util.List;

import org.kh.splendy.vo.Article;
import org.kh.splendy.vo.Auth;
import org.kh.splendy.vo.Room;

public interface LobbyService {

	List<Room> getList();

	void initPlayer(int uid);

	Auth getAuth(int uid);

}
