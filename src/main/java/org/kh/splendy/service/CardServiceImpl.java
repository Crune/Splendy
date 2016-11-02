package org.kh.splendy.service;

import javax.annotation.Resource;

import org.kh.splendy.dao.CardDAO;
import org.springframework.stereotype.Service;

@Service("cardSerivce")
public class CardServiceImpl implements CardService {

	@Resource(name = "cardDAO")
	private CardDAO dao;
}
