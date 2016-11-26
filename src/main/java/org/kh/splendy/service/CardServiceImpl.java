package org.kh.splendy.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kh.splendy.mapper.*;
import org.kh.splendy.vo.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Service
@EnableTransactionManagement
public class CardServiceImpl implements CardService {

	@Autowired
	private CardMapper cardMap;
	private static List<Card> level_1 = null;
	private static List<Card> level_2 = null;
	private static List<Card> level_3 = null;
	private static List<Card> level_noble = null;
	
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);
	
	@Override
	public List<Card> getLevel_1() throws Exception{
		level_1 = cardMap.selectLevel_1();
		level_1 = parseCard(level_1);
		level_1 = suffleDeck(level_1);
		return level_1;
	}

	@Override
	public List<Card> getLevel_2() throws Exception {
		level_2 = cardMap.selectLevel_2();
		level_2 = parseCard(level_2);
		level_2 = suffleDeck(level_2);
		return level_2;
	}

	@Override
	public List<Card> getLevel_3() throws Exception {
		level_3 = cardMap.selectLevel_3();
		level_3 = parseCard(level_3);
		level_3 = suffleDeck(level_3);
		return level_3;
	}

	@Override
	public List<Card> getLevel_noble() throws Exception {
		level_noble = cardMap.selectLevel_noble();
		level_noble = parseCard(level_noble);
		level_noble = suffleDeck(level_noble);
		return level_noble;
	}

	@Override
	public List<Card> suffleDeck(List<Card> deck) throws Exception {
		
		List<Card> cards = deck;
		Collections.shuffle(cards);		
		return cards;
	}

	@Override
	public List<Card> parseCard(List<Card> deck) throws Exception {
		List<Card> cards = new ArrayList<Card>();
		
		for(Card card : deck){
			String code = card.getCode();
			card.setType(code.substring(0, 4));
			card.setPoint(Integer.parseInt(code.substring(4,5)));
			card.setWhite(Integer.parseInt(code.substring(5,6)));
			card.setBlue(Integer.parseInt(code.substring(6,7)));
			card.setGreen(Integer.parseInt(code.substring(7,8)));
			card.setRed(Integer.parseInt(code.substring(8,9)));
			card.setBlack(Integer.parseInt(code.substring(9)));
			cards.add(card);
		}
		return cards;
	}	
}
