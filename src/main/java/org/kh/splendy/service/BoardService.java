package org.kh.splendy.service;

import java.util.List;

import org.kh.splendy.vo.Article;
import org.kh.splendy.vo.Board;

public interface BoardService {

	List<Article> getList(String bName) throws Exception;

	Article getArticle(int articleId) throws Exception;
	
	int boardCount() throws Exception;
}
