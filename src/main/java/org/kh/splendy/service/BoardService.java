package org.kh.splendy.service;

import java.util.List;

import org.kh.splendy.vo.Article;
import org.kh.splendy.vo.Board;
import org.springframework.web.bind.annotation.RequestParam;

public interface BoardService {

	List<Article> getList(String bName) throws Exception;

	Article getArticle(int articleId) throws Exception;
	
	int boardCount() throws Exception;
	
	String write(String bName) throws Exception;
}
