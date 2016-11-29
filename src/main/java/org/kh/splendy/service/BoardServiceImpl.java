package org.kh.splendy.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.kh.splendy.mapper.ArticleMapper;
import org.kh.splendy.mapper.BoardMapper;
import org.kh.splendy.vo.Article;
import org.kh.splendy.vo.Comment;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMap;
	
	@Autowired
	private ArticleMapper articleMap;

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

	/** TODO 찬우.게시판: 게시글 목록 반환 구현
	 * boardMap 이용하여 작성
	 */
	@Override
	public List<Article> getList(String bName) throws Exception {
		
		List<Article> article = boardMap.getList(bName);
		return article;
		
	}
	
	@Override
	public int boardCount() throws Exception {
		int result = boardMap.boardCount();
		return result;
	}

	@Override
	public void writePro(@ModelAttribute("article") Article article) throws Exception {		
		boardMap.writePro(article);
		
	}

	@Override
	public void reply(HashMap<String, String> map) throws Exception {
		boardMap.reply(map);
		
	}

	@Override
	public int max() throws Exception {
		int max = boardMap.max();
		return max;
	}

	@Override
	public Article getDetail(int at_id) throws Exception {
		Article article = boardMap.getDetail(at_id);
		return article;
	}

	@Override
	public void readCount(int at_id) throws Exception {		
		boardMap.readCount(at_id);		
	}
	
	@Override
	public void updateBoard(Article article) throws Exception{
		boardMap.updateBoard(article);
	}

	@Override
	public void deleteBoard(int at_id) throws Exception {
		boardMap.deleteBoard(at_id);
		
	}
	
	@Override
    public void insertBoardReply(Comment comment) {
        if (comment.getRe_id()==0 || "".equals(comment.getRe_id())) {
            //sqlSession.insert("insertBoard5Reply", comment);
        } else {
            //sqlSession.insert("updateBoard5Reply", comment);
        }
    }
	
}
