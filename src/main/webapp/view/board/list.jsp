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
            <col width="10%"/>
            <col width="*"/>
            <col width="15%"/>
            <col width="20%"/>
        </colgroup>
        <thead>
            <tr>
                <th scope="col">글번호</th>
                <th scope="col">제목</th>                
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
    <br/>
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