package org.kh.splendy.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.kh.splendy.sample.SampleController;
import org.kh.splendy.service.BoardService;
import org.kh.splendy.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** TODO 찬우.게시판: 컨트롤러 구현
 * @author 찬우 */
@Controller
@SessionAttributes("article")
public class BoardController {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SampleController.class);

	@Autowired
	private BoardService boardServ;
	
	@ModelAttribute("article")
	/** 뷰어(JSP)에서 article을 요청했을때 선언되지 않을 경우 반환할 값
	 * @return 게시글 읽기 실패했다는 id가 -1인 결과를 반환 */
	public Article article() {
		Article article = new Article();
		article.setU_id(-1);
		article.setSubject("게시글 읽기 실패");
		article.setContent("게시글 읽기에 실패하였습니다.");
		return null;
	}
	
	@RequestMapping(value = "/bbs/list", method = RequestMethod.GET)
	/** 게시글 목록
	 * @param bName 게시판이름
	 * @return	해당 게시판의 게시글 목록 화면 */
	public String list(@RequestParam("pageNum") String pageNum,@RequestParam String bName, ModelAndView model
						,HttpServletRequest request) throws Exception{
		/** TODO 찬우.게시판: 게시글 목록 구현
		 * - 게시판 이름으로 접속 가능하도록 구현
		 * - bName이 설정되어 있지 않을경우 default로 설정
		 */		
		
		String boardName = "default";
		if (bName==null) {			
		} else if (!bName.isEmpty()) {
			boardName = bName;
		}
		
        
        if (pageNum == null) {
            pageNum = "1";
        }
        int pageSize = 10;//한 페이지의 글의 개수
        int currentPage = Integer.parseInt(pageNum);
        int startRow = (currentPage - 1) * pageSize + 1;//한 페이지의 시작글 번호
        int endRow = currentPage * pageSize;//한 페이지의 마지막 글번호
        int count =0;
        int number=0;
        
        List<Article> article = null;
        //BoardDBBean dbPro = BoardDBBean.getInstance();//DB연동
        //count = dbPro.getArticleCount();//전체 글의 수 
        count = boardServ.boardCount();
        
	    HashMap map = new HashMap();
	    map.put("start", startRow);
	    map.put("end", endRow);
        if (count > 0) {
            article = boardServ.getList(bName);
        } else {
        	article = Collections.EMPTY_LIST;
        }
		number=count-(currentPage-1)*pageSize;//글목록에 표시할 글번호
        //해당 뷰에서 사용할 속성
		request.setAttribute("currentPage", new Integer(currentPage));
        request.setAttribute("startRow", new Integer(startRow));
        request.setAttribute("endRow", new Integer(endRow));
        request.setAttribute("count", new Integer(count));
        request.setAttribute("pageSize", new Integer(pageSize));
		request.setAttribute("number", new Integer(number));
		model.addObject("article", article);
		
		
		return "board/list";
		
	}

	@RequestMapping(value = "/bbs/view", method = RequestMethod.GET)
	/** 게시글 보기
	 * @param aId 게시글 번호
	 * @return 해당 게시글의 내용보기 화면 */
	public String view(@RequestParam int aId, Model model) {
		/** TODO 찬우.게시판: 게시글 보기 구현
		 * - 사용자가 작성한 글이 아니고 해당 사용자가 처음 보는 글이라면 읽기 횟수가 증가해야 함
		 * - 목록으로 돌아가기엔 해당 게시판이름에 해당하는 목록의 해당 글이 있는 페이지가 보여야 함
		 */
		//Article article = boardServ.getArticle(aId);
		//model.addAttribute("article",article);
		
		return "board/view";
	}

	@RequestMapping(value = "/bbs/mod", method = RequestMethod.GET)
	/** 게시글 수정
	 * @param aId
	 * @return 글쓰기 화면에 해당 게시글 내용이 채워져 있는 화면 */
	public String modify(@RequestParam int aId, RedirectAttributes rttr) {
		/** TODO 찬우.게시판: 게시글 수정 구현
		 * - 게시글 쓰기 화면으로 리다이렉트 하되 글쓰기 내용이 채워져 있어야 함.
		 */
	
		return "redirect:/bbs/write";
	}

	@RequestMapping(value = "/bbs/write", method = RequestMethod.GET)
	/** 게시글 쓰기
	 * @param bName 게시판이름
	 * @return 글쓰기 화면 */
	public String write(@RequestParam("num") String num,@RequestParam String bName ) {	
		
	
		return "board/write";
	}
	

	@RequestMapping(value = "/bbs/writePro", method = RequestMethod.POST)
	/** 게시글 쓰기
	 * @param bName 게시판이름
	 * @return 글쓰기 화면 */
	public String writePro(@ModelAttribute @Valid Article article, BindingResult result, RedirectAttributes rttr) {
		/** TODO 찬우.게시판: 게시글 쓰기 구현
		 * - 작성 후 해당 글 보기 화면으로 리다이렉트
		 */
		rttr.addFlashAttribute("bName",article.getBd_id());
		return "redirect:/bbs/list";
	}

	@RequestMapping(value = "/bbs/deletePro", method = RequestMethod.GET)
	/** 게시글 삭제
	 * @param bName 게시판이름
	 * @return 글삭제 확인 화면 */
	public String delete(@RequestParam int aId, RedirectAttributes rttr) {
		/** TODO 찬우.게시판: 게시글 삭제 구현
		 * - 삭제 후 해당 글이 있던 게시판의 목록화면으로 리다이렉트
		 */
		rttr.addFlashAttribute("bName","???"); // 해당 게시글의 게시판 읽어와서 설정 요망
		return "redirect:/bbs/list";
	}
	
}
