package org.kh.splendy.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.kh.splendy.vo.Article;
import org.kh.splendy.vo.Board;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface BoardService {

	List<Article> getList(String bName) throws Exception;

	Article getArticle(int articleId) throws Exception;
	
	int boardCount() throws Exception;
	
	void writePro(String at_subject ,String at_content ,String at_pass ) throws Exception;
	
	void reply(HashMap<String, String> map) throws Exception;
	
	int max() throws Exception;
	 
	 
	
}
