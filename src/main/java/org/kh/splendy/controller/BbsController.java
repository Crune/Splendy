/*package org.kh.splendy.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.kh.splendy.service.BoardService;
import org.kh.splendy.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

*//** TODO 찬우.게시판: 컨트롤러 구현
 * @author 찬우 *//*
@Controller

@RequestMapping("board")
public class BbsController {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);

	@Autowired
	private BoardService boardServ;

	@RequestMapping(value = "/list", method = {RequestMethod.GET,RequestMethod.POST})
	*//** 게시글 목록
	 * @param bName 게시판이름
	 * 
	 *//*
	public String list(HttpServletRequest request) throws Exception{
		*//** 
		 * 게시판 이름으로 접속 가능하도록 구현
		 * bName이 설정되어 있지 않을경우 default로 설정
		 *//*
		
		String bd_id = null;
		String pageNum = null;
		List<Article> article = null;
		List<Article> pageArticle = null;
		
		int currentPage = 1;   // 현재페이지
		int totalCount = boardServ.boardCount();	 // 전체 게시물 수
		int totalPage;	 // 전체 페이지 수
		int blockCount = 5;	 // 한 페이지의  게시물의 수
		int blockPage = 5;	 // 한 화면에 보여줄 페이지 수
		int startCount;	 // 한 페이지에서 보여줄 게시글의 시작 번호
		int endCount;	 // 한 페이지에서 보여줄 게시글의 끝 번호
		int startPage;	 // 시작 페이지
		int endPage;	 // 마지막 페이지
		int number;		 // 뷰에서 사용할 글 번호
		StringBuffer pagingHtml;
		
		// 전체 페이지 수
		totalPage = (int) Math.ceil((double) totalCount / blockCount);
		if (totalPage == 0) {
			totalPage = 1;
		}

        if (request.getParameter("currentPage") != null) {
            currentPage = Integer.parseInt(request.getParameter("currentPage"));
        }

		// 현재 페이지가 전체 페이지 수보다 크면 전체 페이지 수로 설정
		if (currentPage > totalPage) {
			currentPage = totalPage;
		}

		// 현재 페이지의 처음과 마지막 글의 번호 가져오기.
		startCount = (currentPage - 1) * blockCount;
		endCount = startCount + blockCount - 1;

		// 시작 페이지와 마지막 페이지 값 구하기.
		startPage = (int) ((currentPage - 1) / blockPage) * blockPage + 1;
		endPage = startPage + blockPage - 1;
		System.out.println("startPage"+startPage);
		System.out.println("endPage"+endPage);
		// 마지막 페이지가 전체 페이지 수보다 크면 전체 페이지 수로 설정
		if (endPage > totalPage) {
			endPage = totalPage;
		}
		
		// 이전 block 페이지
		pagingHtml = new StringBuffer();
		if (currentPage > blockPage) {
			pagingHtml.append("<a href=/list?currentPage=" + (startPage - 1) + ">");
			pagingHtml.append("이전");
			pagingHtml.append("</a>");
		}
		
		//페이지 번호.현재 페이지는 빨간색으로 강조하고 링크를 제거.
		for (int i = startPage; i <= endPage; i++) {
			if (i > totalPage) {
				break;
			}
			if (i == currentPage) {
				pagingHtml.append("&nbsp;<b> <font color='red'>");
				pagingHtml.append(i);
				pagingHtml.append("</font></b>");
			} else {
				pagingHtml.append("&nbsp;<a href='/list?currentPage=");
				pagingHtml.append(i);
				pagingHtml.append("'>");
				pagingHtml.append(i);
				pagingHtml.append("</a>");
			}
			pagingHtml.append("&nbsp;");
		}
		pagingHtml.append("&nbsp;&nbsp;|&nbsp;&nbsp;");
		
		// 다음 block 페이지
		if (totalPage - startPage >= blockPage) {
			pagingHtml.append("<a href=/list?currentPage=" + (endPage + 1) + ">");
			pagingHtml.append("다음");
			pagingHtml.append("</a>");
		}
		
		if (request.getParameter("bd_id") == null) {
			bd_id = "1";
		}

        if (totalCount > 0) {
            article = boardServ.getList(Integer.parseInt(bd_id));
        } else {
        	article = Collections.emptyList();
        }
        pageArticle = article.subList(startCount, endCount+1);
        
		number = totalCount-(currentPage-1)*blockCount;//글목록에 표시할 글번호
        //해당 뷰에서 사용할 속성
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("blockCount", blockCount);
		request.setAttribute("blockPage", blockPage);
		request.setAttribute("currentPage", new Integer(currentPage));
		request.setAttribute("pagingHtml", pagingHtml);
		request.setAttribute("number", number);		
		request.setAttribute("article", pageArticle);
		request.setAttribute("bd_id", bd_id);
		
		return "board/list";
	}
	

	@RequestMapping(value = "/view")
	*//** 게시글 보기
	 * 
	 *//*
	public String view(@RequestParam("at_id") int at_id, @RequestParam("bd_id") int bd_id, @RequestParam("currentPage") int currentPage, Model model) throws Exception {
		
		Article article = boardServ.getDetail(at_id);
		
		boardServ.readCount(at_id);
		
		List<Comment> comment = null;    	
		comment = boardServ.replyList(at_id);		
				
		model.addAttribute("article",article);
		model.addAttribute("comment", comment);
		model.addAttribute("at_id", at_id);
		model.addAttribute("bd_id", bd_id);
		
		return "board/view";
	}
	
	@RequestMapping(value ="/writeForm") //여기부터 다시
	public String write(@RequestParam("currentPage")int currentPage, @RequestParam("bd_id") int bd_id, RedirectAttributes rttr) {
		
		
		return "board/write";
	}

	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String writePro(@RequestParam("currentPage")int currentPage, @RequestParam("bd_id") int bd_id, Model model)throws Exception{
		boardServ.		
		
		return "";
	}

	@RequestMapping(value = "/mod", method = {RequestMethod.GET,RequestMethod.POST})
	*//** 게시글 수정
	 * @param aId
	 * @return 글쓰기 화면에 해당 게시글 내용이 채워져 있는 화면 *//*
	public String modify(@RequestParam int at_id,@RequestParam String bName,RedirectAttributes rttr,Model model) throws Exception {
		*//** TODO 찬우.게시판: 게시글 수정 구현
		 * - 게시글 쓰기 화면으로 리다이렉트 하되 글쓰기 내용이 채워져 있어야 함.
		 *//*
			
	
		Article article = boardServ.getDetail(at_id);	
		rttr.addFlashAttribute(article);
		//rttr.
		//model.addAttribute("article",article);		
		
		return "redirect:/bbs/write?bName=1";
	}

	

	@RequestMapping(value = "/deletePro", method = {RequestMethod.GET,RequestMethod.POST})
	*//** 게시글 삭제
	 * @param bName 게시판이름
	 * @return 글삭제 확인 화면 *//*
	public String delete(@RequestParam int at_id, RedirectAttributes rttr) throws Exception {
		*//** TODO 찬우.게시판: 게시글 삭제 구현
		 * - 삭제 후 해당 글이 있던 게시판의 목록화면으로 리다이렉트
		 *//*		
		boardServ.deleteBoard(at_id);
		
		rttr.addFlashAttribute("bName","1"); // 해당 게시글의 게시판 읽어와서 설정 요망
		rttr.addFlashAttribute("pageNum", 1);
		
		return "redirect:/bbs/list?pageNum=1&bName=1";
	}
	
    @RequestMapping(value = "/insertReply", method = {RequestMethod.GET,RequestMethod.POST})
    public String insertUpdateReply(@ModelAttribute("comment") Comment comment) throws Exception {
             
        if (comment.getRe_id()==0 || "".equals(comment.getRe_id())) {
    		 boardServ.insertReply(comment);
         } else {
        	 boardServ.updateReply(comment);
         }    	     	
    	
        return "redirect:/insertReply";
    	
    }   
    
}
*/