package org.kh.splendy.controller;

import org.kh.splendy.vo.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * TODO 찬우.게시판: 컨트롤러 구현
 * 
 * @author 찬우
 *
 */
@Controller
public class BoardController {

	@RequestMapping("/list.bbs?={boardName}/")
	public String list(@RequestParam String name) {
		/**
		 * TODO 찬우.게시판: 게시판 리스트 구현
		 * 
		 * 게시판 이름으로 접속 가능하도록 구현
		 */
		return "board/list";
	}
	

	@RequestMapping(value = "/bbs/{boardName}/view={articleId}", method = RequestMethod.GET)
	public String view(@PathVariable String boardName, @PathVariable String articleId) {
		/**
		 * TODO 찬우.게시판: 게시글 보기 구현
		 */
		return "board/view";
	}
	

	@RequestMapping(value = "/bbs/{boardName}/view={articleId}", method = RequestMethod.GET)
	public String test(@PathVariable String boardName, @PathVariable String articleId) {
		/**
		 * TODO 찬우.게시판: 게시글 수정 구현
		 */
		return "board/view";
	}
}
