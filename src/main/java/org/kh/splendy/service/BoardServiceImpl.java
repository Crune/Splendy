package org.kh.splendy.service;

import java.util.List;

import org.kh.splendy.mapper.ArticleMapper;
import org.kh.splendy.mapper.BoardMapper;
import org.kh.splendy.vo.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@MapperScan(basePackages = { "org.kh.splendy.dao" })
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMap;
	
	@Autowired
	private ArticleMapper articleMap;

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

	@Override
	public List<Article> getList(String bName) throws Exception {
		/** TODO 찬우.게시판: 게시글 목록 반환 구현
		 * boardMap 이용하여 작성
		 */
		List<Article> article = boardMap.getList(bName);
		return article;
	}

	@Override
	public Article getArticle(int articleId) {
		/** TODO 찬우.게시판: 게시글 내용 반환 구현
		 * boardMap 이용하여 작성
		 */
		
		return null;
	}

	@Override
	public int boardCount() throws Exception {
		int result = boardMap.boardCount();
		return result;
	}

	@Override
	public String write(String bName) throws Exception {
		
		return null;
	}
	
		
	
}
