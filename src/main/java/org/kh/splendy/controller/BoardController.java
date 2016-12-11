package org.kh.splendy.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.kh.splendy.service.BoardService;
import org.kh.splendy.vo.Article;
import org.kh.splendy.vo.Comment;
import org.kh.splendy.vo.UserCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("board")
public class BoardController {
	@Autowired 
	private BoardService boardServ;
	
	@RequestMapping(value = "/list", method = {RequestMethod.GET,RequestMethod.POST})
	public String list(HttpServletRequest request) throws Exception{
		
		String bd_id = request.getParameter("bd_id");
		String pageNum = null;
		List<Article> article = null;
		List<Article> pageArticle = null;
		
		int currentPage = 1;   // 현재페이지
		int totalCount = boardServ.boardCount(Integer.parseInt(bd_id));	 // 전체 게시물 수
		int totalPage;	 // 전체 페이지 수
		int blockCount = 10;	 // 한 페이지의  게시물의 수
		int blockPage = 5;	 // 한 화면에 보여줄 페이지 수
		int startCount;	 // 한 페이지에서 보여줄 게시글의 시작 번호
		int endCount;	 // 한 페이지에서 보여줄 게시글의 끝 번호
		int startPage;	 // 시작 페이지
		int endPage;	 // 마지막 페이지
		int number;		 // 뷰에서 사용할 글 번호
		StringBuffer pagingHtml = new StringBuffer();
		
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
		System.out.println("startCount"+startCount);
		System.out.println("endCount"+endCount);
		// 마지막 페이지가 전체 페이지 수보다 크면 전체 페이지 수로 설정
		if (endPage > totalPage) {
			endPage = totalPage;
		}
		
		// 이전 block 페이지
		pagingHtml = new StringBuffer();
		pagingHtml.append("&nbsp;&nbsp;|&nbsp;&nbsp;");
		
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
				pagingHtml.append("&nbsp;<a href='/board/list?bd_id="+bd_id+"&currentPage=");
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
		
		if (request.getParameter("bd_id") == null || "".equals(bd_id)) {
			bd_id = "1";
		} else {
			bd_id = request.getParameter("bd_id");
		}

        if (totalCount > 0) {
            article = boardServ.getList(Integer.parseInt(bd_id));
        } else {
        	article = Collections.emptyList();
        }
        
        if (endCount+1 < article.size()){
        	pageArticle = article.subList(startCount, endCount+1);
        } else if (endCount+1 >= article.size()){
        	pageArticle = article.subList(startCount, article.size());
        }
        
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
	
	@RequestMapping("/view")
	public String view(@RequestParam("at_id") int at_id, @RequestParam("bd_id") int bd_id, Model model) throws Exception {
		
		Article article = boardServ.getDetail(at_id);
		
		boardServ.readCount(at_id);
		int u_id = article.getU_id();
		List<Comment> comment = null;    	
		comment = boardServ.replyList(at_id);		
				
		model.addAttribute("article",article);
		model.addAttribute("comment", comment);
		model.addAttribute("at_id", at_id);
		model.addAttribute("bd_id", bd_id);
		
		return "board/view";
	}
	
	@RequestMapping(value ="/writeForm") 
	public String write(@RequestParam("bd_id") int bd_id, HttpServletRequest request) throws Exception{
		request.setAttribute("bd_id", bd_id);
		if(request.getParameter("at_id") != null){
			int at_id = Integer.parseInt(request.getParameter("at_id"));
			Article article = boardServ.getDetail(at_id);
			request.setAttribute("article", article);
		}
		return "board/write";
	}
	
	@RequestMapping(value = "/write", method = {RequestMethod.GET,RequestMethod.POST})
	public String writePro(@ModelAttribute("articleForm") Article article, Model model)throws Exception{
		System.out.println("u_id = "+article.getU_id());
		if(article.getAt_id() == 0){
			boardServ.writePro(article);						
		}else{									
			boardServ.updateBoard(article);
		}
		int bd_id = article.getBd_id();
		
		return "redirect:/board/list?bd_id="+bd_id;
	}
	
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modify(@ModelAttribute("frm") Article frmArticle, RedirectAttributes rttr,Model model) throws Exception {
		int at_id = frmArticle.getAt_id();
		int bd_id = frmArticle.getBd_id();
		return "redirect:/board/writeForm?bd_id="+bd_id+"&at_id="+at_id;
	}
	
	@RequestMapping(value = "/deletePro", method = RequestMethod.POST)
	public String delete(@RequestParam("at_id") int at_id) throws Exception {
		boardServ.deleteBoard(at_id);
		
		return "redirect:/board/list";
	}
	
	@RequestMapping(value = "/insertReply", method = RequestMethod.POST)
    public String insertUpdateReply(@ModelAttribute("comment") Comment comment, @RequestParam("at_id") int at_id, @RequestParam("bd_id") int bd_id) throws Exception {
             
        if (comment.getRe_id()==0 || "".equals(comment.getRe_id())) {
    		 boardServ.insertReply(comment);
         } else {
        	 boardServ.updateReply(comment);
         }    	     	
    	
        return "redirect:/board/view?bd_id="+bd_id+"&at_id="+at_id;
    } 
	
	@RequestMapping("/deleteReply")
	public String deleteReply(@RequestParam("re_id") int re_id, @RequestParam("bd_id") int bd_id, @RequestParam("at_id") int at_id) throws Exception {
		boardServ.deleteReply(re_id);
		
		return "redirect:/board/view?bd_id="+bd_id+"&at_id="+at_id;
	}
	
}
