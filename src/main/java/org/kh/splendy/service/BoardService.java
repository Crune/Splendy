package org.kh.splendy.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.kh.splendy.vo.Article;
import org.kh.splendy.vo.Board;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface BoardService {

	List<Article> getList(String bName) throws Exception;

	Article getDetail(int at_id) throws Exception;
	
	int boardCount() throws Exception;
	
	void writePro(@ModelAttribute("BoardVO") Article article) throws Exception;
	
	void reply(HashMap<String, String> map) throws Exception;
	
	int max() throws Exception;	 
	 
	void readCount(int at_id) throws Exception;
	
	void updateBoard(int at_id) throws Exception;
	
	
	
}
