package org.kh.splendy.service;

import org.kh.splendy.vo.Card;

public interface CardService {

	void getAll() throws Exception;
	
	Card get(int id) throws Exception;

}
