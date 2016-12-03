<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/view/board/include/boardinclude.jspf" %>
	<title>Splendy - 환영합니다!</title>
</head>
<body>
    <form id="articleForm"> 
        <c:if test="${article == null}">
        <table class="board_view">
            <colgroup>
                <col width="15%">
                <col width="*"/>
            </colgroup>
            <caption>게시글 작성</caption>
            <tbody>
                <tr>
                    <th scope="row">제목</th>
                    <td><input type="text" id="at_subject" name="at_subject" class="wdp_90"></input>         	 
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="view_text">
                        <textarea rows="20" cols="100" title="내용" id="at_content" name="at_content"></textarea>
                    </td>
                <input type="hidden" name="u_id" value="${sessionScope.user.id}" />   
                <input type="hidden" name="bd_id" value="${bd_id}" /> 
                </tr>
            </tbody>
        </table>
        </c:if>   
        
        <c:if test="${article != null}">
        <table class="board_view">
            <colgroup>
                <col width="15%">
                <col width="*"/>
            </colgroup>
            <caption>게시글 수정</caption>
            <tbody>
                <tr>
                    <th scope="row">제목</th>
                    <td><input type="text" id="at_subject" name="at_subject" class="wdp_90" value="${article.at_subject }"></input>
                    	
                    </td>
                    
                </tr>
                <tr>
                    <td colspan="2" class="view_text">
                        <textarea rows="20" cols="100" title="내용" id="at_content" name="at_content">${article.at_content }</textarea>
                    </td>
        			<input type="hidden" name="at_id" value="${article.at_id}" />
        			<input type="hidden" name="bd_id" value="${bd_id}" />			           
                </tr>
            </tbody>
        </table>
        </c:if>
     </form>
        <a href="#this" class="btn" id="write" >작성하기</a>
        <a href="#this" class="btn" id="list" >목록으로</a>      
       
 
     
    
    <script type="text/javascript">
    $(document).ready(function(){
        $("#list").on("click", function(e){ //목록으로 버튼
            e.preventDefault();
            fn_openBoardList();
        });
         
        $("#write").on("click", function(e){ //작성하기 버튼
            e.preventDefault();
            fn_insertBoard();
        });
    });
     
    function fn_openBoardList(){
        var comSubmit = new ComSubmit("articleForm");
        comSubmit.setUrl("<c:url value='/board/list' />");
        comSubmit.addParam("bd_id", $("#bd_id").val());
        comSubmit.submit();
    }
     
    function fn_insertBoard(){
        var comSubmit = new ComSubmit("articleForm");
        comSubmit.setUrl("<c:url value='/board/write' />");
        comSubmit.submit();
    }
</script>
</body>
</html>