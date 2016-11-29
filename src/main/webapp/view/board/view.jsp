<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/view/board/include/boardinclude.jspf"%>
</head>
<body>
	  <form id="frm">
	    <table class="board_view">
	        <colgroup>
	            <col width="15%"/>
	            <col width="35%"/>
	            <col width="15%"/>
	            <col width="35%"/>
	        </colgroup>
	        <caption>게시글 상세</caption>
	        <tbody>
	            <tr>
	                <th scope="row">글 번호</th>
	                <td>${article.at_id }</td>
	                 <input type="hidden" id="at_id" value="${article.at_id }">
	                <th scope="row">조회수</th>
	            	<td>${article.at_readcount }</td>
	            </tr>
	            <tr>
	                <th scope="row">작성자</th>
	            <td></td>
	                <th scope="row">작성시간</th>
	                <td>${article.at_reg_date }</td>
	            </tr>
	            <tr>
	                <th scope="row">제목</th>
	                <td colspan="3">${article.at_subject }</td>
	            </tr>
	            <tr>
	                <td colspan="4">${article.at_content }</td>
	            </tr>
	        </tbody>
	    </table>
	     
	    <a href="#this" class="btn" id="list" >목록으로</a>
	    <a href="#this" class="btn" id="update">수정하기</a>
	    <a href="#this" class="btn" id="delete">삭제하기</a>
	</form>  	   
<p>&nbsp;</p>
<div style="border: 1px solid; width: 600px; padding: 5px">
    <form id="form1" action="/bbs/insertReply" method="post">
        <input type="hidden" name="at_id" value="<c:out value="${article.at_id}"/>" > 
        작성자: <input type="text" name="u_id" size="20" maxlength="20"> <br/>
        <textarea name="cm_cont" rows="3" cols="60" maxlength="500" placeholder="댓글을 달아주세요."></textarea>
        <a href="#" onclick="fn_formSubmit()">저장</a>
        
    </form>
</div>
 
    <%@ include file="/view/board/include/boardinclude.jspf" %>
	    <script type="text/javascript">
    $(document).ready(function(){
        $("#list").on("click", function(e){ //목록으로 버튼
            e.preventDefault();
            fn_openBoardList();
        });
         
        $("#update").on("click", function(e){
            e.preventDefault();
            fn_openBoardUpdate();
        });
        
        $("#delete").on("click", function(e){ //삭제하기 버튼
            e.preventDefault();
            fn_deleteBoard();
        });
    });
     
    function fn_openBoardList(){
        var comSubmit = new ComSubmit("frm");
        comSubmit.setUrl("<c:url value='/bbs/list?pageNum=1&bName=1' />");
        comSubmit.submit();
    }
    function fn_openBoardUpdate(){
       	var comSubmit = new ComSubmit("frm");
        comSubmit.setUrl("<c:url value='/bbs/mod?bName=1' />");
        comSubmit.addParam("at_id", $("#at_id").val());
        comSubmit.submit();
    } 
    function fn_deleteBoard(){
        var comSubmit = new ComSubmit("frm");
        comSubmit.setUrl("<c:url value='/bbs/deletePro' />");
        comSubmit.addParam("at_id", $("#at_id").val());
        comSubmit.submit();             
    }
    function fn_formSubmit() {
    	var comSubmit = new ComSubmit("form1");
        comSubmit.setUrl("<c:url value='/bbs/insertReply' />");
        comSubmit.addParam("at_id", $("#at_id").val());
        comSubmit.submit();             
	}
</script>
</body>
</html>   