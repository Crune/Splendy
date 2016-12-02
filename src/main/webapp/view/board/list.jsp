<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>게시판</title>
</head>
  
  <body>
  	<table width="600" border="0" cellspacing="0" cellpadding="2">
  		<tr>
  			<td align="center"><h2>게시판</h2></td>
  		</tr>
  		<tr>
  			<td height="20"></td>
  		</tr>
  	</table>
  	
  
	<table width="600" border="0" cellspacing="0" cellpadding="2">
	      <tr align="center" bgcolor="#F3F3F3">
      			<td width="50"><strong>번호</strong></td>
				<td width="350"><strong>제목</strong></td>
        		<td width="70"><strong>글쓴이</strong></td>
        		<td width="80"><strong>날짜</strong></td>
				<td width="50"><strong>조회</strong></td>
      	  </tr>
      	  <tr bgcolor="#777777">
        		<td height="1" colspan="5"></td>
      	  </tr>

	      <c:forEach var="article" items="${article}">
	      	<tr bgcolor="#FFFFFF"  align="center">
        		<td>${article.at_id}</td>
        		<td align="left"> &nbsp;<a href="http://localhost/board/view?at_id=${article.at_id}&bd_id=${bd_id}&currentPage=${currentPage}">${article.at_subject}</a></td>
        		<td align="center">${article.u_id}</td>
				<td align="center"><fmt:formatDate value="${article.at_reg_date}" type="date"/></td>
        		<td>${article.at_readcount}</td>
      	    </tr>
      	    <tr bgcolor="#777777">
        		<td height="1" colspan="5"></td>
      	    </tr>
	      </c:forEach>
			
	      <c:if test="${article.size() == 0}">
	      	<tr bgcolor="#FFFFFF"  align="center">
				<td colspan="5">등록된 게시물이 없습니다.</td>
            </tr>						
	        <tr bgcolor="#777777">
      			<td height="1" colspan="5"></td>
    	    </tr>   
	      </c:if>
	
    	  <tr align="center">
    	  	<td colspan="5">${pagingHtml}</td>
    	  </tr>
    	
    	  <tr align="right">
    		<td colspan="5">
    		<input type="button" value="글쓰기" class="input" onClick="javascript:location.href='http://localhost/board/writeForm?currentPage=${currentPage}&bd_id=${bd_id}';">
		    </td>
    	  </tr>
	</table>
   </body>
</html>





<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/view/board/include/boardinclude.jspf" %>
</head>
<body>
    <h2>게시판 목록</h2>

    <table class="board_list">
        <colgroup>
            <col width="8%"/>
            <col width="*"/>
            <col width="15%"/>
            <col width="30%"/>
            <col width="10%"/>
        </colgroup>
        <thead>
            <tr>
                <th scope="col">글번호</th>
                <th scope="col">제목</th>  
                <th scope="col">작성자</th>              
                <th scope="col">작성일</th>
                <th scope="col">조회</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${fn:length(article) > 0}">
                    <c:forEach items="${article }" var="row">
                        <tr>
                            <td>${row.at_id }</td>
                            <td class="at_subject">
                                <a href="#this" name="at_subject">${row.at_subject }</a>
                                <input type="hidden" id="at_id" value="${row.at_id }">
                            </td>                                                      
                            <td>${row.u_id }</td>
                            <td>${row.at_reg_date }</td>
                            <td>${row.at_readcount }</td> 
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="4">조회된 결과가 없습니다.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
      <div id="PAGE_NAVI"></div>
    <input type="hidden" id="PAGE_INDEX" name="PAGE_INDEX"/>
     
    <br/> 
     
 <c:if test="${count > 0}">
   <c:set var="pageCount" value="${count / pageSize + ( count % pageSize == 0 ? 0 : 1)}"/>
   <c:set var="startPage" value="${pageGroupSize*(numPageGroup-1)+1}"/>
   <c:set var="endPage" value="${startPage + pageGroupSize-1}"/>
   
   <c:if test="${endPage > pageCount}" >
     <c:set var="endPage" value="${pageCount}" />
   </c:if>
          
   <c:if test="${numPageGroup > 1}">
        <a href="./list.do?pageNum=${(numPageGroup-2)*pageGroupSize+1 }">[이전]</a>
   </c:if>


   <c:forEach var="i" begin="${startPage}" end="${endPage}">
       <a href="list.do?pageNum=${i}">[
        <font color="#000000" />
          <c:if test="${currentPage == i}">
          <font color="#bbbbbb" />
        </c:if>
        ${i}
       </font>]
       </a>
   </c:forEach>


   <c:if test="${numPageGroup < pageGroupCount}">
        <a href="./list.do?pageNum=${numPageGroup*pageGroupSize+1}">[다음]</a>
   </c:if>
</c:if>

    <a href="#this" class="btn" id="write">글쓰기</a>

    <%@ include file="/view/board/include/bodyinclude.jspf" %>
    <script type="text/javascript">
        $(document).ready(function(){
            $("#write").on("click", function(e){ //글쓰기 버튼
                e.preventDefault();
                fn_openBoardWrite();
            }); 
             
            $("a[name='at_subject']").on("click", function(e){ //제목 
                e.preventDefault();
                fn_openBoardDetail($(this));
            });
        });
         
         
        function fn_openBoardWrite(){
            var comSubmit = new ComSubmit();
            comSubmit.setUrl("<c:url value='/bbs/write?bName=1' />");
            comSubmit.submit();
        }
         
        function fn_openBoardDetail(obj){
            var comSubmit = new ComSubmit();
            comSubmit.setUrl("<c:url value='/bbs/view' />");
            comSubmit.addParam("at_id", obj.parent().find("#at_id").val());
            comSubmit.submit();
        }       

    </script> 

</body>
</html> --%>