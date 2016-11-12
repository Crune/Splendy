package org.kh.splendy.service;

import java.util.HashMap;
import java.util.List;

import org.kh.splendy.vo.Article;
import org.kh.splendy.vo.Board;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface BoardService {

	List<Article> getList(String bName) throws Exception;

	Article getArticle(int articleId) throws Exception;
	
	int boardCount() throws Exception;
	
	String writePro(Article article, BindingResult result, RedirectAttributes rttr) throws Exception;
	
	void reply(HashMap<String, String> map) throws Exception;
	
	int max() throws Exception;
	
}
