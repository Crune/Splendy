package org.kh.splendy.service;

import org.kh.splendy.mapper.*;
import org.kh.splendy.vo.Card;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@MapperScan(basePackages = { "org.kh.splendy.dao" })
public class CardServiceImpl implements CardService {

	@Autowired
	private CardMapper cardDao;

	private static final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);
	
	@Override
	public void getAll() throws Exception {
		for (Card card : cardDao.selectAll()) {
			log.info("card info : "+card.toString());
		}
	}

	@Override
	public Card get(int id) throws Exception {
		Card card = cardDao.select(id);
		log.info("card info : "+card);
		return card;
	}
}
