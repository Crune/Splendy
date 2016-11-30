<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
</html>