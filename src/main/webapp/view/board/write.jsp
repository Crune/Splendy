<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/view/board/include/boardinclude.jspf" %>
</head>
<body>
    <form id="frm">
        <table class="board_view">
            <colgroup>
                <col width="15%">
                <col width="*"/>
            </colgroup>
            <caption>게시글 작성</caption>
            <tbody>
                <tr>
                    <th scope="row">제목</th>
                    <td><input type="text" id="TITLE" name="at_subject" class="wdp_90"></input></td>
                </tr>
                <tr>
                    <td colspan="2" class="view_text">
                        <textarea rows="20" cols="100" title="내용" id="CONTENTS" name="at_content"></textarea>
                    </td>
                </tr>
            </tbody>
        </table>
         
        <a href="#this" class="btn" id="write" >작성하기</a>
        <a href="#this" class="btn" id="list" >목록으로</a>
    </form>
     
    <%@ include file="/view/board/include/boardinclude.jspf" %>
    <script type="text/javascript">
       
    
       	$(document).ready(function(){
       		$("#list").on("click",function(e){
				e.preventDefault();
				fn_openBoardList();
       		});
       		$("#write").on("click",function(e){ 
       	        e.preventDefault();
       	        fn_insertBoard();
       	   	});
       	});
    
        
       	function fn_openBoardList(){
       	    var comSubmit = new ComSubmit();
       	    comSubmit.setUrl("<c:url value='/bbs/list'/>");
       	    comSubmit.submit();
       	}
       	function fn_insertBoard(){
       	    var comSubmit = new ComSubmit("frm");
       	    comSubmit.setUrl("<c:url value='/bbs/writePro'/>");       	    
       	    comSubmit.submit();       	
       	}
    </script>
</body>
</html>