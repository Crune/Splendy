<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/view/board/include/boardinclude.jspf"%>
</head>
<center>
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
	                 <input type="hidden" id="bd_id" value="${article.bd_id }">
	                 <input type="hidden" id="at_id" value="${article.at_id }">
	                <th scope="row">조회수</th>
	            	<td>${article.at_readcount }</td>
	            </tr>
	            <tr>
	                <th scope="row">작성자</th>
	            	<td>${article.u_id }</td>
	                <th scope="row">작성시간</th>
	                <td><fmt:formatDate value="${article.at_reg_date}" type="date"/></td>
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
	    <c:if test="${article.u_id == sessionScope.user.id }">
	    <a href="#this" class="btn" id="update">수정하기</a>
	    <a href="#this" class="btn" id="delete">삭제하기</a>
	    </c:if>
	</form>  	
	  
<p>&nbsp;</p>
<div style="border: 1px solid; width: 600px; padding: 5px">
    <form id="form1" action="/bbs/insertReply" method="post">
        <input type="hidden" name="at_id" value="<c:out value="${article.at_id}"/>" > 
        작성자: ${sessionScope.user.nickname}<input type="hidden" name="u_id" value="${sessionScope.user.id}"> <br/>
        <textarea name="cm_cont" rows="3" cols="60" maxlength="500" placeholder="댓글을 달아주세요."></textarea>
        <a href="#" onclick="fn_formSubmit()">저장</a>
        
    </form>    
</div>

<c:forEach var="row" items="${comment}" >
    <div style="border: 1px solid gray; width: 600px; padding: 5px; margin-top: 5px;">    
        <c:out value=""/>
        <a href="#" onclick="fn_replyDelete('<c:out value="${row.re_id}"/>')">삭제</a>
        <a href="#" onclick="fn_replyUpdate('<c:out value="${row.re_id}"/>')">수정</a>
        <br/>
        <div id="reply<c:out value="${row.re_id}"/>">${row.u_id} : ${row.cm_cont}</div>
    </div>
</c:forEach>

<div id="replyDiv" style="width: 99%; display:none">
    <form name="form2" action="board5ReplySave" method="post">
        <input type="hidden" name="brdno" value="<c:out value="${boardInfo.brdno}"/>"> 
        <input type="hidden" name="reno"> 
        <input type="hidden" name="at_id" value="${article.at_id}" />
        <input type="hidden" name="bd_id" value="${article.bd_id}" />
        <textarea name="rememo" rows="3" cols="60" maxlength="500"></textarea>
        <a href="#" onclick="fn_replyUpdateSave()">저장</a>
        <a href="#" onclick="fn_replyUpdateCancel()">취소</a>
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
        comSubmit.setUrl("<c:url value='/board/list?bd_id=${bd_id}' />");
        comSubmit.submit();
    }
    function fn_openBoardUpdate(){
       	var comSubmit = new ComSubmit("frm");
        comSubmit.setUrl("<c:url value='/board/modify' />");
        comSubmit.addParam("at_id", $("#at_id").val());
        comSubmit.addParam("bd_id", $("#bd_id").val());
        comSubmit.submit();
    } 
    function fn_deleteBoard(){
        var comSubmit = new ComSubmit("frm");
        comSubmit.setUrl("<c:url value='/board/deletePro' />");
        comSubmit.addParam("at_id", $("#at_id").val());
        comSubmit.submit();             
    }
    function fn_formSubmit() {
    	var comSubmit = new ComSubmit("form1");
        comSubmit.setUrl("<c:url value='/board/insertReply' />");
        comSubmit.addParam("at_id", $("#at_id").val());
        comSubmit.addParam("bd_id", $("#bd_id").val());
        comSubmit.submit();             
	}
    function fn_replyDelete(re_id) {
    	document.location.href='/board/deleteReply?re_id='+re_id+'&bd_id='+${bd_id}+'&at_id='+${at_id};
    }
</script>
</body>
</center>
</html>   