package org.kh.splendy.service;

import java.util.*;
import org.kh.splendy.vo.*;

public interface CompService {

	List<PLCoin> getNewCoins(int rid, int uid);
	List<PLCard> getNewDeck(int rid);

	void initCompDB(int rid);
	
	Card getCard(int cid);
	Coin getCoin(int cid);

}
