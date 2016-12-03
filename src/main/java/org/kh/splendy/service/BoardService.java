package org.kh.splendy.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.kh.splendy.vo.Article;
import org.kh.splendy.vo.Board;
import org.kh.splendy.vo.Comment;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface BoardService {

	List<Article> getList(int bd_id) throws Exception;

	Article getDetail(int at_id) throws Exception;
	
	int boardCount(int bd_id) throws Exception;
	
	void writePro(@ModelAttribute("article") Article article) throws Exception;
	
	void reply(HashMap<String, String> map) throws Exception;
	
	int max() throws Exception;	 
	 
	void readCount(int at_id) throws Exception;
	
	void updateBoard(Article article) throws Exception;
	
	void deleteBoard(int at_id)throws Exception;	
	
	void insertReply(@ModelAttribute("comment") Comment comment) throws Exception;
	
	void updateReply(@ModelAttribute("comment") Comment comment) throws Exception;
	
	void deleteReply(int re_id) throws Exception;
	
	List<Comment> replyList(int at_id) throws Exception;
	
}

