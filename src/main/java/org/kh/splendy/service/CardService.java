package org.kh.splendy.service;

import java.util.List;

import org.kh.splendy.vo.Card;

public interface CardService {

/*	void getAll() throws Exception;
	
	Card get(int id) throws Exception;
	*/
	
	public List<Card> getLevel_1() throws Exception;
	public List<Card> getLevel_2() throws Exception;
	public List<Card> getLevel_3() throws Exception;
	public List<Card> getLevel_noble() throws Exception;
	public List<Card> suffleDeck(List<Card> deck) throws Exception;
	public List<Card> parseCard(List<Card> deck) throws Exception;
}
